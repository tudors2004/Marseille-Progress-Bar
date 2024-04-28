import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.openapi.util.ScalableIcon;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.*;

public class NyanProgressBarUi extends BasicProgressBarUI {
    private static final float ONE_OVER_SEVEN = 1f / 7;
    private static final Color VIOLET = new Color(90, 0, 157);

    public static ComponentUI createUI(JComponent c) {
        c.setBorder(JBUI.Borders.empty().asUIResource());
        return new NyanProgressBarUi();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(super.getPreferredSize(c).width, JBUI.scale(20));
    }

    private volatile int offset = 0;
    private volatile boolean isRunning = true;

    @Override
    protected void paintIndeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        setupGraphics(g2, c);
        if (isRunning) {
            drawMovingGraphics(g2, c);
        }
    }

    private void setupGraphics(Graphics2D g, JComponent c) {
        Insets b = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }
        g.setColor(new JBColor(Gray._240.withAlpha(50), Gray._128.withAlpha(50)));
        Dimension preferredSize = c.getPreferredSize();
        int height = isEven(c.getHeight() - preferredSize.height) ? preferredSize.height : preferredSize.height + 1;
        setupPaint(g, c.getWidth(), height);
    }

    private void setupPaint(Graphics2D g, int width, int height) {
        LinearGradientPaint paint = new LinearGradientPaint(0, JBUI.scale(2), 0, height - JBUI.scale(6),
            new float[]{ONE_OVER_SEVEN * 1, ONE_OVER_SEVEN * 2, ONE_OVER_SEVEN * 3, ONE_OVER_SEVEN * 4, ONE_OVER_SEVEN * 5, ONE_OVER_SEVEN * 6, ONE_OVER_SEVEN * 7},
            new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.cyan, Color.blue, VIOLET});
        g.setPaint(paint);
        g.fillRect(0, (height - height) / 2, width, height);
    }

    private void drawMovingGraphics(Graphics2D g, JComponent c) {
        int width = c.getWidth();
        int height = c.getPreferredSize().height;
        g.translate(0, (c.getHeight() - height) / 2);
        try {
            Icon icon = loadIcon(velocity > 0 ? "/rsz_cat.png" : "/rsz_rcat.png");
            if (icon != null) {
                icon.paintIcon(c, g, offset, 0);
            }
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + e.getMessage());
        }
        offset = (offset + 1) % width;
        g.translate(0, -(c.getHeight() - height) / 2);
    }

    private Icon loadIcon(String path) {
        try {
            return new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isEven(int value) {
        return value % 2 == 0;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }
}

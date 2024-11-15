import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.ide.ui.LafManagerListener;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;

public class MarseilleApplicationComponent implements LafManagerListener, ApplicationActivationListener {
    public MarseilleApplicationComponent() {
    }

    @Override
    public void lookAndFeelChanged(@NotNull LafManager lafManager) {
        // see https://plugins.jetbrains.com/docs/intellij/plugin-listeners.html
        updateProgressBarUi();
    }

    @Override
    public void applicationActivated(@NotNull IdeFrame ideFrame) {
        updateProgressBarUi();
    }
    private void updateProgressBarUi() {
        UIManager.put("ProgressBarUI", MarseilleProgressBarUi.class.getName());
        UIManager.getDefaults().put(MarseilleProgressBarUi.class.getName(), MarseilleProgressBarUi.class);
    }
}
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ScriptDialog extends DialogWrapper {

    private final JBTextArea scriptArea;

    public ScriptDialog(Project project, String script) {
        super(project);
        scriptArea = new JBTextArea(script, 10, 80);
        scriptArea.setLineWrap(true);
        setTitle("Nastavit skript");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(new JBLabel("Základní příkaz generátoru (prázdné = výchozí):"), BorderLayout.NORTH);
        panel.add(new JBScrollPane(scriptArea), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return scriptArea;
    }

    public String getScript() {
        return scriptArea.getText();
    }
}

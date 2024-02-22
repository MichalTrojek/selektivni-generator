import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import utils.PluginBundle;
import utils.PluginVersionUtil;
import utils.SuggestionsUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

public class GeneratorForm {
    private JPanel mainPanel;
    private JTextArea resultPane;
    private JButton saveToClipboardBtn;
    private JButton startScriptBtn;
    private JTextField webClientPath;
    private JLabel versionLabel;
    private TextFieldWithSuggestions restInput;
    private TextFieldWithSuggestions modelInput;

    private String apiNames;
    private String modelNames;
    private String MODELS = "models";
    private String APIS = "apis";
    private PluginState state;
    private Project project;

    public GeneratorForm(PluginState state, Project project) {
        this.project = project;
        this.state = state.getState();
        registerWebClientPathListener();
        registerDocumentListener(restInput, APIS);
        registerDocumentListener(modelInput, MODELS);
        registerSaveToClipBoardBtnListener();
        registerStartScriptBtnListener();
        restoreFromState(state);
        setVersionLabel();
    }

    public void createUIComponents() {
        this.restInput = createTextFieldWidthSuggestions(project);
        this.modelInput = createTextFieldWidthSuggestions(project);
    }

    private TextFieldWithSuggestions createTextFieldWidthSuggestions(Project project) {
        TextFieldWithSuggestions field = new TextFieldWithSuggestions(project);
        field.addFocusListener((new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    field.setVariants(SuggestionsUtil.getSuggestions(project));
                });
            }
            @Override
            public void focusLost(FocusEvent e) {
            }
        }));
        return field;
    }

    private void registerWebClientPathListener() {
        webClientPath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onWebClientPathChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onWebClientPathChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onWebClientPathChanged();
            }
        });
    }


    private void onWebClientPathChanged() {
        String text = webClientPath.getText().trim();
        state.setWebClientPath(text);
        toggleStartScriptButton(text);
    }

    private void toggleStartScriptButton(String text) {
        boolean enabled = text.contains("okbase") && text.contains("web-client");
        this.startScriptBtn.setEnabled(enabled);
    }

    private void registerDocumentListener(TextFieldWithSuggestions field, String fieldName) {
        field.getDocument().addDocumentListener(new com.intellij.openapi.editor.event.DocumentListener() {
            @Override
            public void documentChanged(com.intellij.openapi.editor.event.@NotNull DocumentEvent event) {
                onTextChange(fieldName, field.getText());
            }
        });
    }

    private void onTextChange(String fieldName, String inputText) {
        String names = cleanText(inputText);
        if (fieldName.equals(APIS)) {
            this.apiNames = names;
            this.state.setApiNames(names);
        }
        if (fieldName.equals(MODELS)) {
            this.modelNames = names;
            this.state.setModelNames(names);
        }
        updateResult();
        this.state.setResult(resultPane.getText());
    }

    private String cleanText(String text) {
        return text.trim().replaceAll("\\s+", ":") // prazdna mista na :
                .replaceAll(",+", ":")
                .replaceAll("\\.+", ":")
                .replaceAll(":+", ":");
    }

    private void updateResult() {
        String script = PluginBundle.getString("script");
        StringBuilder sb = new StringBuilder(script).append(" ").append(addWrapper(APIS, apiNames)).append(" ").append(addWrapper(MODELS, modelNames));
        resultPane.setText(sb.toString());
    }

    private String addWrapper(String fieldName, String names) {
        if (names == null || names.isEmpty()) {
            return "";
        }
        return new StringBuilder("--global-property ").append(fieldName).append("=").append('"').append(names).append('"').toString().trim();
    }

    private void registerSaveToClipBoardBtnListener() {
        saveToClipboardBtn.addActionListener((ActionListener) -> copyToClipboard(this.resultPane.getText()));
    }

    private void copyToClipboard(String content) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void registerStartScriptBtnListener() {
        startScriptBtn.addActionListener((ActionListener) -> startScript());
    }

    private void startScript() {
        try {
            String command = resultPane.getText().trim();
            Runtime.getRuntime().exec("cmd.exe /c cd \"" + webClientPath.getText().trim() + "\" & start cmd.exe /k \"echo Spoustim generovani.. && " + command + "\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void restoreFromState(PluginState state) {
        this.restInput.setText(state.getApiNames().replaceAll(":", " ").trim());
        this.modelInput.setText(state.getModelNames().replaceAll(":", " ").trim());
        this.webClientPath.setText(state.getWebClientPath());
        this.updateResult();
    }

    private void setVersionLabel() {
        String version = PluginVersionUtil.getPluginVersion();
        if (version != null) {
            this.versionLabel.setText("v." + version);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}

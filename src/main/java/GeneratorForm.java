import utils.InputTextFilter;
import utils.PluginBundle;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class GeneratorForm {
    private JPanel mainPanel;
    private JTextField apisInput;
    private JTextField modelsInput;
    private JTextPane resultPane;
    private JButton saveToClipboardBtn;
    private JButton startScriptBtn;
    private JTextField webClientPath;

    private String apiNames;
    private String modelNames;
    private String MODELS = "models";
    private String APIS = "apis";
    private PluginState state;

    public GeneratorForm(PluginState state) {
        this.state = state.getState();
        registerWebClientPathListener();
        registerDocumentListener(apisInput, APIS);
        registerDocumentListener(modelsInput, MODELS);
        registerSaveToClipBoardBtnListener();
        registerStartScriptBtnListener();
        restoreFromState(state);
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

    private void registerDocumentListener(JTextField field, String fieldName) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new InputTextFilter());
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextChange(fieldName, field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextChange(fieldName, field.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
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
        updateResultAndSaveToState();
        this.state.setResult(resultPane.getText());
    }

    private String cleanText(String text) {
        return text.trim().replaceAll(" ", ":")
                .replaceAll(",", ":")
                .replaceAll("\\.", ":")
                .replace("::", ":");
    }

    private void updateResultAndSaveToState() {
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
        this.apisInput.setText(state.getApiNames().replaceAll(":", " ").trim());
        this.modelsInput.setText(state.getModelNames().replaceAll(":", " ").trim());
        this.webClientPath.setText(state.getWebClientPath());
        this.updateResultAndSaveToState();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}

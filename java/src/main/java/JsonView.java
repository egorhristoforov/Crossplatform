import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class JsonView {

    Container container;
    JsonViewController controller;

    JTextField urlField;
    JButton loadButton;

    JTextArea jsonTextArea;
    JButton saveButton;

    public JsonView(JsonViewController controller) {
        this.controller = controller;
    }

    public void attachTo(Container container) {
        this.container = container;

        setupViews(container);
        setupLoadButtonAction();
        setupSaveButtonAction();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(
                container,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void showJSON(String json) {
        jsonTextArea.setText(json);
        jsonTextArea.setVisible(true);

        saveButton.setVisible(true);
    }

    public void  showSuccess(String message) {
        JOptionPane.showMessageDialog(
                container,
                message
        );
    }

    private void setupViews(Container container) {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JLabel urlLabel = new JLabel("Json URL");
        urlLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
        container.add(urlLabel);

        urlField = new JTextField(20);
        urlField.setMinimumSize(new Dimension(200, 50));
        urlField.setMaximumSize(new Dimension(400, 50));
        urlField.setAlignmentX(Container.CENTER_ALIGNMENT);
        container.add(urlField);

        loadButton = new JButton("Load json");
        loadButton.setAlignmentX(Container.CENTER_ALIGNMENT);
        container.add(loadButton);

        jsonTextArea = new JTextArea();
        jsonTextArea.setVisible(false);
        jsonTextArea.setAlignmentX(Container.CENTER_ALIGNMENT);
        container.add(jsonTextArea);

        saveButton = new JButton("Save json");
        saveButton.setAlignmentX(Container.CENTER_ALIGNMENT);
        saveButton.setVisible(false);
        container.add(saveButton);
    }

    private void setupLoadButtonAction() {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.didClickLoadButton(urlField.getText());
            }
        };

        loadButton.addActionListener(listener);
    }

    private void setupSaveButtonAction() {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.isValidJSON(jsonTextArea.getText())) {
                    showFilePicker();
                } else {
                    showError("Invalid JSON");
                }
            }
        };

        saveButton.addActionListener(listener);
    }

    private void showFilePicker() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(container);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            controller.didClickSaveButton(jsonTextArea.getText(), fileToSave);
        }
    }
}
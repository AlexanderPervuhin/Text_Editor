import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

class ControlPanel extends JPanel {
    private JButton saveButton = new JButton();
    private JButton openButton = new JButton();
    private JButton startSearchButton = new JButton();
    private JButton previousMatchButton = new JButton();
    private JButton nextMatchButton = new JButton();
    JTextField searchField = new JTextField();
    JCheckBox regexCheckBox = new JCheckBox("Use regex");
    private int iconW = 30;
    private int iconH = 30;
    private int buttonH = 35;
    private int buttonW = 35;

    ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        searchField.setColumns(33);
        NameComponents();
        setButtonCommands();
        setButtonsStyle();
        addAllComponents();
    }

    private void NameComponents() {
        saveButton.setName("SaveButton");
        openButton.setName("OpenButton");
        searchField.setName("SearchField");
        startSearchButton.setName("StartSearchButton");
        previousMatchButton.setName("PreviousMatchButton");
        nextMatchButton.setName("NextMatchButton");
        regexCheckBox.setName("UseRegExCheckbox");
    }

    private void setButtonCommands() {
        saveButton.setActionCommand("SAVE");
        openButton.setActionCommand("OPEN");
        startSearchButton.setActionCommand("START SEARCH");
        previousMatchButton.setActionCommand("PREVIOUS MATCH");
        nextMatchButton.setActionCommand("NEXT MATCH");
    }

    private void setButtonsStyle() {
        try {
            setButtonDefaultStyle(saveButton, "Icons/Drives/Floppy.png");
            setButtonDefaultStyle(openButton, "Icons/Folders/Opened.png");
            setButtonDefaultStyle(startSearchButton, "Icons/Start Menu/Search.png");
            setButtonDefaultStyle(previousMatchButton, "Icons/Extras/Backward.png");
            setButtonDefaultStyle(nextMatchButton, "Icons/Extras/Forward.png");
            setButtonDefaultStyle(nextMatchButton, "Icons/Extras/Forward.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setButtonDefaultStyle(JButton button, String iconLocation) throws IOException {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(buttonW, buttonH));
        Image image = ImageIO.read(new File(iconLocation));
        button.setIcon(resizeIcon(image, iconW, iconH));
    }

    private static Icon resizeIcon(Image img, int resizedWidth, int resizedHeight) {
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void addAllComponents() {
        this.add(saveButton);
        this.add(openButton);
        this.add(searchField);
        this.add(startSearchButton);
        this.add(previousMatchButton);
        this.add(nextMatchButton);
        this.add(regexCheckBox);
    }

    void addSearchFieldListener(DocumentListener documentListener) {
        searchField.getDocument().addDocumentListener(documentListener);
    }

    void addActionListener(ActionListener actionListener) {
        startSearchButton.addActionListener(actionListener);
        previousMatchButton.addActionListener(actionListener);
        nextMatchButton.addActionListener(actionListener);
        saveButton.addActionListener(actionListener);
        openButton.addActionListener(actionListener);
    }

    void setMatchButtonsState(boolean state) {
        nextMatchButton.setEnabled(state);
        previousMatchButton.setEnabled(state);
    }

    String getTextToSearch() {
        return searchField.getText();
    }
}
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame implements DocumentListener, ActionListener {
    private ControlPanel controlPanel = new ControlPanel();
    private MainMenuBar mainMenuBar = new MainMenuBar();
    private JFileChooser fileSelectScreen = new JFileChooser();
    private JTextArea textArea = new JTextArea(20, 40);
    private JScrollPane scrollableTextArea = new JScrollPane(textArea);
    private BackgroundSearch backgroundSearch = new BackgroundSearch();
    private Matcher matcher;

    public TextEditor() {
        setTitle("Text Editor");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        NameComponents();
        setScrollBarPolicies();
        setMargins();
        addAllComponentsToFrame();
        backgroundSearch.execute();
        listenToInputs();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void NameComponents() {
        scrollableTextArea.setName("ScrollPane");
        fileSelectScreen.setName("FileChooser");
        textArea.setName("TextArea");
    }

    private void setMargins() {
        BorderFactory.setMargin(controlPanel, 5, 0, 0, 5);
        BorderFactory.setMargin(scrollableTextArea, 10, 10, 10, 10);
    }

    private void addAllComponentsToFrame() {
        add(controlPanel, BorderLayout.NORTH);
        add(fileSelectScreen);
        add(scrollableTextArea, BorderLayout.CENTER);
        setJMenuBar(mainMenuBar);
    }

    private void setScrollBarPolicies() {
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    private void listenToInputs() {
        mainMenuBar.addActionListener(this);
        fileSelectScreen.addActionListener(this);
        controlPanel.addActionListener(this);
        controlPanel.addSearchFieldListener(this);
        textArea.getDocument().addDocumentListener(this);
    }

    private boolean configureMatcher() {
        String searchQuery = controlPanel.getTextToSearch();
        if (searchQuery.isEmpty()) {
            showEmptySearchMsg();
            return false;
        } else {
            setMatchPattern(searchQuery);
            return true;
        }
    }

    private void setMatchPattern(String pattern) {
        Pattern searchPattern;
        if (!controlPanel.regexCheckBox.isSelected()) {
            pattern = Pattern.quote(pattern);
        }
        searchPattern = Pattern.compile(pattern);
        matcher = searchPattern.matcher(textArea.getText());
    }

    private void showEmptySearchMsg() {
        JOptionPane.showMessageDialog(null, "Search field is empty!", "", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        switch (command) {
            case "EXIT":
                dispose();
                break;
            case "OPEN":
                fileSelectScreen.showOpenDialog(null);
                break;
            case "SAVE":
                fileSelectScreen.showSaveDialog(null);
                break;
            case JFileChooser.APPROVE_SELECTION:
                SaveLoadSelectedFile();
                break;
            default:
                searchText(actionEvent);
                break;
        }
    }

    private void loadTextFromFile(File file) {
        textArea.setText("");
        try {
            Path path = Paths.get(file.getPath());
            String text = new String(Files.readAllBytes(path));
            textArea.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTextToFile(File file) {
        String text = textArea.getText();
        Path path = Paths.get(file.getPath());
        try {
            Files.write(path, text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveLoadSelectedFile() {
        File file = fileSelectScreen.getSelectedFile();
        switch (fileSelectScreen.getDialogType()) {
            case JFileChooser.OPEN_DIALOG:
                loadTextFromFile(file);
                break;
            case JFileChooser.SAVE_DIALOG:
                saveTextToFile(file);
                break;
        }
    }

    private void searchText(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        switch (command) {
            case "START SEARCH":
                startNewSearch();
                break;
            case "NEXT MATCH":
                findNextMatch();
                break;
            case "PREVIOUS MATCH":
                findPreviousMatch();
                break;
            case "USE REGEXP":
                selectUseRegex();
        }
    }

    private void startNewSearch() {
        if (configureMatcher()) {
            backgroundSearch.setSearchParameters(textArea, matcher);
            backgroundSearch.currentTask = SearchTask.START_SEARCH;
            setMatchControlState(true);
        }
    }

    private void setMatchControlState(boolean state) {
        controlPanel.setMatchButtonsState(state);
        mainMenuBar.setMatchMenuState(state);
    }

    private void findPreviousMatch() {
        backgroundSearch.currentTask = SearchTask.PREVIOUS_MATCH;
    }

    private void findNextMatch() {
        backgroundSearch.currentTask = SearchTask.NEXT_MATCH;
    }

    private void selectUseRegex() {
        controlPanel.regexCheckBox.setSelected(true);
    }


    // Disables Match buttons if TextArea and TextField are changed.
    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        setMatchControlState(false);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        setMatchControlState(false);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        setMatchControlState(false);
    }

}
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class MainMenuBar extends JMenuBar {
    private JMenuItem open = new JMenuItem("Open");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem exit = new JMenuItem("Exit");
    private JMenuItem startSearch = new JMenuItem("Start search");
    private JMenuItem useRegexp = new JMenuItem("Use regular expressions");
    private JMenu searchMenu = new JMenu("Search");
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem previousMatch = new JMenuItem("Previous match");
    private JMenuItem nextMatch = new JMenuItem("Next match");

    MainMenuBar() {
        fileMenu.setMnemonic(KeyEvent.VK_F);
        NameComponents();
        setMenuCommands();
        addAllComponents();
    }

    private void addAllComponents() {
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        add(fileMenu);
        searchMenu.add(startSearch);
        searchMenu.add(previousMatch);
        searchMenu.add(nextMatch);
        searchMenu.add(useRegexp);
        add(searchMenu);
    }

    private void NameComponents() {
        fileMenu.setName("MenuFile");
        open.setName("MenuOpen");
        save.setName("MenuSave");
        exit.setName("MenuExit");
        searchMenu.setName("MenuSearch");
        startSearch.setName("MenuStartSearch");
        previousMatch.setName("MenuPreviousMatch");
        nextMatch.setName("MenuNextMatch");
        useRegexp.setName("MenuUseRegExp");
    }

    private void setMenuCommands() {
        open.setActionCommand("OPEN");
        save.setActionCommand("SAVE");
        exit.setActionCommand("EXIT");
        startSearch.setActionCommand("START SEARCH");
        previousMatch.setActionCommand("PREVIOUS MATCH");
        nextMatch.setActionCommand("NEXT MATCH");
        useRegexp.setActionCommand("USE REGEXP");
    }

    void addActionListener(ActionListener actionListener) {
        save.addActionListener(actionListener);
        open.addActionListener(actionListener);
        exit.addActionListener(actionListener);
        startSearch.addActionListener(actionListener);
        previousMatch.addActionListener(actionListener);
        nextMatch.addActionListener(actionListener);
        useRegexp.addActionListener(actionListener);
    }

    void setMatchMenuState(boolean state) {
        nextMatch.setEnabled(state);
        previousMatch.setEnabled(state);
    }
}
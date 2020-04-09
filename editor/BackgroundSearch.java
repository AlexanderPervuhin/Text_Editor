import javax.swing.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

enum SearchTask {
    START_SEARCH, NEXT_MATCH, PREVIOUS_MATCH, STANDBY
}

class WordIndexes {
    int start;
    int end;

    public WordIndexes(int start, int end) {
        this.start = start;
        this.end = end;
    }
}

class BackgroundSearch extends SwingWorker {

    private ArrayList<WordIndexes> foundIndexes;
    private JTextArea textArea;
    private Matcher matcher;
    private ListIterator<WordIndexes> listIterator;
    private SearchTask previousTask;
    SearchTask currentTask = SearchTask.STANDBY;

    public void setSearchParameters(JTextArea textArea, Matcher matcher) {
        this.textArea = textArea;
        this.matcher = matcher;
    }

    @Override
    protected Object doInBackground() throws InterruptedException {
        foundIndexes = new ArrayList<>();
        listIterator = foundIndexes.listIterator();
        while (!isCancelled()) {
            doCurrentSearchAction();
            currentTask = SearchTask.STANDBY;
            int napTime = 30;
            Thread.sleep(napTime);
        }
        return null;
    }

    protected void done() {
    }

    private   void doCurrentSearchAction() {
        switch (currentTask) {
            case STANDBY:
                break;
            case START_SEARCH:
                foundIndexes = new ArrayList<>();
                listIterator = foundIndexes.listIterator();
                findAllMatches();
                previousTask = SearchTask.START_SEARCH;
                break;
            case NEXT_MATCH:
                goToNextMatch();
                previousTask = SearchTask.NEXT_MATCH;
                break;
            case PREVIOUS_MATCH:
                goToPreviousMatch();
                previousTask = SearchTask.PREVIOUS_MATCH;
        }
    }

    private void moveIteratorForward() {
        if (listIterator.hasNext()) listIterator.next();
    }

    private void moveIteratorBackward() {
        if (listIterator.hasPrevious()) listIterator.previous();
    }

    private void findAllMatches() {
        if (findNext()) {
            MatchResult result = matcher.toMatchResult();
            highlightText(result.start(), result.end());
            if (findNext()) {
                while (findNext()) ;
            }
            listIterator = foundIndexes.listIterator();
        } else {
            fireMatchLockerEvent();
            showNoMatchMsg();
        }
    }

    private void setListIteratorToLast() {
        WordIndexes wordIndexes = null;
        while (listIterator.hasNext()) {
            wordIndexes = listIterator.next();
        }
        if (wordIndexes != null) highlightText(wordIndexes.start, wordIndexes.end);
    }

    private void goToNextMatch() {
        if (previousTask != SearchTask.NEXT_MATCH) moveIteratorForward();
        if (!iterateToNextMatch()) iterateNextFromStart();
    }

    private void goToPreviousMatch() {
        if (previousTask != SearchTask.PREVIOUS_MATCH) moveIteratorBackward();
        if (listIterator.hasPrevious()) {
            WordIndexes wordIndexes = listIterator.previous();
            highlightText(wordIndexes.start, wordIndexes.end);
        } else setListIteratorToLast();
    }

    private boolean findNext() {
        if (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            listIterator.add(new WordIndexes(result.start(), result.end()));
            return true;
        } else return false;
    }

    private void iterateNextFromStart() {
        listIterator = foundIndexes.listIterator();
        iterateToNextMatch();
    }

    private boolean iterateToNextMatch() {
        if (listIterator.hasNext()) {
            WordIndexes wordIndexes = listIterator.next();
            highlightText(wordIndexes.start, wordIndexes.end);
            return true;
        } else return false;
    }

    private void highlightText(int start, int end) {
        textArea.setCaretPosition(end);
        textArea.select(start, end);
        textArea.grabFocus();
    }

    private void fireMatchLockerEvent() {
        String text = textArea.getText();
        textArea.setText(text);
    }

    private void showNoMatchMsg() {
        JOptionPane.showMessageDialog(null, "No matches found.", "", JOptionPane.PLAIN_MESSAGE);
    }

}
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class HighScoreWindow extends JFrame {
    // List of all leaderboard entries
    private final ArrayList<HighScoreEntry> highScoreEntries = new ArrayList<>();
    JPanel scoreArea = new JPanel();
    JScrollPane scrollPane = new JScrollPane(scoreArea);

    Font font = new Font("Segoe Script", Font.ITALIC, 20);
    Font fontHeading = new Font("Segoe Script", Font.BOLD, 20);

    // Custom comparator to sort by name
    public static Comparator<HighScoreEntry> nameComparator = Comparator.comparing(HighScoreEntry::getName);

    // Custom comparator to sort by Tries
    public static Comparator<HighScoreEntry> triesComparator = Comparator.comparingLong(HighScoreEntry::getTries);

    // Custom comparator to sort by seconds
    public static Comparator<HighScoreEntry> secondsComparator = Comparator.comparingLong(HighScoreEntry::getSecond);

    public HighScoreWindow() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(600, 400);
        JPanel topPanel = new JPanel();
        topPanel.setSize(500, 180);
        createMenus();
        JTextArea text = new JTextArea();
        text.append("Sort by:");
        text.setEditable(false);


        JButton sortByName = new JButton("Name");
        JButton sortByTries = new JButton("Tries");
        JButton sortBySeconds = new JButton("Seconds");

        sortByName.addActionListener(e -> {
            highScoreEntries.sort(nameComparator);
            displayScores();
        });
        sortByTries.addActionListener(e -> {
            highScoreEntries.sort(triesComparator);
            displayScores();
        });
        sortBySeconds.addActionListener(e -> {
            highScoreEntries.sort(secondsComparator);
            displayScores();
        });
        topPanel.add(text);
        topPanel.add(sortByName);
        topPanel.add(sortByTries);
        topPanel.add(sortBySeconds);

        scoreArea.setLayout(new BoxLayout(scoreArea, BoxLayout.Y_AXIS));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        getScores();
        displayScores();
    }


    // Create the menu bar
    public void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        JMenuItem close = new JMenuItem("Close Window");
        close.addActionListener((e) -> this.setVisible(false));
        menu.add(close);
        menuBar.add(menu);

    }

    // Retrieve the high scores from the cloud Database and display it
    public void getScores() {

        Database db = new Database();
        try {
            JSONObject obj = db.read();
            Set set = obj.keySet();
            for (Object key : set) {
                JSONObject json = (JSONObject) obj.get(key);
                String name = (String) key;
                String word = (String) json.get("Word");
                long tries = (long) json.get("Tries");
                long seconds = (long) json.get("Seconds");
                System.out.println(word + " " + tries + " " + seconds);
                HighScoreEntry entry = new HighScoreEntry(name, tries, seconds, word);
                highScoreEntries.add(entry);
                System.out.println(highScoreEntries);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Iterate through the high score entries and add it to the high score window
    public void displayScores() {
        scoreArea.removeAll();
        scoreArea.add(getListHeading());
        for (HighScoreEntry entry : highScoreEntries) {
            scoreArea.add(getListItem(entry));
        }
        this.repaint();
        this.setVisible(false);
        this.setVisible(true);
    }


    // Generates a single panel for each user with name, tries and seconds
    public JPanel getListItem(HighScoreEntry entry) {
        JPanel item = new JPanel();

        JTextArea name = new JTextArea(1, 12);
        name.append(entry.getName());
        name.setFont(font);
        item.add(name);

        JTextArea word = new JTextArea(1, 5);
        word.append(entry.getWord());
        word.setFont(font);
        item.add(word);

        JTextArea tries = new JTextArea(1, 4);
        tries.append(String.valueOf(entry.getTries()));
        tries.setFont(font);
        item.add(tries);

        JTextArea seconds = new JTextArea(1, 4);
        seconds.append(String.valueOf(entry.getSecond()));
        seconds.setFont(font);
        item.add(seconds);

        return item;
    }


    // Generates the column headings
    public JPanel getListHeading() {
        JPanel item = new JPanel();

        JTextArea name = new JTextArea(1, 12);
        name.append("Name");
        name.setFont(fontHeading);
        item.add(name);

        JTextArea word = new JTextArea(1, 5);
        word.append("Word");
        word.setFont(fontHeading);
        item.add(word);

        JTextArea tries = new JTextArea(1, 4);
        tries.append("Tries");
        tries.setFont(fontHeading);
        item.add(tries);

        JTextArea seconds = new JTextArea(1, 4);
        seconds.append("Seconds");
        seconds.setFont(fontHeading);
        item.add(seconds);

        return item;
    }


}

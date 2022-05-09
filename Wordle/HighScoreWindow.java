import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class HighScoreWindow extends JFrame {
    private ArrayList<HighScoreEntry> highScoreEntries = new ArrayList<>();
    JPanel scoreArea = new JPanel();

    JScrollPane scrollPane = new JScrollPane(scoreArea);

    Font font = new Font("Segoe Script", Font.ITALIC, 20);
    Font fontHeading = new Font("Segoe Script", Font.BOLD, 20);



    public HighScoreWindow() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(600, 400);
        JPanel topPanel = new JPanel();
        topPanel.setSize(500, 180);
        createMenus();

        JButton sortByName = new JButton("Name");
        JButton sortByTries = new JButton("Tries");
        JButton sortBySeconds = new JButton("Seconds");

        topPanel.add(sortByName);
        topPanel.add(sortByTries);
        topPanel.add(sortBySeconds);

        scoreArea.setLayout(new BoxLayout(scoreArea, BoxLayout.Y_AXIS));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        getScores();
        displayScores();
    }

    public void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        JMenuItem close = new JMenuItem("Close Window");
        close.addActionListener((e) -> this.setVisible(false));
        menu.add(close);
        menuBar.add(menu);

    }

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

    public void displayScores() {
        scoreArea.add(getListHeading());
        for (HighScoreEntry entry : highScoreEntries) {
            scoreArea.add(getListItem(entry));
        }
    }

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
        tries.append( String.valueOf(entry.getTries()));
        tries.setFont(font);
        item.add(tries);

        JTextArea seconds = new JTextArea(1, 4);
        seconds.append(String.valueOf(entry.getSecond()));
        seconds.setFont(font);
        item.add(seconds);

        return item;
    }

    public JPanel getListHeading(){
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
        tries.append( "Tries");
        tries.setFont(fontHeading);
        item.add(tries);

        JTextArea seconds = new JTextArea(1, 4);
        seconds.append("Seconds");
        seconds.setFont(fontHeading);
        item.add(seconds);

        return item;
    }


}

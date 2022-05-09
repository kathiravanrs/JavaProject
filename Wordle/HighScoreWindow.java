import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class HighScoreWindow extends JFrame {
    private final JTextField key;
    private ArrayList<HighScoreEntry> highScoreEntries = new ArrayList<>();

    public HighScoreWindow() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 600);
        JPanel topPanel = new JPanel();
        topPanel.setSize(500, 180);
        createMenus();
        JLabel textLabel = new JLabel("Enter your guess: ");
        key = new JTextField(10);
        key.addActionListener(e -> key.setText(""));
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> key.setText(""));

        topPanel.add(textLabel);
        topPanel.add(key);
        topPanel.add(clearButton);
        this.add(topPanel, BorderLayout.NORTH);

        getScores();
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

}

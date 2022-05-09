
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.util.ArrayList;

public class Wordle extends JFrame {
    private long startTime = 0;
    private int time = 0;
    private int tries = 0;
    private final ReadWordFile commonWords;
    private final ReadWordFile allWords;
    private final JTextField key;
    private boolean fileOpened = false;
    private final ArrayList<String> guesses = new ArrayList<>();
    private final CompoundBorder blackBorder = new CompoundBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));

    Font font = new Font("Segoe Script", Font.BOLD, 20);
    Color green = new Color(0, 255, 0);
    Color yellow = new Color(255, 255, 0);
    Color black = new Color(100, 100, 100);
    Color grey = new Color(50, 50, 50);
    ArrayList<Object> allWordsList;
    String randomWord = "raise";
    ArrayList<ArrayList<JTextArea>> wordsTextArea = new ArrayList<>();
    ArrayList<ArrayList<Color>> colors = new ArrayList<>();

    public Wordle() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 600);
        commonWords = new ReadWordFile();
        allWords = new ReadWordFile();
        JPanel topPanel = new JPanel();
        topPanel.setSize(500, 180);
        createMenus();
        pickRandomWord();
        loadWords();
        System.out.println(randomWord);
        JLabel textLabel = new JLabel("Enter your guess: ");
        key = new JTextField(10);
        key.addActionListener(e -> addWords());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> key.setText(""));

        topPanel.add(textLabel);
        topPanel.add(key);
        topPanel.add(clearButton);
        createGrid();
        displayGrid();
        this.add(topPanel, BorderLayout.NORTH);
    }

    public void addHighScoreList() {
        HighScoreWindow highScoreWindow = new HighScoreWindow();
        highScoreWindow.setVisible(true);

    }

    public void addWords() {
        String input = key.getText().strip().toUpperCase();
        if (input.length() != 5) {
            JOptionPane.showMessageDialog(null, "Please Enter a Five Letter Word");
            return;
        } else if (!allWordsList.contains(input.toLowerCase())) {
            JOptionPane.showMessageDialog(null, "Your word is not in my dictionary!");
            key.setText("");
            return;
        } else {
            guesses.set(tries, input);
            for (int i = 0; i < 5; i++) {
                if (input.charAt(i) == randomWord.toUpperCase().charAt(i)) {
                    colors.get(tries).set(i, green);
                } else if (randomWord.toUpperCase().contains(String.valueOf(input.charAt(i)))) {
                    colors.get(tries).set(i, yellow);
                } else colors.get(tries).set(i, black);
            }

            tries++;
            updateGrid();
            key.setText("");
            if (input.equals(randomWord.toUpperCase())) gameWon();
        }
        if (tries == 6) gameLost();
    }

    public void gameWon() {
        updateTime();
        int choice = JOptionPane.showOptionDialog(null,
                "Congrats! You guessed the word in " + tries + " tries!\nAnd you took " + time + " seconds!",
                "You Won",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Play Again", "Submit My Score"},
                "Play Again");
        if (choice == 1) submitScore();
        else reset();

    }

    public void gameLost() {
        int choice = JOptionPane.showOptionDialog(null,
                "Sorry! You didn't guess the word correctly.\n The word was " + randomWord.toUpperCase(),
                "You Lost",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Play Again", "Exit"},
                "Exit");
        if (choice == 0) reset();
        else System.exit(0);
    }

    public void submitScore() {
        String email = JOptionPane.showInputDialog("Enter Your Email Address");
        String name = JOptionPane.showInputDialog("Enter Your Name");

        Database db = new Database();
        try {
            db.post(name, tries, time, randomWord.toUpperCase());
            Email.send(email, "Wordle Score",
                    "Congrats! You guessed the word \"" + randomWord.toUpperCase() + "\" in " + tries + " tries!\nAnd you took " + time + " seconds!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> reset());
        exit.addActionListener((e) -> System.exit(0));
        menu.add(restart);
        menu.add(exit);
        menuBar.add(menu);

        JMenu highScores = new JMenu("HighScores");
        JMenuItem view = new JMenuItem("View High Scores");
        view.addActionListener(e -> addHighScoreList());
        highScores.add(view);
        menuBar.add(highScores);
    }

    public void pickRandomWord() {

        try {
            InputStream in = new FileInputStream("common_words");
            commonWords.load(in);
            fileOpened = true;
        } catch (IOException error) {
            error.printStackTrace();
        }
        if (!fileOpened) {
            System.out.println("File Not Chosen!");
            return;
        }
        randomWord = (String) commonWords.getRandomWord();
    }

    public void loadWords() {
        try {
            InputStream in = new FileInputStream("words");
            allWords.load(in);
            fileOpened = true;
        } catch (IOException error) {
            error.printStackTrace();
        }
        if (!fileOpened) {
            System.out.println("File Not Chosen!");
            return;
        }
        allWordsList = (ArrayList<Object>) allWords.getWords();
    }

    public void createGrid() {
        guesses.clear();
        for (int i = 0; i < 6; i++) {
            guesses.add("     ");
        }
        for (int i = 0; i < 6; i++) {
            ArrayList<JTextArea> a = new ArrayList<>();
            ArrayList<Color> c = new ArrayList<>();

            for (int j = 0; j < 5; j++) {
                JTextArea area = new JTextArea(1, 1);
                area.setEditable(false);
                area.setAlignmentX(Component.CENTER_ALIGNMENT);
                area.setBorder(blackBorder);
                a.add(area);
                c.add(grey);
            }
            wordsTextArea.add(a);
            colors.add(c);
        }
    }

    public void updateGrid() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                wordsTextArea.get(i).get(j).setText(String.valueOf(guesses.get(i).charAt(j)));
                wordsTextArea.get(i).get(j).setFont(font);
                wordsTextArea.get(i).get(j).setBackground(colors.get(i).get(j));

            }
        }
    }

    public void displayGrid() {
        updateGrid();

        Panel wordsPanel = new Panel();
        for (ArrayList<JTextArea> a : wordsTextArea) {
            Panel lettersPanel = new Panel();
            for (JTextArea j : a) {
                lettersPanel.add(j);
            }
            wordsPanel.add(lettersPanel);
        }
        this.add(wordsPanel);
        this.repaint();
        startTime = System.currentTimeMillis();
    }

    public void reset() {
        this.setVisible(false);
        Wordle wordFinder = new Wordle();
        wordFinder.setVisible(true);
    }

    public void updateTime() {
        time = (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    public static void main(String[] args) {
        Wordle wordFinder = new Wordle();
        wordFinder.setVisible(true);
    }
}

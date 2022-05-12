import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.util.ArrayList;

public class Wordle extends JFrame {

    private final ReadWordFile commonWords;
    private final ReadWordFile allWords;

    private final JTextField key;
    private boolean fileOpened = false;

    private String email = "";
    private String name = "";
    private long startTime = 0;
    private int time = 0;
    private int tries = 0;

    Font font = new Font("Segoe Script", Font.BOLD, 20);
    Color green = new Color(0, 255, 0);
    Color yellow = new Color(255, 255, 0);
    Color black = new Color(100, 100, 100);
    Color grey = new Color(50, 50, 50);

    private final CompoundBorder blackBorder = new CompoundBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));

    String randomWord = "raise";

    ArrayList<Object> allWordsList;
    private final ArrayList<String> guesses = new ArrayList<>();
    ArrayList<ArrayList<JTextArea>> wordsTextArea = new ArrayList<>();
    ArrayList<ArrayList<Color>> colors = new ArrayList<>();


    public Wordle() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 600);

        JPanel topPanel = new JPanel();
        topPanel.setSize(500, 180);
        createMenus();
        pickRandomWord();
        loadWords();
        System.out.println(randomWord);

        commonWords = new ReadWordFile(); //List of common 5 letter words
        allWords = new ReadWordFile(); //List of all 5 letter words

        JLabel textLabel = new JLabel("Enter your guess: ");
        key = new JTextField(10);
        key.addActionListener(e -> acceptInput());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> key.setText(""));

        topPanel.add(textLabel);
        topPanel.add(key);
        topPanel.add(clearButton);

        createGrid();
        displayGrid();
        this.add(topPanel, BorderLayout.NORTH);
    }

    // Creates the menu bar and adds the menu items with their listeners.
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


    // Creates an empty 5 x 6 grid
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

    // Updates the grid with the user guesses and appropriate background colors.
    public void updateGrid() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                wordsTextArea.get(i).get(j).setText(String.valueOf(guesses.get(i).charAt(j)));
                wordsTextArea.get(i).get(j).setFont(font);
                wordsTextArea.get(i).get(j).setBackground(colors.get(i).get(j));

            }
        }
    }

    // Displays the updated grid to the user after updating it
    // Stores the current time to be used in final calculation
    public void displayGrid() {
        updateGrid();

        Panel wordsPanel = new Panel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
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


    // Creates a new window to display the leaderboard
    // Can be opened from the menu bar

    public void addHighScoreList() {
        HighScoreWindow highScoreWindow = new HighScoreWindow();
        highScoreWindow.setVisible(true);

    }


    // Adds the user entered word to the guesses list and updates the UI
    // Also generates error when the criteria are not met
    public void acceptInput() {
        String input = key.getText().strip().toUpperCase();

        // Show the dialog if the entered word is not 5 letters
        if (input.length() != 5) {
            JOptionPane.showMessageDialog(null, "Please Enter a Five Letter Word");
            return;
        }
        // If the word is not a recognized word from the dictionary
        else if (!allWordsList.contains(input.toLowerCase())) {
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


    // Displays a Dialog box when the user wins the game
    // It shows options to play again or submit the score
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


    // Dialog showed when user loses
    // Two options to either exit or play again
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


    // A Threaded method to send an email
    public void sendEmail() {
        Thread t1 = new Thread(() -> Email.send(email, "Wordle Score",
                "Congrats! You guessed the word \"" + randomWord.toUpperCase() + "\" in " + tries + " tries!\nAnd you took " + time + " seconds!"));
        t1.start();
    }

    // A Threaded method to update the database
    public void databasePost() {
        Database db = new Database();

        Thread t1 = new Thread(() -> {
            try {
                db.post(name, tries, time, randomWord.toUpperCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();

    }


    // Dialogs to accept email address and name
    // Then calls the threaded methods of email and database
    public void submitScore() {
        email = JOptionPane.showInputDialog("Enter Your Email Address");
        name = JOptionPane.showInputDialog("Enter Your Name");
        sendEmail();
        databasePost();
    }


    // Loads the common words text file from the resources and stores a random word
    public void pickRandomWord() {

        try {
            InputStream in = getClass().getResourceAsStream("/common_words");

            commonWords.load(in);
            fileOpened = true;
        } catch (IOException error) {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null, "Common File Exception");

        }
        if (!fileOpened) {
            System.out.println("File Not Chosen!");
            JOptionPane.showMessageDialog(null, "Common Word File Not Opened");

            return;
        }
        randomWord = (String) commonWords.getRandomWord();
    }


    // Loads all 5 letter words from the word file and then stores it in an array
    public void loadWords() {
        try {
            InputStream in = getClass().getResourceAsStream("/words");
            allWords.load(in);
            fileOpened = true;
        } catch (IOException error) {
            JOptionPane.showMessageDialog(null, "Word File Exception");
            error.printStackTrace();
        }
        if (!fileOpened) {
            System.out.println("File Not Chosen!");
            JOptionPane.showMessageDialog(null, "Word File Not Opened");

            return;
        }
        allWordsList = (ArrayList<Object>) allWords.getWords();
    }


    // Hides the current game window and opens a new one
    public void reset() {
        this.setVisible(false);
        Wordle wordFinder = new Wordle();
        wordFinder.setVisible(true);
    }


    // Gets the current time and calculates the number of seconds since game started
    public void updateTime() {
        time = (int) ((System.currentTimeMillis() - startTime) / 1000);
    }

    public static void main(String[] args) {
        Wordle wordFinder = new Wordle();
        wordFinder.setVisible(true);
    }
}

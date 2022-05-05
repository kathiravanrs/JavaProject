import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class WordFinder extends JFrame {

    private int tries = 0;
    private JFileChooser jFileChooser;
    private JPanel topPanel;
    private WordList wordList;
    private JTextField key;
    private JTextArea wordsBox;
    private boolean fileOpened = false;
    private ArrayList<String> guesses = new ArrayList<>();
    private CompoundBorder blackBorder = new CompoundBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));

    Font font = new Font("Segoe Script", Font.BOLD, 20);

    private JPanel wordPanel;
    ArrayList<JTextArea> letters = new ArrayList<>();
    ArrayList<ArrayList<JTextArea>> words = new ArrayList<>();


    public WordFinder() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 800);
        jFileChooser = new JFileChooser(".");
        wordList = new WordList();
        topPanel = new JPanel();
        topPanel.setSize(500, 180);
        createMenus();
        JLabel textLabel = new JLabel("Find: ");

        key = new JTextField(10);
        key.addActionListener(e -> {
            guesses.set(tries, key.getText());
            tries++;
//            find();
        });


        for (int i = 0; i < 6; i++) {
            guesses.add("ABCDE");
        }
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            key.setText("");
        });

        topPanel.add(textLabel);
        topPanel.add(key);
        topPanel.add(clearButton);
        createGrid();
        displayGrid();
//        wordsBox = new JTextArea(10, 30);
//        wordsBox.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(wordsBox);
//        wordsBox.setText("Open A File to Search");
//
//        this.add(topPanel, BorderLayout.NORTH);
//        this.add(scrollPane);
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((e) -> System.exit(0));

        /* OpenActionListener that will open the file chooser */
        class OpenActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                OpenFileListener myFileListener = new OpenFileListener();
                myFileListener.actionPerformed(e);
            }
        }

        open.addActionListener(new OpenActionListener());
        menu.add(open);
        menu.add(exit);
        menuBar.add(menu);
    }

    private void find() {
        if (!fileOpened) {
            wordsBox.setText("You must open a file before entering a string to search");
            return;
        }

//        wordsBox.setText(null);
//        wordsBox.setBorder(blackBorder);
        String searchResult;
        searchResult = (String) wordList.getWords();
        wordsBox.append(searchResult + "\n");
        for (String s : guesses) {
            wordsBox.append(s + "\n");
        }
    }

    class OpenFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int returnVal = jFileChooser.showOpenDialog(getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    System.out.println("You chose to open this file: " + jFileChooser.getSelectedFile().getAbsolutePath());
                    InputStream in = new FileInputStream(jFileChooser.getSelectedFile().getAbsolutePath());
                    wordList.load(in);
                    fileOpened = true;

                    find();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        }
    }

    public void createGrid() {
        for (int i = 0; i < 6; i++) {
            ArrayList<JTextArea> a = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                JTextArea area = new JTextArea(2,2);
                area.setBorder(blackBorder);
                a.add(area);
            }
            words.add(a);
        }
    }

    public void updateGrid(){
        for(int i = 0; i<6;i++){
            for(int j=0;j<5;j++){
                words.get(i).get(j).setText(String.valueOf(guesses.get(i).charAt(j)));
                words.get(i).get(j).setFont(font);
            }
        }
    }

    public void displayGrid() {
        updateGrid();
        Panel wordsPanel = new Panel();
        for (ArrayList<JTextArea> a : words) {
            Panel lettersPanel = new Panel();
            for (JTextArea j : a) {
                lettersPanel.add(j);
            }
            wordsPanel.add(lettersPanel);
        }
        this.add(wordsPanel);
    }


    public static void main(String[] args) {
        WordFinder wordFinder = new WordFinder();
        wordFinder.setVisible(true);
    }
}

import java.io.*;
import java.net.URL;
import java.text.Collator;
import java.util.*;

public class WordList {
    private List words;

    public WordList() {

    }

    public Object getWords() {
        int r = (int) (Math.random() * words.size());
        return words.get(r);
    }

    /**
     * Load a file into this word set.
     * @effects Removes all the words from this word set and replaces
     * them with the words found in the given file.
     * @param in File to load
     * @throws IOException if file cannot be opened
     */

    public void load(InputStream in) throws IOException {
        Collator c = Collator.getInstance();
        c.setStrength(Collator.PRIMARY);
        Set s = new TreeSet(c);

        StreamTokenizer tok = new StreamTokenizer(new InputStreamReader(in));
        tok.resetSyntax();
        tok.wordChars('a', 'z');
        tok.wordChars('A', 'Z');
        tok.wordChars('\'', '\'');

        while (tok.nextToken() != StreamTokenizer.TT_EOF) {
            if (tok.ttype == StreamTokenizer.TT_WORD)
                s.add(tok.sval);
        }
        words = new ArrayList(s);
    }

    /**
     * @param args Command-line arguments.  Ignored.
     */
    public static void main(String[] args) {
        WordList words = new WordList ();


        URL url = WordList.class.getResource("words");
        if (url == null)
            throw new RuntimeException("Missing resource: words");
        try {
            words.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

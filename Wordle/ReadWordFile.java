import java.io.*;
import java.net.URL;
import java.text.Collator;
import java.util.*;



// This method directly used from one of the assignments

public class ReadWordFile {
    private List words;

    public ReadWordFile() {

    }


    // Returns a random word from the list
    public Object getRandomWord() {
        int r = (int) (Math.random() * words.size());
        return words.get(r);
    }

    public List<Object> getWords() {
        return words;
    }

    // Loads the file
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

}

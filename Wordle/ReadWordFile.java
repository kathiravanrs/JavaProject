import java.io.*;
import java.net.URL;
import java.text.Collator;
import java.util.*;

public class ReadWordFile {
    private List words;

    public ReadWordFile() {

    }

    public Object getRandomWord() {
        int r = (int) (Math.random() * words.size());
        return words.get(r);
    }

    public List<Object> getWords() {
        return words;
    }

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


public class HighScoreEntry {
    private final String name;
    private final long tries;
    private final long second;
    private final String word;

    public HighScoreEntry(String name, long tries, long second, String word) {
        this.name = name;
        this.word = word;
        this.tries = tries;
        this.second = second;
    }


    public String getName() {
        return name.toUpperCase();
    }

    public String getWord() {
        return word.toUpperCase();
    }

    public long getTries() {
        return tries;
    }

    public long getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return name + " " +
                word + " " +
                tries + " " +
                second;
    }
}

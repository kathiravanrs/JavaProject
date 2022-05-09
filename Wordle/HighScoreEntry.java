public class HighScoreEntry {
    private String name;
    private long tries;
    private long second;
    private String word;

    public HighScoreEntry(String name, long tries, long second, String word) {
        this.name = name;
        this.word = word;
        this.tries = tries;
        this.second = second;
    }


    public String getName() {
        return name;
    }

    public String getWord() {
        return word;
    }

    public long getTries() {
        return tries;
    }

    public long getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return name + " "+
                word + " "+
                tries + " "+
                second;
    }
}

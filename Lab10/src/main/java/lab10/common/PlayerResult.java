package lab10.common;

public class PlayerResult implements Comparable<PlayerResult> {
    private String name;
    private int score;
    private long totalResponseTimeMs;

    public PlayerResult(String name, int score, long totalResponseTimeMs) {
        this.name = name;
        this.score = score;
        this.totalResponseTimeMs = totalResponseTimeMs;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public long getTotalResponseTimeMs() { return totalResponseTimeMs; }

    public void incrementScore() { this.score++; }
    public void addResponseTime(long ms) { this.totalResponseTimeMs += ms; }

    @Override
    public int compareTo(PlayerResult other) {
        int scoreCompare = Integer.compare(other.score, this.score); // descending by score
        if (scoreCompare != 0) return scoreCompare;
        return Long.compare(this.totalResponseTimeMs, other.totalResponseTimeMs); // ascending by time
    }

    @Override
    public String toString() {
        return name + ": score=" + score + ", time=" + totalResponseTimeMs + "ms";
    }
}

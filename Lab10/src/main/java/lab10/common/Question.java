package lab10.common;

import java.util.List;

public class Question {
    private String text;
    private List<String> options;
    private int correctIndex;
    private String difficulty;

    public Question() {}

    public Question(String text, List<String> options, int correctIndex, String difficulty) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.difficulty = difficulty;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public int getCorrectIndex() { return correctIndex; }
    public void setCorrectIndex(int correctIndex) { this.correctIndex = correctIndex; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public boolean isCorrect(int answerIndex) {
        return answerIndex == correctIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(text).append("\n");
        for (int i = 0; i < options.size(); i++) {
            sb.append("  ").append(i).append(". ").append(options.get(i)).append("\n");
        }
        return sb.toString();
    }
}

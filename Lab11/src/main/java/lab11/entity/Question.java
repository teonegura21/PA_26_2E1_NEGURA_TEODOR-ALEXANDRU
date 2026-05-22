package lab11.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String correctAnswer;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options = new ArrayList<>();

    @ManyToMany(mappedBy = "questions", fetch = FetchType.LAZY)
    private List<Game> games = new ArrayList<>();

    public Question() {}

    public Question(String text, String correctAnswer, List<String> options) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public List<Game> getGames() { return games; }
    public void setGames(List<Game> games) { this.games = games; }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + text + "'}";
    }
}

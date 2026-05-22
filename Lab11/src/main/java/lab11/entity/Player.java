package lab11.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameResult> results = new ArrayList<>();

    public Player() {}

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<GameResult> getResults() { return results; }
    public void setResults(List<GameResult> results) { this.results = results; }

    public void addResult(GameResult result) {
        results.add(result);
        result.setPlayer(this);
    }

    public void removeResult(GameResult result) {
        results.remove(result);
        result.setPlayer(null);
    }

    @Override
    public String toString() {
        return "Player{id=" + id + ", username='" + username + "'}";
    }
}

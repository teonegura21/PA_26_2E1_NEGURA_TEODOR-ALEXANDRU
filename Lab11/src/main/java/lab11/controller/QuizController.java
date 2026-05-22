package lab11.controller;

import lab11.entity.*;
import lab11.repository.QuizService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Map<String, String> body) {
        Player p = quizService.createPlayer(body.get("username"), body.get("password"));
        return ResponseEntity.ok(p);
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers() {
        return ResponseEntity.ok(quizService.findAllPlayers());
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> options = (List<String>) body.get("options");
        Question q = quizService.createQuestion(
                (String) body.get("text"),
                (String) body.get("correctAnswer"),
                options
        );
        return ResponseEntity.ok(q);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getQuestions() {
        return ResponseEntity.ok(quizService.findAllQuestions());
    }

    @GetMapping("/questions/search")
    public ResponseEntity<List<Question>> searchQuestions(@RequestParam String keyword) {
        return ResponseEntity.ok(quizService.findQuestionsByKeyword(keyword));
    }

    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> qids = ((List<?>) body.get("questionIds")).stream().map(o -> Long.valueOf(o.toString())).toList();
        Game g = quizService.createGame((String) body.get("name"), qids);
        return ResponseEntity.ok(g);
    }

    @GetMapping("/games")
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok(quizService.findAllGames());
    }

    @PostMapping("/results")
    public ResponseEntity<GameResult> createResult(@RequestBody Map<String, Object> body) {
        GameResult gr = quizService.createGameResult(
                Long.valueOf(body.get("playerId").toString()),
                Long.valueOf(body.get("gameId").toString()),
                Integer.parseInt(body.get("score").toString()),
                Integer.parseInt(body.get("totalQuestions").toString())
        );
        return ResponseEntity.ok(gr);
    }

    @GetMapping("/results/player/{playerId}")
    public ResponseEntity<List<GameResult>> getResultsByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(quizService.findResultsByPlayer(playerId));
    }

    @GetMapping("/results/game/{gameId}")
    public ResponseEntity<List<GameResult>> getResultsByGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(quizService.findResultsByGame(gameId));
    }

    @PutMapping("/results/{id}/score")
    public ResponseEntity<String> updateScore(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        int updated = quizService.updateGameResultScore(id, body.get("score"));
        return ResponseEntity.ok("Updated " + updated + " row(s)");
    }

    @GetMapping("/results/search")
    public ResponseEntity<List<GameResult>> searchResults(
            @RequestParam(required = false) String playerUsername,
            @RequestParam(required = false) String gameName,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) String playedAfter) {
        LocalDateTime after = null;
        if (playedAfter != null && !playedAfter.isEmpty()) {
            after = LocalDateTime.parse(playedAfter);
        }
        return ResponseEntity.ok(quizService.searchResults(playerUsername, gameName, minScore, after));
    }

    @GetMapping("/audit")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(quizService.findAllAuditLogs());
    }
}

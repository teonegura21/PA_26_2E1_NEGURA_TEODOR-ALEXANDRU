package lab11;

import lab11.entity.*;
import lab11.repository.QuizService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class QuizJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(QuizService quizService) {
        return args -> {
            System.out.println("=== Lab 11: JPA Persistence for Quiz Game ===");

            // Create players
            Player p1 = quizService.createPlayer("alice", "secret");
            Player p2 = quizService.createPlayer("bob", "password");
            System.out.println("Created players: " + p1.getUsername() + ", " + p2.getUsername());

            // Create questions
            Question q1 = quizService.createQuestion(
                    "What is the capital of France?",
                    "Paris",
                    Arrays.asList("Paris", "London", "Berlin", "Madrid")
            );
            Question q2 = quizService.createQuestion(
                    "Which planet is known as the Red Planet?",
                    "Mars",
                    Arrays.asList("Venus", "Mars", "Jupiter", "Saturn")
            );
            Question q3 = quizService.createQuestion(
                    "What is 2 + 2?",
                    "4",
                    Arrays.asList("3", "4", "5", "6")
            );
            System.out.println("Created questions: " + q1.getId() + ", " + q2.getId() + ", " + q3.getId());

            // Create a game with questions (many-to-many)
            Game g1 = quizService.createGame("General Knowledge", Arrays.asList(q1.getId(), q2.getId(), q3.getId()));
            System.out.println("Created game: " + g1.getName());

            // Verify questions were associated using transactional method to avoid lazy loading issues
            Game g1Fresh = quizService.findGameWithQuestions(g1.getId());
            System.out.println("Game has " + g1Fresh.getQuestions().size() + " questions");

            // Create game results (one-to-many from Player and Game)
            GameResult r1 = quizService.createGameResult(p1.getId(), g1.getId(), 3, 3);
            GameResult r2 = quizService.createGameResult(p2.getId(), g1.getId(), 2, 3);
            System.out.println("Created results: id=" + r1.getId() + ", id=" + r2.getId());

            // JPQL read query: find questions by keyword
            List<Question> foundQuestions = quizService.findQuestionsByKeyword("capital");
            System.out.println("JPQL search 'capital' found: " + foundQuestions.size() + " question(s)");

            // JPQL read query: find results by player
            List<GameResult> aliceResults = quizService.findResultsByPlayer(p1.getId());
            System.out.println("Results for alice: " + aliceResults.size() + " result(s)");

            // Transactional modifying query: update score
            int updated = quizService.updateGameResultScore(r2.getId(), 3);
            System.out.println("Updated score rows: " + updated);

            // Dynamic search with Criteria API / Specification
            List<GameResult> searchResults = quizService.searchResults("alice", null, null, null);
            System.out.println("Criteria search for alice results: " + searchResults.size());

            // Audit logs
            List<AuditLog> logs = quizService.findAllAuditLogs();
            System.out.println("Audit log entries: " + logs.size());
            for (AuditLog log : logs) {
                System.out.println("  " + log);
            }

            System.out.println("=== Demo complete. REST API available at http://localhost:8080/api ===");
        };
    }
}

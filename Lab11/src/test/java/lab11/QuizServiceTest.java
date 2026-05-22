package lab11;

import lab11.entity.*;
import lab11.repository.QuizService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Test
    public void testCreatePlayer() {
        Player p = quizService.createPlayer("testuser", "pass");
        assertNotNull(p.getId());
        assertEquals("testuser", p.getUsername());
    }

    @Test
    public void testCreateQuestion() {
        Question q = quizService.createQuestion("What is Java?", "A language", Arrays.asList("A language", "A car", "A city"));
        assertNotNull(q.getId());
        assertEquals("What is Java?", q.getText());
    }

    @Test
    public void testCreateGameAndResults() {
        Question q1 = quizService.createQuestion("Q1", "A1", Arrays.asList("A1", "A2"));
        Question q2 = quizService.createQuestion("Q2", "B1", Arrays.asList("B1", "B2"));
        Game g = quizService.createGame("TestGame", Arrays.asList(q1.getId(), q2.getId()));
        assertNotNull(g.getId());
        assertEquals(2, g.getQuestions().size());

        Player p = quizService.createPlayer("gamer", "pass");
        GameResult gr = quizService.createGameResult(p.getId(), g.getId(), 1, 2);
        assertNotNull(gr.getId());
        assertEquals(1, gr.getScore());
    }

    @Test
    public void testFindPlayerById() {
        Player p = quizService.createPlayer("finder", "pass");
        Optional<Player> found = quizService.findPlayerById(p.getId());
        assertTrue(found.isPresent());
        assertEquals("finder", found.get().getUsername());
    }

    @Test
    public void testFindQuestionsByKeyword() {
        quizService.createQuestion("Capital of Italy", "Rome", Arrays.asList("Rome", "Milan"));
        quizService.createQuestion("Capital of Spain", "Madrid", Arrays.asList("Madrid", "Barcelona"));
        List<Question> results = quizService.findQuestionsByKeyword("Italy");
        assertEquals(1, results.size());
    }

    @Test
    public void testUpdateGameResultScore() {
        Question q = quizService.createQuestion("Q", "A", Arrays.asList("A", "B"));
        Game g = quizService.createGame("G", Arrays.asList(q.getId()));
        Player p = quizService.createPlayer("updater", "pass");
        GameResult gr = quizService.createGameResult(p.getId(), g.getId(), 0, 1);
        int updated = quizService.updateGameResultScore(gr.getId(), 1);
        assertEquals(1, updated);
    }

    @Test
    public void testSearchResultsWithCriteria() {
        Question q = quizService.createQuestion("Q", "A", Arrays.asList("A", "B"));
        Game g = quizService.createGame("SearchGame", Arrays.asList(q.getId()));
        Player p = quizService.createPlayer("searcher", "pass");
        quizService.createGameResult(p.getId(), g.getId(), 5, 5);
        List<GameResult> results = quizService.searchResults("searcher", "SearchGame", 3, null);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testAuditLogCreated() {
        int before = quizService.findAllAuditLogs().size();
        quizService.createPlayer("audited", "pass");
        int after = quizService.findAllAuditLogs().size();
        assertTrue(after > before);
    }
}

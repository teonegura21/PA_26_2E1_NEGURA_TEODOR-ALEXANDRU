package lab11.repository;

import lab11.audit.AuditService;
import lab11.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;
    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final AuditService auditService;

    public QuizService(PlayerRepository playerRepository,
                       QuestionRepository questionRepository,
                       GameRepository gameRepository,
                       GameResultRepository gameResultRepository,
                       AuditService auditService) {
        this.playerRepository = playerRepository;
        this.questionRepository = questionRepository;
        this.gameRepository = gameRepository;
        this.gameResultRepository = gameResultRepository;
        this.auditService = auditService;
    }

    // Player operations
    @Transactional
    public Player createPlayer(String username, String password) {
        Player player = new Player(username, password);
        Player saved = playerRepository.save(player);
        auditService.logChange("Player", "CREATE", "Created player: " + username);
        logger.info("Created player: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Player> findPlayerById(Long id) {
        long start = System.currentTimeMillis();
        try {
            Optional<Player> result = playerRepository.findById(id);
            long duration = System.currentTimeMillis() - start;
            logger.info("JPQL findPlayerById executed in {} ms", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("JPQL findPlayerById failed after {} ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    // Question operations
    @Transactional
    public Question createQuestion(String text, String correctAnswer, List<String> options) {
        Question question = new Question(text, correctAnswer, options);
        Question saved = questionRepository.save(question);
        auditService.logChange("Question", "CREATE", "Created question: " + text);
        logger.info("Created question: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Question> findQuestionsByKeyword(String keyword) {
        long start = System.currentTimeMillis();
        try {
            List<Question> result = questionRepository.findByTextContaining(keyword);
            long duration = System.currentTimeMillis() - start;
            logger.info("JPQL findQuestionsByKeyword executed in {} ms", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("JPQL findQuestionsByKeyword failed after {} ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    // Game operations
    @Transactional
    public Game createGame(String name, List<Long> questionIds) {
        Game game = new Game(name);
        for (Long qid : questionIds) {
            questionRepository.findById(qid).ifPresent(game::addQuestion);
        }
        Game saved = gameRepository.save(game);
        auditService.logChange("Game", "CREATE", "Created game: " + name + " with questions: " + questionIds);
        logger.info("Created game: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Game findGameWithQuestions(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        // Force initialization of the lazy collection inside the transaction
        game.getQuestions().size();
        return game;
    }

    // GameResult operations
    @Transactional
    public GameResult createGameResult(Long playerId, Long gameId, int score, int totalQuestions) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        GameResult result = new GameResult(player, game, score, totalQuestions);
        GameResult saved = gameResultRepository.save(result);
        auditService.logChange("GameResult", "CREATE", "Player " + player.getUsername()
                + " scored " + score + "/" + totalQuestions + " in game " + game.getName());
        logger.info("Created game result: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<GameResult> findResultsByPlayer(Long playerId) {
        long start = System.currentTimeMillis();
        try {
            List<GameResult> result = gameResultRepository.findByPlayerId(playerId);
            long duration = System.currentTimeMillis() - start;
            logger.info("JPQL findResultsByPlayer executed in {} ms", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("JPQL findResultsByPlayer failed after {} ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<GameResult> findResultsByGame(Long gameId) {
        long start = System.currentTimeMillis();
        try {
            List<GameResult> result = gameResultRepository.findByGameId(gameId);
            long duration = System.currentTimeMillis() - start;
            logger.info("JPQL findResultsByGame executed in {} ms", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("JPQL findResultsByGame failed after {} ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    // Transactional modifying query
    @Transactional
    public int updateGameResultScore(Long resultId, int newScore) {
        long start = System.currentTimeMillis();
        try {
            int updated = gameResultRepository.updateScoreById(resultId, newScore);
            long duration = System.currentTimeMillis() - start;
            logger.info("JPQL updateGameResultScore executed in {} ms, updated {} rows", duration, updated);
            auditService.logChange("GameResult", "UPDATE", "Updated result " + resultId + " score to " + newScore);
            return updated;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("JPQL updateGameResultScore failed after {} ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    // Dynamic search using Criteria API / Specification
    @Transactional(readOnly = true)
    public List<GameResult> searchResults(String playerUsername, String gameName, Integer minScore, LocalDateTime playedAfter) {
        long start = System.currentTimeMillis();
        try {
            Specification<GameResult> spec = Specification.where(GameResultSpecification.hasPlayerUsername(playerUsername))
                    .and(GameResultSpecification.hasGameName(gameName))
                    .and(GameResultSpecification.scoreGreaterThanOrEqual(minScore))
                    .and(GameResultSpecification.playedAfter(playedAfter));
            List<GameResult> result = gameResultRepository.findAll(spec);
            long duration = System.currentTimeMillis() - start;
            logger.info("Criteria API searchResults executed in {} ms, found {} rows", duration, result.size());
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("Criteria API searchResults failed after {} ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findAllAuditLogs() {
        return auditService.findAll();
    }
}

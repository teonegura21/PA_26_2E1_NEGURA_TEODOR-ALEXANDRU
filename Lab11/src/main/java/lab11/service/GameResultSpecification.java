package lab11.service;

import lab11.entity.GameResult;
import lab11.entity.Player;
import lab11.entity.Game;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class GameResultSpecification {

    public static Specification<GameResult> hasPlayerUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isEmpty()) {
                return cb.conjunction();
            }
            Join<GameResult, Player> player = root.join("player", JoinType.LEFT);
            return cb.like(cb.lower(player.get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<GameResult> hasGameName(String gameName) {
        return (root, query, cb) -> {
            if (gameName == null || gameName.isEmpty()) {
                return cb.conjunction();
            }
            Join<GameResult, Game> game = root.join("game", JoinType.LEFT);
            return cb.like(cb.lower(game.get("name")), "%" + gameName.toLowerCase() + "%");
        };
    }

    public static Specification<GameResult> scoreGreaterThanOrEqual(Integer minScore) {
        return (root, query, cb) -> {
            if (minScore == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("score"), minScore);
        };
    }

    public static Specification<GameResult> playedAfter(LocalDateTime after) {
        return (root, query, cb) -> {
            if (after == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("playedAt"), after);
        };
    }
}

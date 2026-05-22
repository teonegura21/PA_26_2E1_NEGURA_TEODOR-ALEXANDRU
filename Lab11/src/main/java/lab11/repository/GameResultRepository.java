package lab11.repository;

import lab11.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long>, JpaSpecificationExecutor<GameResult> {

    @Query("SELECT gr FROM GameResult gr WHERE gr.player.id = :playerId")
    List<GameResult> findByPlayerId(@Param("playerId") Long playerId);

    @Query("SELECT gr FROM GameResult gr WHERE gr.game.id = :gameId")
    List<GameResult> findByGameId(@Param("gameId") Long gameId);

    @Modifying
    @Transactional
    @Query("UPDATE GameResult gr SET gr.score = :newScore WHERE gr.id = :id")
    int updateScoreById(@Param("id") Long id, @Param("newScore") int newScore);
}

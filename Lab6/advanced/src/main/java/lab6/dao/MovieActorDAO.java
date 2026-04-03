package lab6.dao;

import lab6.model.MovieActor;
import lab6.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieActorDAO {

    public void create(MovieActor movieActor) throws SQLException {
        String sql = "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieActor.getMovieId());
            stmt.setInt(2, movieActor.getActorId());
            stmt.executeUpdate();
        }
    }

    public List<MovieActor> findByMovie(int movieId) throws SQLException {
        String sql = "SELECT * FROM movie_actors WHERE movie_id = ?";
        List<MovieActor> list = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        }
        return list;
    }

    public List<MovieActor> findByActor(int actorId) throws SQLException {
        String sql = "SELECT * FROM movie_actors WHERE actor_id = ?";
        List<MovieActor> list = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        }
        return list;
    }

    private MovieActor mapResultSet(ResultSet rs) throws SQLException {
        MovieActor ma = new MovieActor();
        ma.setMovieId(rs.getInt("movie_id"));
        ma.setActorId(rs.getInt("actor_id"));
        return ma;
    }
}

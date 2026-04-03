package lab6.dao;

import lab6.model.Actor;
import lab6.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    public void create(Actor actor) throws SQLException {
        String sql = "INSERT INTO actors (name, birth_date) VALUES (?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getName());
            stmt.setDate(2, actor.getBirthDate());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    actor.setId(rs.getInt(1));
                }
            }
        }
    }

    public Actor findById(int id) throws SQLException {
        String sql = "SELECT * FROM actors WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActor(rs);
                }
            }
        }
        return null;
    }

    public List<Actor> findAll() throws SQLException {
        String sql = "SELECT * FROM actors";
        List<Actor> actors = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                actors.add(mapResultSetToActor(rs));
            }
        }
        return actors;
    }

    public List<Actor> findByMovie(int movieId) throws SQLException {
        String sql = "SELECT a.* FROM actors a JOIN movie_actors ma ON a.id = ma.actor_id WHERE ma.movie_id = ?";
        List<Actor> actors = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(mapResultSetToActor(rs));
                }
            }
        }
        return actors;
    }

    public void update(Actor actor) throws SQLException {
        String sql = "UPDATE actors SET name = ?, birth_date = ? WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actor.getName());
            stmt.setDate(2, actor.getBirthDate());
            stmt.setInt(3, actor.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM actors WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Actor mapResultSetToActor(ResultSet rs) throws SQLException {
        Actor actor = new Actor();
        actor.setId(rs.getInt("id"));
        actor.setName(rs.getString("name"));
        actor.setBirthDate(rs.getDate("birth_date"));
        return actor;
    }
}

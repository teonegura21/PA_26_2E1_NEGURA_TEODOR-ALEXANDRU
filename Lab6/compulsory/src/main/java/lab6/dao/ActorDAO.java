package lab6.dao;

import lab6.model.Actor;
import lab6.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    public void create(Actor actor) throws SQLException {
        String sql = "INSERT INTO actors (name, birth_date) VALUES (?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, actor.getName());
        stmt.setDate(2, actor.getBirthDate());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            actor.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();
    }

    public Actor findById(int id) throws SQLException {
        String sql = "SELECT * FROM actors WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Actor actor = null;
        if (rs.next()) {
            actor = mapResultSetToActor(rs);
        }
        rs.close();
        stmt.close();
        return actor;
    }

    public List<Actor> findAll() throws SQLException {
        String sql = "SELECT * FROM actors";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Actor> actors = new ArrayList<>();
        while (rs.next()) {
            actors.add(mapResultSetToActor(rs));
        }
        rs.close();
        stmt.close();
        return actors;
    }

    public void update(Actor actor) throws SQLException {
        String sql = "UPDATE actors SET name = ?, birth_date = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, actor.getName());
        stmt.setDate(2, actor.getBirthDate());
        stmt.setInt(3, actor.getId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM actors WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    private Actor mapResultSetToActor(ResultSet rs) throws SQLException {
        Actor actor = new Actor();
        actor.setId(rs.getInt("id"));
        actor.setName(rs.getString("name"));
        actor.setBirthDate(rs.getDate("birth_date"));
        return actor;
    }
}

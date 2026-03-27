package lab6.dao;

import lab6.model.Genre;
import lab6.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    public void create(Genre genre) throws SQLException {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, genre.getName());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            genre.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();
    }

    public Genre findById(int id) throws SQLException {
        String sql = "SELECT * FROM genres WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Genre genre = null;
        if (rs.next()) {
            genre = mapResultSetToGenre(rs);
        }
        rs.close();
        stmt.close();
        return genre;
    }

    public Genre findByName(String name) throws SQLException {
        String sql = "SELECT * FROM genres WHERE name = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        Genre genre = null;
        if (rs.next()) {
            genre = mapResultSetToGenre(rs);
        }
        rs.close();
        stmt.close();
        return genre;
    }

    public List<Genre> findAll() throws SQLException {
        String sql = "SELECT * FROM genres";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            genres.add(mapResultSetToGenre(rs));
        }
        rs.close();
        stmt.close();
        return genres;
    }

    public void update(Genre genre) throws SQLException {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, genre.getName());
        stmt.setInt(2, genre.getId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM genres WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    private Genre mapResultSetToGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}

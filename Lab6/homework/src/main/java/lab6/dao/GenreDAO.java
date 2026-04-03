package lab6.dao;

import lab6.model.Genre;
import lab6.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    public void create(Genre genre) throws SQLException {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, genre.getName());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    genre.setId(rs.getInt(1));
                }
            }
        }
    }

    public Genre findById(int id) throws SQLException {
        String sql = "SELECT * FROM genres WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGenre(rs);
                }
            }
        }
        return null;
    }

    public Genre findByName(String name) throws SQLException {
        String sql = "SELECT * FROM genres WHERE name = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGenre(rs);
                }
            }
        }
        return null;
    }

    public List<Genre> findAll() throws SQLException {
        String sql = "SELECT * FROM genres";
        List<Genre> genres = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                genres.add(mapResultSetToGenre(rs));
            }
        }
        return genres;
    }

    public void update(Genre genre) throws SQLException {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genre.getName());
            stmt.setInt(2, genre.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM genres WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Genre mapResultSetToGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}

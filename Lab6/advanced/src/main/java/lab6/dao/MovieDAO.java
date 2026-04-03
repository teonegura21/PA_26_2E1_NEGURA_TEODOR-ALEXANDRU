package lab6.dao;

import lab6.model.Movie;
import lab6.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public void create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movie.getTitle());
            stmt.setDate(2, movie.getReleaseDate());
            stmt.setInt(3, movie.getDuration());
            stmt.setDouble(4, movie.getScore());
            stmt.setInt(5, movie.getGenreId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movie.setId(rs.getInt(1));
                }
            }
        }
    }

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovie(rs);
                }
            }
        }
        return null;
    }

    public List<Movie> findAll() throws SQLException {
        String sql = "SELECT * FROM movies";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                movies.add(mapResultSetToMovie(rs));
            }
        }
        return movies;
    }

    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setReleaseDate(rs.getDate("release_date"));
        movie.setDuration(rs.getInt("duration"));
        movie.setScore(rs.getDouble("score"));
        movie.setGenreId(rs.getInt("genre_id"));
        return movie;
    }
}

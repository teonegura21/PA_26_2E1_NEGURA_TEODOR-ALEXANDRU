package lab6.dao;

import lab6.model.Movie;
import lab6.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public void create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, movie.getTitle());
        stmt.setDate(2, movie.getReleaseDate());
        stmt.setInt(3, movie.getDuration());
        stmt.setDouble(4, movie.getScore());
        stmt.setInt(5, movie.getGenreId());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            movie.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();
    }

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Movie movie = null;
        if (rs.next()) {
            movie = mapResultSetToMovie(rs);
        }
        rs.close();
        stmt.close();
        return movie;
    }

    public List<Movie> findAll() throws SQLException {
        String sql = "SELECT * FROM movies";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Movie> movies = new ArrayList<>();
        while (rs.next()) {
            movies.add(mapResultSetToMovie(rs));
        }
        rs.close();
        stmt.close();
        return movies;
    }

    public void update(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET title = ?, release_date = ?, duration = ?, score = ?, genre_id = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, movie.getTitle());
        stmt.setDate(2, movie.getReleaseDate());
        stmt.setInt(3, movie.getDuration());
        stmt.setDouble(4, movie.getScore());
        stmt.setInt(5, movie.getGenreId());
        stmt.setInt(6, movie.getId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
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

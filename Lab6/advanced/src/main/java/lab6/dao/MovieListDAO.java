package lab6.dao;

import lab6.model.MovieList;
import lab6.model.Movie;
import lab6.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieListDAO {

    public void create(MovieList movieList) throws SQLException {
        String sql = "INSERT INTO movie_lists (name) VALUES (?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movieList.getName());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movieList.setId(rs.getInt(1));
                }
            }
        }
        for (Movie movie : movieList.getMovies()) {
            addMovieToList(movieList.getId(), movie.getId());
        }
    }

    public MovieList findById(int id) throws SQLException {
        String sql = "SELECT * FROM movie_lists WHERE id = ?";
        MovieList list = null;
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    list = mapResultSet(rs);
                }
            }
        }
        if (list != null) {
            list.setMovies(findMoviesInList(id));
        }
        return list;
    }

    public List<MovieList> findAll() throws SQLException {
        String sql = "SELECT * FROM movie_lists";
        List<MovieList> lists = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MovieList list = mapResultSet(rs);
                list.setMovies(findMoviesInList(list.getId()));
                lists.add(list);
            }
        }
        return lists;
    }

    public void addMovieToList(int listId, int movieId) throws SQLException {
        String sql = "INSERT INTO list_movies (list_id, movie_id) VALUES (?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, listId);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM movie_lists WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private List<Movie> findMoviesInList(int listId) throws SQLException {
        String sql = "SELECT m.* FROM movies m JOIN list_movies lm ON m.id = lm.movie_id WHERE lm.list_id = ?";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, listId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapMovie(rs));
                }
            }
        }
        return movies;
    }

    private MovieList mapResultSet(ResultSet rs) throws SQLException {
        MovieList list = new MovieList();
        list.setId(rs.getInt("id"));
        list.setName(rs.getString("name"));
        list.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
        return list;
    }

    private Movie mapMovie(ResultSet rs) throws SQLException {
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

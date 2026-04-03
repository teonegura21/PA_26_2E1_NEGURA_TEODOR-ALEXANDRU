package lab6.graph;

import lab6.model.Movie;
import lab6.dao.MovieDAO;
import lab6.dao.MovieActorDAO;
import lab6.dao.ActorDAO;
import java.sql.SQLException;
import java.util.*;

public class MovieGraph {
    private Map<Movie, Set<Movie>> adjacencyList;

    public MovieGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void buildFromDatabase() throws SQLException {
        MovieDAO movieDAO = new MovieDAO();
        MovieActorDAO movieActorDAO = new MovieActorDAO();
        ActorDAO actorDAO = new ActorDAO();

        List<Movie> movies = movieDAO.findAll();
        Map<Integer, Movie> movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
            adjacencyList.put(movie, new HashSet<>());
        }

        for (Movie movie : movies) {
            var movieActors = movieActorDAO.findByMovie(movie.getId());
            for (var ma : movieActors) {
                var actorMovies = movieActorDAO.findByActor(ma.getActorId());
                for (var otherMa : actorMovies) {
                    if (otherMa.getMovieId() != movie.getId()) {
                        Movie otherMovie = movieMap.get(otherMa.getMovieId());
                        if (otherMovie != null) {
                            addEdge(movie, otherMovie);
                        }
                    }
                }
            }
        }
    }

    public void addEdge(Movie m1, Movie m2) {
        adjacencyList.get(m1).add(m2);
        adjacencyList.get(m2).add(m1);
    }

    public Set<Movie> getNeighbors(Movie movie) {
        return adjacencyList.getOrDefault(movie, new HashSet<>());
    }

    public Set<Movie> getAllMovies() {
        return adjacencyList.keySet();
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        int count = 0;
        for (Set<Movie> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        return count / 2;
    }

    public int getDegree(Movie movie) {
        return adjacencyList.getOrDefault(movie, new HashSet<>()).size();
    }
}

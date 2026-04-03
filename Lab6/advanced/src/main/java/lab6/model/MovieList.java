package lab6.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private int id;
    private String name;
    private Timestamp creationTimestamp;
    private List<Movie> movies;

    public MovieList() {
        this.movies = new ArrayList<>();
    }

    public MovieList(String name) {
        this.name = name;
        this.movies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    @Override
    public String toString() {
        return "MovieList{id=" + id + ", name='" + name + "', movies=" + movies.size() + "}";
    }
}

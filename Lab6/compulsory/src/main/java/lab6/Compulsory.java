package lab6;

import lab6.dao.GenreDAO;
import lab6.dao.MovieDAO;
import lab6.dao.ActorDAO;
import lab6.model.Genre;
import lab6.model.Movie;
import lab6.model.Actor;
import lab6.util.DatabaseConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Compulsory {

    public static void main(String[] args) {
        System.out.println("=== Lab 6 Compulsory - JDBC Movie Database ===\n");

        GenreDAO genreDAO = new GenreDAO();
        MovieDAO movieDAO = new MovieDAO();
        ActorDAO actorDAO = new ActorDAO();

        try {
            System.out.println("--- Testing GenreDAO ---");

            System.out.println("All genres:");
            List<Genre> genres = genreDAO.findAll();
            for (Genre g : genres) {
                System.out.println("  " + g);
            }

            System.out.println("\nFind genre by ID (1):");
            Genre genreById = genreDAO.findById(1);
            System.out.println("  " + genreById);

            System.out.println("\nFind genre by name ('Drama'):");
            Genre genreByName = genreDAO.findByName("Drama");
            System.out.println("  " + genreByName);

            System.out.println("\nCreate new genre:'Fantasy'");
            Genre newGenre = new Genre("Fantasy");
            genreDAO.create(newGenre);
            System.out.println("  Created: " + newGenre);

            System.out.println("\nAll genres after creation:");
            genres = genreDAO.findAll();
            for (Genre g : genres) {
                System.out.println("  " + g);
            }

            System.out.println("\n--- Testing MovieDAO ---");

            Movie movie1 = new Movie("Inception", Date.valueOf("2010-07-16"), 148, 8.8, 5);
            movieDAO.create(movie1);
            System.out.println("Created: " + movie1);

            Movie movie2 = new Movie("The Dark Knight", Date.valueOf("2008-07-18"), 152, 9.0, 1);
            movieDAO.create(movie2);
            System.out.println("Created: " + movie2);

            System.out.println("\nAll movies:");
            List<Movie> movies = movieDAO.findAll();
            for (Movie m : movies) {
                System.out.println("  " + m);
            }

            System.out.println("\nFind movie by ID (1):");
            Movie movieById = movieDAO.findById(1);
            System.out.println("  " + movieById);

            System.out.println("\n--- Testing ActorDAO ---");

            Actor actor1 = new Actor("Leonardo DiCaprio", Date.valueOf("1974-11-11"));
            actorDAO.create(actor1);
            System.out.println("Created: " + actor1);

            Actor actor2 = new Actor("Christian Bale", Date.valueOf("1974-01-30"));
            actorDAO.create(actor2);
            System.out.println("Created: " + actor2);

            System.out.println("\nAll actors:");
            List<Actor> actors = actorDAO.findAll();
            for (Actor a : actors) {
                System.out.println("  " + a);
            }

            System.out.println("\n--- Testing Updates ---");

            if (genreById != null) {
                genreById.setName("Action Movies");
                genreDAO.update(genreById);
                System.out.println("Updated genre: " + genreDAO.findById(1));
            }

            System.out.println("\n=== All Tests Completed Successfully ===");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.getInstance().close();
        }
    }
}

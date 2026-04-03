package lab6;

import lab6.dao.GenreDAO;
import lab6.dao.MovieDAO;
import lab6.dao.ActorDAO;
import lab6.dao.MovieActorDAO;
import lab6.model.Genre;
import lab6.model.Movie;
import lab6.model.Actor;
import lab6.model.MovieActor;
import lab6.util.ConnectionPool;
import lab6.util.ReportGenerator;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

public class Homework {

    public static void main(String[] args) {
        System.out.println("=== Lab 6 Homework - JDBC with Connection Pool ===\n");

        initializeDatabase();

        GenreDAO genreDAO = new GenreDAO();
        MovieDAO movieDAO = new MovieDAO();
        ActorDAO actorDAO = new ActorDAO();
        MovieActorDAO movieActorDAO = new MovieActorDAO();
        ReportGenerator reportGenerator = new ReportGenerator();

        try {
            System.out.println("--- Connection Pool Statistics ---");
            System.out.println("Active connections: " + ConnectionPool.getInstance().getDataSource().getHikariPoolMXBean().getActiveConnections());
            System.out.println("Idle connections: " + ConnectionPool.getInstance().getDataSource().getHikariPoolMXBean().getIdleConnections());
            System.out.println("Total connections: " + ConnectionPool.getInstance().getDataSource().getHikariPoolMXBean().getTotalConnections());

            System.out.println("\n--- Testing GenreDAO ---");
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

            System.out.println("\nCreate new genre: 'Fantasy'");
            Genre newGenre = new Genre("Fantasy");
            genreDAO.create(newGenre);
            System.out.println("  Created: " + newGenre);

            System.out.println("\n--- Testing MovieDAO ---");
            System.out.println("All movies:");
            List<Movie> movies = movieDAO.findAll();
            for (Movie m : movies) {
                System.out.println("  " + m);
            }

            System.out.println("\nFind movie by ID (1):");
            Movie movieById = movieDAO.findById(1);
            System.out.println("  " + movieById);

            System.out.println("\nFind movies by genre (Sci-Fi, id=5):");
            List<Movie> sciFiMovies = movieDAO.findByGenre(5);
            for (Movie m : sciFiMovies) {
                System.out.println("  " + m);
            }

            System.out.println("\nCreate new movie:");
            Movie newMovie = new Movie("Interstellar", Date.valueOf("2014-11-07"), 169, 8.6, 5);
            movieDAO.create(newMovie);
            System.out.println("  Created: " + newMovie);

            System.out.println("\n--- Testing ActorDAO ---");
            System.out.println("All actors:");
            List<Actor> actors = actorDAO.findAll();
            for (Actor a : actors) {
                System.out.println("  " + a);
            }

            System.out.println("\nFind actors by movie (Inception, id=1):");
            List<Actor> inceptionActors = actorDAO.findByMovie(1);
            for (Actor a : inceptionActors) {
                System.out.println("  " + a);
            }

            System.out.println("\n--- Testing MovieActorDAO ---");
            System.out.println("Find movie-actor relationships for movie 1:");
            List<MovieActor> maList = movieActorDAO.findActorsByMovie(1);
            for (MovieActor ma : maList) {
                System.out.println("  " + ma);
            }

            System.out.println("\n--- Generating HTML Report ---");
            reportGenerator.generateHtmlReport("movies_report.html");
            System.out.println("Report generated: movies_report.html");

            System.out.println("\n--- Connection Pool Statistics After Operations ---");
            System.out.println("Active connections: " + ConnectionPool.getInstance().getDataSource().getHikariPoolMXBean().getActiveConnections());
            System.out.println("Idle connections: " + ConnectionPool.getInstance().getDataSource().getHikariPoolMXBean().getIdleConnections());

            System.out.println("\n=== All Tests Completed Successfully ===");

        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().close();
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            InputStream is = Homework.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sql.append(line).append("\n");
                }
                reader.close();

                for (String command : sql.toString().split(";")) {
                    String trimmed = command.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}

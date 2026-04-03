package lab6;

import lab6.dao.*;
import lab6.model.*;
import lab6.util.ConnectionPool;
import lab6.util.FlywayMigration;
import lab6.graph.MovieGraph;
import lab6.graph.GraphPartitioner;
import java.sql.SQLException;
import java.util.List;

public class Advanced {

    public static void main(String[] args) {
        System.out.println("=== Lab 6 Advanced - JDBC with Flyway, CSV Import, Graph Partitioning ===\n");

        FlywayMigration.migrate(ConnectionPool.getInstance().getDataSource());
        System.out.println("Database migrations completed.\n");

        GenreDAO genreDAO = new GenreDAO();
        MovieDAO movieDAO = new MovieDAO();
        ActorDAO actorDAO = new ActorDAO();
        MovieListDAO movieListDAO = new MovieListDAO();

        try {
            System.out.println("--- Database Contents ---");
            System.out.println("Genres:");
            List<Genre> genres = genreDAO.findAll();
            for (Genre g : genres) {
                System.out.println("  " + g);
            }

            System.out.println("\nMovies:");
            List<Movie> movies = movieDAO.findAll();
            for (Movie m : movies) {
                System.out.println("  " + m);
            }

            System.out.println("\nActors:");
            List<Actor> actors = actorDAO.findAll();
            for (Actor a : actors) {
                System.out.println("  " + a);
            }

            System.out.println("\n--- Building Movie Graph ---");
            MovieGraph graph = new MovieGraph();
            graph.buildFromDatabase();
            System.out.println("Vertices (movies): " + graph.getVertexCount());
            System.out.println("Edges (relationships): " + graph.getEdgeCount());

            System.out.println("\n--- Graph Partitioning ---");
            GraphPartitioner partitioner = new GraphPartitioner();
            List<MovieList> partitions = partitioner.partitionMovies(graph);

            System.out.println("Number of partitions: " + partitions.size());
            for (MovieList list : partitions) {
                System.out.println("  " + list.getName() + ": " + list.getMovies().size() + " movies");
                for (Movie m : list.getMovies()) {
                    System.out.println("    - " + m.getTitle());
                }
            }

            System.out.println("\n--- Validation ---");
            boolean isValid = partitioner.validatePartition(partitions, graph);
            boolean isBalanced = partitioner.isBalanced(partitions);
            System.out.println("Partition valid (no related movies in same list): " + isValid);
            System.out.println("Partition balanced (size diff <= 1): " + isBalanced);

            System.out.println("\n--- Saving Partitions to Database ---");
            for (MovieList list : partitions) {
                movieListDAO.create(list);
                System.out.println("Saved: " + list.getName() + " with ID " + list.getId());
            }

            System.out.println("\n--- Retrieving Lists from Database ---");
            List<MovieList> savedLists = movieListDAO.findAll();
            for (MovieList list : savedLists) {
                System.out.println(list);
            }

            System.out.println("\n=== All Tests Completed Successfully ===");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().close();
        }
    }
}

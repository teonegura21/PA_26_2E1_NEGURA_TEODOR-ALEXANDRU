package lab6.graph;

import lab6.model.Movie;
import lab6.model.MovieList;
import java.util.*;

public class GraphPartitioner {

    public List<MovieList> partitionMovies(MovieGraph graph) {
        List<Movie> sortedMovies = new ArrayList<>(graph.getAllMovies());
        sortedMovies.sort((m1, m2) -> graph.getDegree(m2) - graph.getDegree(m1));

        List<MovieList> lists = new ArrayList<>();
        Map<Movie, Integer> movieToList = new HashMap<>();

        for (Movie movie : sortedMovies) {
            int bestListIndex = findBestList(movie, lists, graph, movieToList);
            if (bestListIndex == -1) {
                MovieList newList = new MovieList("Partition " + (lists.size() + 1));
                newList.addMovie(movie);
                lists.add(newList);
                movieToList.put(movie, lists.size() - 1);
            } else {
                lists.get(bestListIndex).addMovie(movie);
                movieToList.put(movie, bestListIndex);
            }
        }

        return lists;
    }

    private int findBestList(Movie movie, List<MovieList> lists, MovieGraph graph, Map<Movie, Integer> movieToList) {
        Set<Movie> neighbors = graph.getNeighbors(movie);
        int bestIndex = -1;
        int minSize = Integer.MAX_VALUE;

        for (int i = 0; i < lists.size(); i++) {
            MovieList list = lists.get(i);
            boolean hasConflict = false;
            for (Movie listMovie : list.getMovies()) {
                if (neighbors.contains(listMovie)) {
                    hasConflict = true;
                    break;
                }
            }
            if (!hasConflict && list.getMovies().size() < minSize) {
                minSize = list.getMovies().size();
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    public boolean validatePartition(List<MovieList> lists, MovieGraph graph) {
        for (MovieList list : lists) {
            List<Movie> movies = list.getMovies();
            for (int i = 0; i < movies.size(); i++) {
                for (int j = i + 1; j < movies.size(); j++) {
                    if (graph.getNeighbors(movies.get(i)).contains(movies.get(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isBalanced(List<MovieList> lists) {
        if (lists.isEmpty()) return true;
        int minSize = Integer.MAX_VALUE;
        int maxSize = 0;
        for (MovieList list : lists) {
            int size = list.getMovies().size();
            minSize = Math.min(minSize, size);
            maxSize = Math.max(maxSize, size);
        }
        return (maxSize - minSize) <= 1;
    }
}

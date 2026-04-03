package lab6.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lab6.dao.MovieDAO;
import lab6.dao.ActorDAO;
import lab6.dao.MovieActorDAO;
import lab6.model.Movie;
import lab6.model.Actor;
import lab6.model.MovieActor;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class CsvImporter {

    private MovieDAO movieDAO;
    private ActorDAO actorDAO;
    private MovieActorDAO movieActorDAO;

    public CsvImporter() {
        this.movieDAO = new MovieDAO();
        this.actorDAO = new ActorDAO();
        this.movieActorDAO = new MovieActorDAO();
    }

    public int importMovies(String csvPath) throws IOException, CsvValidationException, SQLException {
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] header = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                Movie movie = new Movie();
                movie.setTitle(line[0]);
                movie.setReleaseDate(Date.valueOf(line[1]));
                movie.setDuration(Integer.parseInt(line[2]));
                movie.setScore(Double.parseDouble(line[3]));
                movie.setGenreId(Integer.parseInt(line[4]));
                movieDAO.create(movie);
                count++;
            }
        }
        return count;
    }

    public int importActors(String csvPath) throws IOException, CsvValidationException, SQLException {
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] header = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                Actor actor = new Actor();
                actor.setName(line[0]);
                actor.setBirthDate(Date.valueOf(line[1]));
                actorDAO.create(actor);
                count++;
            }
        }
        return count;
    }

    public int importMovieActors(String csvPath) throws IOException, CsvValidationException, SQLException {
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] header = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                MovieActor ma = new MovieActor();
                ma.setMovieId(Integer.parseInt(line[0]));
                ma.setActorId(Integer.parseInt(line[1]));
                movieActorDAO.create(ma);
                count++;
            }
        }
        return count;
    }
}

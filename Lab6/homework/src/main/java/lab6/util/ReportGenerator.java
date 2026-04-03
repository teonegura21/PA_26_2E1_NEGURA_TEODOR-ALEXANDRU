package lab6.util;

import lab6.report.MovieReport;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public List<MovieReport> fetchMovieData() throws SQLException {
        String sql = "SELECT * FROM movie_details_view ORDER BY movie_id";
        List<MovieReport> reports = new ArrayList<>();
        Map<Integer, MovieReport> movieMap = new HashMap<>();

        try (Connection conn = ConnectionPool.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                MovieReport report = movieMap.get(movieId);

                if (report == null) {
                    report = new MovieReport();
                    report.setMovieId(movieId);
                    report.setTitle(rs.getString("title"));
                    report.setReleaseDate(rs.getDate("release_date"));
                    report.setDuration(rs.getInt("duration"));
                    report.setScore(rs.getDouble("score"));
                    report.setGenreName(rs.getString("genre_name"));
                    report.setActors("");
                    movieMap.put(movieId, report);
                    reports.add(report);
                }

                String actorName = rs.getString("actor_name");
                if (actorName != null) {
                    String currentActors = report.getActors();
                    if (currentActors.isEmpty()) {
                        report.setActors(actorName);
                    } else {
                        report.setActors(currentActors + ", " + actorName);
                    }
                }
            }
        }
        return reports;
    }

    public void generateHtmlReport(String filename) throws SQLException, IOException {
        List<MovieReport> movies = fetchMovieData();

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <title>Movie Database Report</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
        html.append("        h1 { color: #333; text-align: center; }\n");
        html.append("        table { border-collapse: collapse; width: 100%; background-color: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
        html.append("        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
        html.append("        th { background-color: #4CAF50; color: white; font-weight: bold; }\n");
        html.append("        tr:nth-child(even) { background-color: #f9f9f9; }\n");
        html.append("        tr:hover { background-color: #f1f1f1; }\n");
        html.append("        .score { font-weight: bold; color: #ff9800; }\n");
        html.append("        .genre { font-style: italic; color: #2196F3; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <h1>Movie Database Report</h1>\n");
        html.append("    <table>\n");
        html.append("        <tr>\n");
        html.append("            <th>ID</th>\n");
        html.append("            <th>Title</th>\n");
        html.append("            <th>Release Date</th>\n");
        html.append("            <th>Duration (min)</th>\n");
        html.append("            <th>Score</th>\n");
        html.append("            <th>Genre</th>\n");
        html.append("            <th>Actors</th>\n");
        html.append("        </tr>\n");

        for (MovieReport m : movies) {
            html.append("        <tr>\n");
            html.append("            <td>").append(m.getMovieId()).append("</td>\n");
            html.append("            <td>").append(escapeHtml(m.getTitle())).append("</td>\n");
            html.append("            <td>").append(m.getReleaseDate()).append("</td>\n");
            html.append("            <td>").append(m.getDuration()).append("</td>\n");
            html.append("            <td class='score'>").append(m.getScore()).append("</td>\n");
            html.append("            <td class='genre'>").append(escapeHtml(m.getGenreName())).append("</td>\n");
            html.append("            <td>").append(escapeHtml(m.getActors())).append("</td>\n");
            html.append("        </tr>\n");
        }

        html.append("    </table>\n");
        html.append("    <p style='text-align: center; margin-top: 20px; color: #666;'>Generated on: ").append(new java.util.Date()).append("</p>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(html.toString());
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}

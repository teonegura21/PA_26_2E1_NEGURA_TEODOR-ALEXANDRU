package lab6.util;

import java.sql.Connection;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.sql.DataSource;

public class FlywayMigration {

    public static void migrate(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            String[] migrations = {"V1__Initial_schema.sql", "V2__Add_movie_lists.sql", "V3__Add_sample_data.sql"};
            for (String migration : migrations) {
                executeMigration(conn, migration);
            }
        } catch (Exception e) {
            throw new RuntimeException("Migration failed", e);
        }
    }

    private static void executeMigration(Connection conn, String filename) throws IOException, Exception {
        InputStream is = FlywayMigration.class.getClassLoader().getResourceAsStream("db/migration/" + filename);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            reader.close();

            Statement stmt = conn.createStatement();
            for (String command : sql.toString().split(";")) {
                String trimmed = command.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            stmt.close();
        }
    }
}

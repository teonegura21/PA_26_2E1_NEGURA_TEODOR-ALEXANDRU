package lab10.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lab10.common.Question;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestions(String resourcePath) {
        try (Reader reader = new InputStreamReader(
                QuestionLoader.class.getClassLoader().getResourceAsStream(resourcePath))) {
            if (reader == null) {
                // Try with leading slash
                try (Reader reader2 = new InputStreamReader(
                        QuestionLoader.class.getClassLoader().getResourceAsStream("/" + resourcePath))) {
                    if (reader2 == null) {
                        System.err.println("Resource not found: " + resourcePath);
                        return Collections.emptyList();
                    }
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Question>>() {}.getType();
                    List<Question> questions = gson.fromJson(reader2, listType);
                    Collections.shuffle(questions);
                    return questions;
                }
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Question>>() {}.getType();
            List<Question> questions = gson.fromJson(reader, listType);
            Collections.shuffle(questions);
            return questions;
        } catch (Exception e) {
            System.err.println("Failed to load questions: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}

package QuizData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizLoader {

    public List<QuizItem> loadQuizzes(String filePath) {
        List<QuizItem> quizzes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    quizzes.add(new QuizItem(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
        return quizzes;
    }
}



package QuizData;

public class QuizItem {
    private String question;
    private String answer;
    private String explanation;

    public QuizItem(String question, String answer, String explanation) {
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }
}




package QuizData;

public class QuizResult {
    private String playerName;
    private String field;
    private int level;
    private int score;

    public QuizResult(String playerName,String field, int level, int score) {
        this.playerName = playerName;
        this.field = field;
        this.level = level;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }
    
    public String getField() {
        return field;
        //상식 퀴즈 : Quiz
        //역사 퀴즈 : History
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }
}
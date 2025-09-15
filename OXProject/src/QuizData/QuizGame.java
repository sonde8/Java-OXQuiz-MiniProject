package QuizData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class QuizGame {

    private final QuizLoader quizLoader;
    private final Scanner scanner;
    private final ScoreRepository scoreRepository;

    public QuizGame() {
        this.quizLoader = new QuizLoader();
        this.scanner = new Scanner(System.in);
        this.scoreRepository = new ScoreRepository();
    }
    public void start() {
    	System.out.println("\n=========퀴즈 게임에 오신 것을 환영합니다!=========");
    	System.out.println("원하는 퀴즈 분야를 선택해 주세요.");
    	System.out.println("1.상식퀴즈  2.역사퀴즈  3.예술문화퀴즈");
    	int fieldnum = QuizInputTest.getValidNumber(scanner,
    		    "분야를 입력하세요 (1-3): ", 1, 3);
        scanner.nextLine();
        System.out.println("\n1단계, 2단계, 3단계 중 원하는 레벨을 선택하세요.");
        int level = QuizInputTest.getValidNumber(scanner,
        	    "레벨을 입력하세요 (1-3): ", 1, 3);
        scanner.nextLine();
        
        String field = "";
        switch(fieldnum) {
        case 1:
        	field = "Quiz";
        	break;
        case 2:
        	field = "History";
        	break;
        case 3:
        	field = "Culture";
        	break;
        default:
        	System.out.println("잘못된 번호가 입력되었습니다 다시 입력하세요.");
        	start();
        }
        
        // QuizLoader를 통해 문제를 가져옴
        List<QuizItem> allQuizzes = quizLoader.loadQuizzes("src/QuizData/"+field+"Level"+level);

        // 10문제 랜덤 선택
        List<QuizItem> selectedQuizzes = getRandomQuizzes(allQuizzes, 10);
        
        int score = 0;

        System.out.println("\n--- 퀴즈 게임을 시작합니다. ---");
        for (int i = 0; i < selectedQuizzes.size(); i++) {
            QuizItem quiz = selectedQuizzes.get(i);
            System.out.println("\n문제 " + (i + 1) + ": " + quiz.getQuestion());
            String userInput = QuizInputTest.getValidAnswer(scanner, "정답을 입력하세요 (O/X): ");

            if (userInput.equals(quiz.getAnswer())) {
                System.out.println("정답입니다!");
                System.out.println("[해설: " + quiz.getExplanation()+"]\n");
                score += 10;
            } else {
                System.out.println("틀렸습니다. 정답은 " + quiz.getAnswer() + " 입니다.");
                System.out.println("[해설: " + quiz.getExplanation()+"]\n");
            }
        }

        System.out.println("\n--- 게임 종료 ---");
        System.out.println("총 점수는 " + score + "점입니다.");
        if (score == 100) {
            System.out.println(" ██   ████   ████ ");
            System.out.println("███  ██  ██ ██  ██");
            System.out.println(" ██  ██  ██ ██  ██");
            System.out.println(" ██  ██  ██ ██  ██");
            System.out.println("████  ████   ████ ");
        } else if (score == 90) {
            System.out.println(" ████   ████ ");
            System.out.println("██  ██ ██  ██");
            System.out.println(" ████  ██  ██");
            System.out.println("   ██  ██  ██");
            System.out.println("████    ████ ");
        } else if (score == 80) {
            System.out.println(" ████   ████ ");
            System.out.println("██  ██ ██  ██");
            System.out.println(" ████   ████ ");
            System.out.println("██  ██ ██  ██");
            System.out.println(" ████   ████ ");
        } else if (score == 70) {
            System.out.println("██████  ████ ");
            System.out.println("    ██ ██  ██");
            System.out.println("   ██  ██  ██");
            System.out.println("  ██   ██  ██");
            System.out.println(" ██     ████ ");
        } else if (score == 60) {
            System.out.println(" ████   ████ ");
            System.out.println("██     ██  ██");
            System.out.println(" ████  ██  ██");
            System.out.println("██  ██ ██  ██");
            System.out.println(" ████   ████ ");
        } else if (score == 50) {
            System.out.println("██████  ████ ");
            System.out.println("██     ██  ██");
            System.out.println("████   ██  ██");
            System.out.println("    ██ ██  ██");
            System.out.println("████    ████ ");
        } else if (score == 40) {
            System.out.println("██  ██  ████ ");
            System.out.println("██  ██ ██  ██");
            System.out.println("██████ ██  ██");
            System.out.println("    ██ ██  ██");
            System.out.println("    ██  ████ ");
        } else if (score == 30) {
            System.out.println(" ████   ████ ");
            System.out.println("    ██ ██  ██");
            System.out.println(" ████  ██  ██");
            System.out.println("    ██ ██  ██");
            System.out.println(" ████   ████ ");
        } else if (score == 20) {
            System.out.println("██████  ████ ");
            System.out.println("    ██ ██  ██");
            System.out.println("██████ ██  ██");
            System.out.println("██     ██  ██");
            System.out.println("██████  ████ ");
        } else if (score == 10) {
            System.out.println(" ██   ████ ");
            System.out.println("███  ██  ██");
            System.out.println(" ██  ██  ██");
            System.out.println(" ██  ██  ██");
            System.out.println("████  ████ ");
        } else if (score == 0) {
            System.out.println(" ████ ");
            System.out.println("██  ██");
            System.out.println("██  ██");
            System.out.println("██  ██");
            System.out.println(" ████ ");
        }
        System.out.println("\n결과를 기록하려면 1번, 종료하려면 2번을 누르세요.");
        System.out.print("선택: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기
        
        if (choice == 1) {
            // 기록을 선택했을 때
            System.out.print("기록할 이름을 입력하세요: ");
            String playerName = scanner.nextLine();

            // QuizResult 객체에 결과 저장
            QuizResult result = new QuizResult(playerName, field, level, score);

            scoreRepository.saveResult(result);
            
            System.out.println("\n===== 저장된 결과 확인 =====");
            System.out.println("이름: " + result.getPlayerName());
            System.out.println("분야: " + result.getField());
            System.out.println("단계: " + result.getLevel());
            System.out.println("점수: " + result.getScore());
            System.out.println("========================");

        } else if (choice == 2) {
            // 종료를 선택했을 때
            System.out.println("게임을 종료합니다");
        } else {
            // 잘못된 입력일 때
            System.out.println("잘못된 입력입니다. 게임을 종료합니다.");
        }
    }

    // List<QuizItem> allQuizzes : 전체 문제 목록
    // int count : 뽑을 문제 수
    private List<QuizItem> getRandomQuizzes(List<QuizItem> allQuizzes, int count) {
        Random random = new Random();
        List<QuizItem> selected = new ArrayList<>();
        List<Integer> usedIndices = new ArrayList<>();

        while (selected.size() < count && selected.size() < allQuizzes.size()) {
            int index = random.nextInt(allQuizzes.size());
            if (!usedIndices.contains(index)) {
                selected.add(allQuizzes.get(index));
                usedIndices.add(index);
            }
        }
        return selected;
    }

    public static void main(String[] args) {
        new QuizGame().start();
    }
}

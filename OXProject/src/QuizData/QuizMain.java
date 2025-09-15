package QuizData;

import java.util.Scanner;

public class QuizMain {
    private final Scanner scanner;
    private final ScoreRepository scoreRepository;
    
    public QuizMain() {
        this.scanner = new Scanner(System.in);
        this.scoreRepository = new ScoreRepository();
    }
    
    public void showMainMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       퀴즈 게임에 오신 것을 환영합니다!");
            System.out.println("========================================");
            System.out.println("1. 게임 시작");
            System.out.println("2. 기록 보기");
            System.out.println("3. 오늘의 상식");
            System.out.println("4. 종료");
            System.out.println("========================================");
            System.out.print("메뉴를 선택하세요 (1-4)\n입력: ");
            
            try {
               String input = scanner.nextLine();
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        startGame();
                        break;
                    case 2:
                        showRecords();
                        break;
                    case 3:
                       gameInfo();
                       break;
                    case 4:
                       // case3가 종료
                        exitProgram();
                        return;
                        
                    default:
                        System.out.println("잘못된 입력입니다. 1, 2, 3 중에서 선택해주세요.");
                }
            } catch (Exception e) {
                System.out.println("숫자만 입력해주세요.");
                scanner.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }
    
    private void startGame() {
        // 지원님의 클래스에서 start 매서드를 불러와야함
        QuizGame quizGame = new QuizGame();
        quizGame.start();
        
        // 게임이 끝나면 메뉴로 돌아가기 전에 잠시 대기
        System.out.println("\n메인 메뉴로 돌아가려면 엔터를 눌러주세요.");
//        Scanner tempScanner = new Scanner(System.in);
        scanner.nextLine();
    }
    
    // 기록 보기는 아직 구현 안 됨
    private void showRecords() {
        System.out.println("\n========================");
        System.out.println("   기록 보기를 선택했습니다.");
        System.out.println("------------------------");
        System.out.println("1. 전체 기록 보기");
        System.out.println("2. 레벨별 랭킹 보기");
        System.out.print("선택: ");
        
        try {
            String input = scanner.nextLine();
            int choice = Integer.parseInt(input);

            if (choice == 1) {
                scoreRepository.showAll(); // 전체 기록 보기
            } else if (choice == 2) {
                System.out.print("볼 레벨을 입력하세요 (1-3): ");
                String levelInput = scanner.nextLine();
                int level = Integer.parseInt(levelInput);
                scoreRepository.showLeaderboardByLevel(level); // 레벨별 랭킹 보기
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력해주세요.");
        }
        
        System.out.println("\n메인 메뉴로 돌아가려면 엔터를 눌러주세요.");
        scanner.nextLine();
    }
    
    private void gameInfo() {
       GameInfo gameinfo = new GameInfo();
       gameinfo.printGameInfo();
       System.out.println("\n메인 메뉴로 돌아가려면 엔터를 눌러주세요.");
        scanner.nextLine();
    }
    
    private void exitProgram() {
        System.out.println("\n게임을 종료합니다.");
    }
    
    public static void main(String[] args) {
        new QuizMain().showMainMenu();
    }
}
package QuizData;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MiniGame {
    // 게임 상태 변수들
    private final int SCREEN_WIDTH = 80;
    private int cactusX = SCREEN_WIDTH - 5;
    private int score = 0;
    private AtomicBoolean isJumping = new AtomicBoolean(false);
    private int jumpHeight = 0;
    private final int JUMP_SPEED = 2; // 점프 속도
    private final int MAX_JUMP_HEIGHT = 10; // 최대 점프 높이

    // 다양한 장애물 모양 (여러 줄로 그리기 위해 배열로 변경)
    private final String[][] OBSTACLES = {
        {"M"},         // 작은 선인장 (높이 1)
        {"M", "M"},    // 중간 선인장 (높이 2)
        {"M", "M", "M"}, // 큰 선인장 (높이 3)
        {"W"}          // 날아오는 새 (높이 1)
    };

    private String[] currentObstacleShape;
    private int obstacleY;
    private int obstacleHeight;

    private ScheduledExecutorService scheduler;
    private Scanner scanner;
    private volatile boolean isRunning;
    private final Random random = new Random();
    private int gameSpeed = 1; // 게임 속도 (선인장이 이동하는 픽셀 수)

    public MiniGame() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.isRunning = true;
        // 게임 시작 시 초기 장애물 설정
        currentObstacleShape = OBSTACLES[0];
        obstacleY = 13;
        obstacleHeight = 1;
    }
    
    public void start(Scanner scanner) {
        this.scanner = scanner;
        
        System.out.println("--- 콘솔 공룡 게임 ---");
        System.out.println("점프하려면 엔터를 누르세요.");
        System.out.println("게임을 시작하려면 엔터를 눌러주세요.");
        scanner.nextLine();

        startGameLoop();
        handleUserInput();

        while(isRunning) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void startGameLoop() {
        scheduler.scheduleAtFixedRate(() -> {
            updateGame();
            drawGame();
            if (checkCollision()) {
                System.out.println("\n\n--- 게임 오버! ---");
                System.out.println("최종 점수: " + score);
                scheduler.shutdown();
                isRunning = false;
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void updateGame() {
        if (isJumping.get()) {
            if (jumpHeight < MAX_JUMP_HEIGHT) {
                jumpHeight += JUMP_SPEED;
            } else {
                isJumping.set(false);
            }
        } else if (jumpHeight > 0) {
            jumpHeight -= JUMP_SPEED;
        }

        // 게임 속도에 따라 선인장 이동
        cactusX -= gameSpeed;
        if (cactusX < 0) {
            cactusX = SCREEN_WIDTH - 5;
            
            // 점수가 50점 오를 때마다 속도 증가
            if (score > 0 && score % 50 == 0) {
                gameSpeed++;
            }
            
            // 무작위로 장애물 모양 선택
            int shapeIndex = random.nextInt(OBSTACLES.length);
            currentObstacleShape = OBSTACLES[shapeIndex];
            obstacleHeight = currentObstacleShape.length;
            
            if (currentObstacleShape[0].equals("W")) {
                obstacleY = 8;
            } else {
                obstacleY = 13;
            }
            
            score += 10;
        }
    }

    private void drawGame() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        StringBuilder screen = new StringBuilder();
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                if (y == 14) {
                    screen.append("_");
                } 
                else if (x == 5 && y == (13 - jumpHeight)) {
                    screen.append("D");
                } 
                // 장애물 그리기
                else if (x >= cactusX && x < cactusX + currentObstacleShape[0].length()) {
                    // 다층 장애물 그리기
                    for(int i = 0; i < obstacleHeight; i++) {
                        if (y == obstacleY + i) {
                            screen.append(currentObstacleShape[i]);
                            break;
                        }
                    }
                }
                else {
                    screen.append(" ");
                }
            }
            screen.append("\n");
        }
        screen.append("점수: ").append(score);
        System.out.print(screen.toString());
    }

    private boolean checkCollision() {
        // 공룡의 x좌표와 장애물의 x좌표가 겹치는지 확인
        boolean xOverlap = cactusX < 6 && cactusX > 3;

        if (xOverlap) {
            // 장애물에 따라 충돌 높이 확인
            if (currentObstacleShape[0].equals("W")) { // 새
                // 새와 충돌하려면 공룡이 점프하고 있어야 함
                return jumpHeight >= 4 && jumpHeight <= 8;
            } else { // 선인장
                // 선인장과 충돌하려면 공룡이 땅에 가까이 있어야 함
                return jumpHeight <= 2;
            }
        }
        return false;
    }

    private void handleUserInput() {
        new Thread(() -> {
            while (isRunning) {
                if (scanner.nextLine().equals("")) {
                    if (!isJumping.get()) {
                        isJumping.set(true);
                    }
                }
            }
        }).start();
    }
}
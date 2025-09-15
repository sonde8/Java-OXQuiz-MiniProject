package QuizData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScoreRepository {

    private static final String FILE_PATH = "scores.csv";
    // 날짜만 저장/표시
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    // 1) 결과 저장: 파일 없으면 헤더 쓰고, 있으면 뒤에 추가
    // CSV: played_date,field,player,level,score
    public void saveResult(QuizResult r) {
        if (r == null) return;

        // 오늘 날짜 (yyyy-MM-dd)만 저장
        String today = DATE.format(LocalDate.now());

        String line = today + "," +
                      r.getField() + "," +
                      r.getPlayerName() + "," +
                      r.getLevel() + "," +
                      r.getScore();

        Path path = Paths.get(FILE_PATH);
        try {
            if (Files.notExists(path)) {
                // 헤더 + 첫 줄
                Files.write(path,
                        Arrays.asList("played_date,field,player,level,score", line),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE);
            } else {
                Files.write(path,
                        Collections.singletonList(line),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("기록 저장 실패: " + e.getMessage());
        }
    }

    // 2) 전체 기록 보기 (요청 형식으로)
    // 예)
    // === 전체 기록 ===
    // 상식 퀴즈, 2025-09-10,홍길동,1,80
    // 역사 퀴즈, 2025-09-10,전희연,1,100
    public void showAll() {
        Path path = Paths.get(FILE_PATH);
        if (Files.notExists(path)) { System.out.println("(기록 없음)"); return; }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.size() <= 1) { System.out.println("(기록 없음)"); return; }

            System.out.println("=== 전체 기록 ===");
            System.out.println("  분야  |     날짜     |  이름  |  난이도  |  점수  ");
            for (int i = 1; i < lines.size(); i++) {
                String[] c = lines.get(i).split(",");
                if (c.length != 5) continue; // played_date,field,player,level,score
                String date = c[0].trim();
                String fieldCode = c[1].trim();
                String player = c[2].trim();
                String level = c[3].trim();
                String score = c[4].trim();

                String fieldKr = toKoreanField(fieldCode);
                System.out.println(fieldKr + "   " + date + "    " + player + "       " + level + "       " + score);
            }
        } catch (IOException e) {
            System.out.println("기록 읽기 실패: " + e.getMessage());
        }
    }

    // 3) 레벨별 간단 랭킹 (TOP 10) — 새 포맷에 맞춰 파싱
    public void showLeaderboardByLevel(int level) {
        Path path = Paths.get(FILE_PATH);
        if (Files.notExists(path)) { System.out.println("(기록 없음)"); return; }

        // 레코드 읽기
        List<Record> records = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String[] c = lines.get(i).split(",");
                if (c.length != 5) continue; // played_date,field,player,level,score
                try {
                    String playedDate = c[0].trim();     // yyyy-MM-dd
                    String field = c[1].trim();
                    String name  = c[2].trim();
                    int lv       = Integer.parseInt(c[3].trim());
                    int score    = Integer.parseInt(c[4].trim());
                    records.add(new Record(playedDate, field, name, lv, score));
                } catch (Exception ignore) {}
            }
        } catch (IOException e) {
            System.out.println("기록 읽기 실패: " + e.getMessage());
            return;
        }

        // 해당 레벨만
        List<Record> filtered = new ArrayList<>();
        for (Record r : records) if (r.level == level) filtered.add(r);
        if (filtered.isEmpty()) { System.out.println("(해당 레벨 기록 없음)"); return; }

        // 같은 사람은 최고 점수 1개만 (동점이면 최근 날짜가 위)
        Map<String, Record> best = new HashMap<>();
        for (Record r : filtered) {
            Record old = best.get(r.name);
            if (old == null || r.score > old.score ||
                (r.score == old.score && r.playedDate.compareTo(old.playedDate) > 0)) {
                best.put(r.name, r);
            }
        }

        // 정렬: 점수 ↓, 날짜 ↓ (yyyy-MM-dd 문자열 비교로도 최신 판별 가능)
        List<Record> list = new ArrayList<>(best.values());
        list.sort((a, b) -> {
            if (b.score != a.score) return b.score - a.score;
            return b.playedDate.compareTo(a.playedDate);
        });

        // 출력 (TOP 10)
        System.out.println("\n레벨 " + level + " 랭킹 (TOP 10)");
        System.out.println("------------------------------------------------------");
        System.out.printf("%-4s %-8s %-7s %-7s %-10s %-10s%n",
                "순위", "이름", "난이도", "점수", "날짜", "분야");
        System.out.println("------------------------------------------------------");
        int rank = 1;
        for (Record r : list) {
            if (rank > 10) break;
            System.out.printf("%-4d %-12s %-7d %-7d %-10s %-10s%n",
                    rank++, cut(r.name, 12), r.level, r.score, r.playedDate, toKoreanField(r.field));
        }
        System.out.println("------------------------------------------------------\n");
    }

    // ---- 내부 도우미 ----

    private static String toKoreanField(String code) {
        // 필요 시 더 추가
        if (code == null) return "기타";
        return switch (code) {
            case "Quiz"     -> "상식 퀴즈";
            case "History"  -> "역사 퀴즈";
            case "Culture"  -> "문화 퀴즈";
            default         -> code; // 모르는 값은 그대로 출력
        };
    }

    // 내부용 간단 구조체 (새 포맷에 맞춤)
    private static class Record {
        String playedDate; String field; String name; int level; int score;
        Record(String playedDate, String field, String name, int level, int score) {
            this.playedDate = playedDate; this.field = field; this.name = name; this.level = level; this.score = score;
        }
    }

    private static String cut(String s, int n) {
        if (s == null) return "";
        return s.length() <= n ? s : s.substring(0, n);
    }
}

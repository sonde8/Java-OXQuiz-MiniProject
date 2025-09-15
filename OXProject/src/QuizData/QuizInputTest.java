package QuizData;

import java.util.InputMismatchException;
import java.util.Scanner;

public class QuizInputTest {
	public static int getValidNumber(Scanner scanner, String prompt, int min, int max) {
        int number = 0;
        while (true) {
            System.out.print(prompt);
            try {
                number = scanner.nextInt();
                if (number >= min && number <= max) {
                    break;
                } else {
                    System.out.println(min + "에서 " + max + " 사이의 숫자를 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력해주세요.");
                scanner.nextLine(); // 버퍼 비우기
            }
        }
        return number;
	}
	
       public static String getValidAnswer(Scanner scanner, String prompt) {
           String answer = "";
           while (true) {
               System.out.print(prompt);
               try {
            	   answer = scanner.nextLine().toUpperCase();
                   if (answer.equals("O") || answer.equals("X")) {
                       break;
                   } else {
                       System.out.println("O와 X중에서 입력해주세요.");
                   }
               } catch (InputMismatchException e) {
                   System.out.println("문자만 입력해주세요.");
                   scanner.nextLine(); // 버퍼 비우기
               }
           }
           return answer;
    }
}



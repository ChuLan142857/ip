import java.util.Scanner;

public class Rei {
    public static void main(String[] args) {
        String horizontal_line = "____________________________________________________________\n";

        System.out.println(horizontal_line + "Hello! I'm Rei.\n" + "What can I do for you?\n"
            +horizontal_line);

        Scanner scanner = new Scanner(System.in);

        while(true){
            String userInput = scanner.nextLine();

            if(userInput.equals("bye")) {
                System.out.println(horizontal_line+ "Bye. Hope to see you again soon!\n" + horizontal_line);
                break;
            }

            System.out.println(horizontal_line + userInput + "\n" + horizontal_line);
        }

        scanner.close();
    }
}

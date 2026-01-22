import java.util.Scanner;
import java.util.ArrayList;

public class Rei {
    public static void main(String[] args) {
        String horizontal_line = "____________________________________________________________\n";

        System.out.println(horizontal_line + "Hello! I'm Rei.\n" + "What can I do for you?\n"
            +horizontal_line);

        Scanner scanner = new Scanner(System.in);
        ArrayList<String> items = new ArrayList<>();

        while(true){
            String userInput = scanner.nextLine();

            if(userInput.equals("bye")) {
                System.out.println(horizontal_line+ "Bye. Hope to see you again soon!\n" + horizontal_line);
                break;
            }

            if(userInput.equals("list")){
                System.out.println(horizontal_line);
                for (int i = 0; i < items.size(); i++){
                    System.out.println((i + 1) + ". " + items.get(i));
                }
                System.out.println(horizontal_line);
                continue;
            }

            items.add(userInput);
            System.out.println(horizontal_line);
            System.out.println("added: " + userInput);
            System.out.println(horizontal_line);
        }

        scanner.close();
    }
}

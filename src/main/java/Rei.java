import java.util.Scanner;
import java.util.ArrayList;

public class Rei {
    public static void main(String[] args) {
        String horizontal_line = "____________________________________________________________\n";

        System.out.println(horizontal_line + "Hello! I'm Rei.\n" + "What can I do for you?\n"
            +horizontal_line);

        Scanner scanner = new Scanner(System.in);
        ArrayList<String> items = new ArrayList<>();
        ArrayList<Boolean> isDone = new ArrayList<>();

        while(true){
            String userInput = scanner.nextLine();

            if(userInput.equals("bye")) {
                System.out.println(horizontal_line+ "Bye. Hope to see you again soon!\n" + horizontal_line);
                break;
            }

            if(userInput.equals("list")){
                System.out.println(horizontal_line);
                System.out.println("Here are the tasks in your list:\n");
                for (int i = 0; i < items.size(); i++){
                    String status = isDone.get(i) ? "[X]" : "[]";
                    System.out.println((i + 1) + ". " + status + " " + items.get(i));
                }
                System.out.println(horizontal_line);
                continue;
            }

            if(userInput.startsWith("mark ")){
                int index = Integer.parseInt(userInput.substring(5)) - 1;
                isDone.set(index, true);

                System.out.println(horizontal_line);
                System.out.println("Nice! I've marked this task as done:\n");
                System.out.println("[X] " + items.get(index));
                System.out.println(horizontal_line);
                continue;
            }

            if(userInput.startsWith("unmark ")){
                int index = Integer.parseInt(userInput.substring(7)) - 1;
                isDone.set(index, false);

                System.out.println(horizontal_line);
                System.out.println("Ok, I've marked this task as not done yet:\n");
                System.out.println("[ ] " + items.get(index));
                System.out.println(horizontal_line);
                continue;
            }

            items.add(userInput);
            isDone.add(false);

            System.out.println(horizontal_line);
            System.out.println("added: " + userInput);
            System.out.println(horizontal_line);
        }

        scanner.close();
    }
}

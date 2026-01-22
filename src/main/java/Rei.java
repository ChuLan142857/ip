import java.util.Scanner;
import java.util.ArrayList;

public class Rei {
    public static void main(String[] args) {
        String horizontal_line = "____________________________________________________________\n";

        System.out.println(horizontal_line + "Hello! I'm Rei.\n" + "What can I do for you?\n"
            +horizontal_line);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while(true){
            String userInput = scanner.nextLine();

            if(userInput.equals("bye")) {
                System.out.println(horizontal_line+ "Bye. Hope to see you again soon!\n" + horizontal_line);
                break;
            }

            if(userInput.equals("list")){
                System.out.println(horizontal_line);
                System.out.println("Here are the tasks in your list:\n");
                for (int i = 0; i < tasks.size(); i++){
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println(horizontal_line);
                continue;
            }

            if(userInput.startsWith("mark ")){
                int index = Integer.parseInt(userInput.substring(5)) - 1;
                Task task = tasks.get(index);
                task.markDone();

                System.out.println(horizontal_line);
                System.out.println("Nice! I've marked this task as done:\n");
                System.out.println(task);
                System.out.println(horizontal_line);
                continue;
            }

            if(userInput.startsWith("unmark ")){
                int index = Integer.parseInt(userInput.substring(7)) - 1;
                Task task = tasks.get(index);
                task.markUndone();

                System.out.println(horizontal_line);
                System.out.println("Ok, I've marked this task as not done yet:\n");
                System.out.println(task);
                System.out.println(horizontal_line);
                continue;
            }

            Task task = new Task(userInput);
            tasks.add(task);

            System.out.println(horizontal_line);
            System.out.println("added: " + userInput);
            System.out.println(horizontal_line);
        }

        scanner.close();
    }
}

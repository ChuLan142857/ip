import java.util.Scanner;
import java.util.ArrayList;

public class Rei {

    private static void addTodo(String input, ArrayList<Task> tasks, String line)
            throws ReiExceptions {

        if (input.equals("todo")) {
            throw new ReiExceptions("OOPS!!! The description of a todo cannot be empty.");
        }

        String description = input.substring(5);
        tasks.add(new Todo(description));

        System.out.println(line);
        System.out.println("Got it. I've added this task:");
        System.out.println(tasks.get(tasks.size() - 1));
        System.out.println("Now you have " + tasks.size() + " tasks in the list");
        System.out.println(line);
    }

    private static void addDeadline(String input, ArrayList<Task> tasks, String line)
            throws ReiExceptions {

        String[] parts = input.split(" /by ");

        if (parts.length < 2 || parts[0].equals("deadline")) {
            throw new ReiExceptions(
                    "OOPS!!! A deadline must have a description and a /by time."
            );
        }

        tasks.add(new Deadline(parts[0].substring(9), parts[1]));

        System.out.println(line);
        System.out.println("Got it. I've added this task:");
        System.out.println(tasks.get(tasks.size() - 1));
        System.out.println("Now you have " + tasks.size() + " tasks in the list");
        System.out.println(line);
    }

    private static void addEvent(String input, ArrayList<Task> tasks, String line)
            throws ReiExceptions {

        String[] parts = input.split(" /from | /to ");

        if (parts.length < 3 || parts[0].equals("event")) {
            throw new ReiExceptions(
                    "OOPS!!! An event must have a description, /from time and /to time."
            );
        }

        tasks.add(new Event(parts[0].substring(6), parts[1], parts[2]));

        System.out.println(line);
        System.out.println("Got it. I've added this task:");
        System.out.println(tasks.get(tasks.size() - 1));
        System.out.println("Now you have " + tasks.size() + " tasks in the list");
        System.out.println(line);
    }

    private static void markDone(String input, ArrayList<Task> tasks, String line)
            throws ReiExceptions {

        int index = parseIndex(input, 5, tasks.size());
        tasks.get(index).markDone();

        System.out.println(line);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(tasks.get(index));
        System.out.println(line);
    }

    private static void markUndone(String input, ArrayList<Task> tasks, String line)
            throws ReiExceptions {

        int index = parseIndex(input, 7, tasks.size());
        tasks.get(index).markUndone();

        System.out.println(line);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(tasks.get(index));
        System.out.println(line);
    }

    private static int parseIndex(String input, int start, int size)
            throws ReiExceptions {
        try {
            int index = Integer.parseInt(input.substring(start)) - 1;
            if (index < 0 || index >= size) {
                throw new ReiExceptions("OOPS!!! That task number is invalid.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }



public static void main(String[] args) {
    String horizontal_line = "____________________________________________________________\n";

    System.out.println(horizontal_line + "Hello! I'm Rei.\n" + "What can I do for you?\n"
            + horizontal_line);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while(true){
            try {
                String userInput = scanner.nextLine();

                if (userInput.equals("bye")) {
                    System.out.println(horizontal_line + "Bye. Hope to see you again soon!\n" + horizontal_line);
                    break;
                }

                if (userInput.equals("list")) {
                    System.out.println(horizontal_line);
                    System.out.println("Here are the tasks in your list:\n");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                    System.out.println(horizontal_line);
                    continue;
                }

                if (userInput.startsWith("mark ")) {
                    markDone(userInput, tasks, horizontal_line);
                    continue;
                }

                if (userInput.startsWith("unmark ")) {
                    markUndone(userInput, tasks, horizontal_line);
                    continue;
                }

                if (userInput.startsWith("todo")) {
                    addTodo(userInput, tasks, horizontal_line);
                    continue;
                }

                if (userInput.startsWith("deadline")) {
                    addDeadline(userInput, tasks, horizontal_line);
                    continue;
                }

                if (userInput.startsWith("event")) {
                    addEvent(userInput, tasks, horizontal_line);
                    continue;
                }

                throw new ReiExceptions("OOPS!!! I'm sorry, but I don't know what that means");

            } catch (ReiExceptions e){
                System.out.println(horizontal_line);
                System.out.println(e.getMessage());
                System.out.println(horizontal_line);
            }


        }

        scanner.close();
    }
}

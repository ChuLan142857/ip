import java.io.*;
import java.util.ArrayList;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws ReiExceptions {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);

                if (task != null){
                    tasks.add(task);
                }
            }
            reader.close();
            return tasks;

        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to load tasks from file.");
        }
    }

    public void save(ArrayList<Task> tasks) throws ReiExceptions {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to save tasks to file.");
        }
    }

    private Task parseTask(String line)
    {
        String[] parts = line.split(" \\| ");

        Task task = null;
        switch (parts[0]) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                task = new Deadline(parts[2], parts[3]);
                break;
            case "E":
                task = new Event(parts[2], parts[3], parts[4]);
                break;
            default:
                return null;
        }

        if (parts[1].equals("1")) {
            task.markDone();
        }

        return task;
    }
}


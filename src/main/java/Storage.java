import java.io.*;
import java.time.LocalDateTime;
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

    public void save(TaskList tasks) throws ReiExceptions {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            ArrayList<Task> list = tasks.getAll();
            for (Task task : list) {
                writer.write(task.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to save tasks to file.");
        }
    }

    private Task parseTask(String line) throws ReiExceptions {
        String[] parts = line.split(" \\| ");

        switch (parts[0]) {
            case "T":
                return loadTodo(parts);
            case "D":
                return loadDeadline(parts);
            case "E":
                return loadEvent(parts);
            default:
                throw new ReiExceptions("OOPS!!! Corrupted data file.");
        }
    }

    private Task loadTodo(String[] parts)
    {
        Task t = new Todo(parts[2]);
        setDone(t, parts[1]);
        return t;
    }

    private Task loadDeadline(String[] parts)
    {
        LocalDateTime by = LocalDateTime.parse(parts[3]);
        Task d = new Deadline(parts[2], by);
        setDone(d, parts[1]);
        return d;
    }

    private Task loadEvent(String[] parts)
    {
        LocalDateTime from = LocalDateTime.parse(parts[3]);
        LocalDateTime to = LocalDateTime.parse(parts[4]);
        Task e = new Event(parts[2], from, to);
        setDone(e, parts[1]);
        return e;
    }

    private void setDone(Task task, String doneFlag)
    {
        if (doneFlag.equals("1")) {
            task.markDone();
        }
    }
}


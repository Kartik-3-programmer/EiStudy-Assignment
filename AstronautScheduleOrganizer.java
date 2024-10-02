import java.lang;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

class Task{
	private String description;
    private Date startTime;
    private Date endTime;
    private String priority;
    private boolean completed;
    
    public Task(String description, String startTime, String endTime, String priority) throws ParseException {
        this.description = description;
        this.startTime = parseTime(startTime);
        this.endTime = parseTime(endTime);
        this.priority = priority;
        this.completed = false;
    }
    private Date parseTime(String timeStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.parse(timeStr);
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(startTime) + " - " + sdf.format(endTime) + ": " + description + " [" + priority + "]";
    }
}

class TaskFactory {
    public static Task createTask(String description, String startTime, String endTime, String priority) throws ParseException {
        return new Task(description, startTime, endTime, priority);
    }
}

class ScheduleManager {
    private static ScheduleManager instance = null;
    private List<Task> tasks;

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addTask(Task task) {
        if (!hasConflict(task)) {
            tasks.add(task);
            System.out.println("Task added successfully. No conflicts.");
        } else {
            System.out.println("Error: Task conflicts with an existing task.");
        }
    }

    private boolean hasConflict(Task newTask) {
        for (Task task : tasks) {
            if (!(newTask.getEndTime().before(task.getStartTime()) || newTask.getStartTime().after(task.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    public void removeTask(String description) {
        for (Task task : tasks) {
            if (task.getDescription().equals(description)) {
                tasks.remove(task);
                System.out.println("Task '" + description + "' removed successfully.");
                return;
            }
        }
        System.out.println("Error: Task '" + description + "' not found.");
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            Collections.sort(tasks, Comparator.comparing(Task::getStartTime));
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    public void viewTasksByPriority(String priority) {
        boolean found = false;
        for (Task task : tasks) {
            if (task.getPriority().equals(priority)) {
                System.out.println(task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks with priority '" + priority + "' found.");
        }
    }
}


public class AstronautScheduleOrganizer {

	public static void main(String[] args) {
		 try {
	            ScheduleManager manager = ScheduleManager.getInstance();

	            Task task1 = TaskFactory.createTask("Morning Exercise", "07:00", "08:00", "High");
	            Task task2 = TaskFactory.createTask("Team Meeting", "09:00", "10:00", "Medium");
	            Task task3 = TaskFactory.createTask("Lunch Break", "12:00", "13:00", "Low");

	            manager.addTask(task1);
	            manager.addTask(task2);
	            manager.addTask(task3);

	            System.out.println("\n--- Viewing All Tasks ---");
	            manager.viewTasks();

	            Task conflictingTask = TaskFactory.createTask("Training Session", "09:30", "10:30", "High");
	            manager.addTask(conflictingTask);

	            manager.removeTask("Team Meeting");
	            
	            System.out.println("\n--- Viewing Tasks with High Priority ---");
	            manager.viewTasksByPriority("High");

	            task1.markCompleted();
	            System.out.println("\nTask '" + task1.getDescription() + "' marked as completed: " + task1.isCompleted());

	        } catch (ParseException e) {
	            System.out.println("Error: Invalid time format.");
	        }

	}

}

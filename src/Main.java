import config.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import service.FileBackedTaskManager;
import service.interfaces.TaskManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        File file;
        try {
            file = File.createTempFile("data", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalDateTime taskStart1 = LocalDateTime.now().plusDays(1);
        LocalDateTime taskStart2 = taskStart1.plusMinutes(50);
        LocalDateTime taskStart3 = taskStart1.plusMinutes(20);
        LocalDateTime taskStart4 = taskStart2.plusMinutes(30);
        LocalDateTime taskStart5 = taskStart2.plusMinutes(30);

        TaskManager tm = Managers.getDefault(file);
        Task t1 = new Task("first", "to do", TaskStatus.NEW, 30, taskStart1);
        Task t2 = new Task("second", "to do", TaskStatus.NEW, 30, taskStart2);

        tm.createTask(t1);
        tm.createTask(t2);

        EpicTask et1 = new EpicTask("first epic", "epic with 3 subs");
        SubTask st1 = new SubTask("first st", "sub 1", TaskStatus.IN_PROGRESS, et1.getId(), 30, taskStart3);
        SubTask st2 = new SubTask("second st", "sub 2", TaskStatus.DONE, et1.getId(), 30, taskStart4);
        SubTask st3 = new SubTask("third st", "sub 3", TaskStatus.IN_PROGRESS, et1.getId(), 30, taskStart5);
        et1.addSubTask(st1);
        et1.addSubTask(st2);
        et1.addSubTask(st3);

        tm.createEpicTask(et1);
        tm.createSubTask(st1);
        tm.createSubTask(st2);
        tm.createSubTask(st3);

        EpicTask et2 = new EpicTask("second epic", "empty epic");

        tm.createEpicTask(et2);

        System.out.println("___________________");
        System.out.println("tasks:\n");
        printAllTasks(tm);
        System.out.println("___________________\n");

        tm.getSubTaskById(st3.getId());
        printHistory(tm);
        tm.getTaskById(t1.getId());
        printHistory(tm);
        tm.getEpicTaskById(et2.getId());
        printHistory(tm);
        tm.getTaskById(t2.getId());
        printHistory(tm);
        tm.getTaskById(t1.getId());
        printHistory(tm);
        tm.getEpicTaskById(et2.getId());
        printHistory(tm);
        tm.getEpicTaskById(et1.getId());
        printHistory(tm);
        tm.getSubTaskById(st1.getId());
        printHistory(tm);
        tm.getSubTaskById(st2.getId());
        printHistory(tm);
        tm.getTaskById(t1.getId());
        printHistory(tm);
        tm.getSubTaskById(st3.getId());
        printHistory(tm);


        System.out.println("___________________");
        System.out.println("tasks before saving:\n");
        printAllTasks(tm);
        System.out.println("___________________\n");

        TaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("___________________");
        System.out.println("tasks after loading from save:\n");
        printAllTasks(taskManager);
        System.out.println("___________________\n");

        tm.removeSubTaskById(st2.getId());
        printHistory(tm);
        tm.removeEpicTaskById(et1.getId());
        printHistory(tm);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        manager.getTasks().forEach(System.out::println);

        System.out.println("Эпики:");
        manager.getEpicTasks().forEach(epic -> {
            System.out.println(epic);
            manager.getSubTasks(epic).forEach(task -> System.out.println("--> " + task));
        });
        System.out.println("Подзадачи:");
        manager.getSubTasks().forEach(System.out::println);

        System.out.println("приоритетные задачи:");
        manager.getPrioritizedTasks().forEach(System.out::println);

        printHistory(manager);
    }

    private static void printHistory(TaskManager manager) {
        System.out.println("История:");
        manager.getHistory().forEach(System.out::println);
    }
}

import config.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import service.interfaces.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager tm = Managers.getDefault();
        Task t1 = new Task("first", "to do", TaskStatus.NEW);
        Task t2 = new Task("second", "to do", TaskStatus.NEW);

        tm.createTask(t1);
        tm.createTask(t2);

        SubTask st1 = new SubTask("first st", "sub 1", TaskStatus.NEW);
        SubTask st2 = new SubTask("second st", "sub 2", TaskStatus.NEW);
        SubTask st3 = new SubTask("third st", "sub 3", TaskStatus.NEW);
        EpicTask et1 = new EpicTask("first epic", "epic with 3 subs");
        st1.setEpicTask(et1);
        st2.setEpicTask(et1);
        st3.setEpicTask(et1);
        et1.addSubTask(st1);
        et1.addSubTask(st2);
        et1.addSubTask(st3);

        tm.createSubTask(st1);
        tm.createSubTask(st2);
        tm.createSubTask(st3);
        tm.createEpicTask(et1);

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

        tm.removeSubTaskById(st2.getId());
        printHistory(tm);
        tm.removeEpicTaskById(et1.getId());
        printHistory(tm);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (EpicTask epic : manager.getEpicTasks()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        printHistory(manager);
    }

    private static void printHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}

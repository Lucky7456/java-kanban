import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import service.interfaces.TaskManager;
import config.Managers;

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
        EpicTask et1 = new EpicTask("first epic", st1.getName() + "; " + st2.getName());
        st1.setEpicTask(et1);
        st2.setEpicTask(et1);
        et1.addSubTask(st1);
        et1.addSubTask(st2);

        tm.createSubTask(st1);
        tm.createSubTask(st2);
        tm.createEpicTask(et1);

        SubTask st3 = new SubTask("third st", "sub 3", TaskStatus.NEW);
        EpicTask et2 = new EpicTask("second epic", st3.getName());
        st3.setEpicTask(et2);
        et2.addSubTask(st3);

        tm.createSubTask(st3);
        tm.createEpicTask(et2);

        System.out.println("___________________");
        System.out.println("tasks:\n");
        printAllTasks(tm);


        tm.updateTask(new Task("first", "to do", t1.getId(), TaskStatus.DONE));
        tm.updateTask(new Task("second", "to do", t2.getId(), TaskStatus.IN_PROGRESS));
        tm.updateSubTask(new SubTask("second st", "sub 2", st2.getId(), TaskStatus.IN_PROGRESS));
        tm.updateSubTask(new SubTask("first st", "sub 1", st1.getId(), TaskStatus.DONE));
        tm.updateSubTask(new SubTask("third st", "sub 3", st3.getId(), TaskStatus.DONE));
        tm.updateEpicTask(new EpicTask("first epic", st1.getName() + "; " + st2.getName(), et1.getId()));

        tm.getSubTaskById(st3.getId());
        tm.getTaskById(t1.getId());
        tm.getEpicTaskById(et2.getId());
        tm.getTaskById(t2.getId());
        tm.getTaskById(t1.getId());

        System.out.println("\n___________________");
        System.out.println("updated tasks:");
        printAllTasks(tm);

        tm.getEpicTaskById(et1.getId());
        tm.getSubTaskById(st1.getId());
        tm.getSubTaskById(st2.getId());
        tm.getSubTaskById(st1.getId());
        tm.getEpicTaskById(et2.getId());
        tm.getTaskById(t1.getId());

        tm.removeTaskById(t2.getId());
        tm.removeEpicTaskById(et2.getId());

        System.out.println("\n___________________");
        System.out.println("after removing tasks:");
        printAllTasks(tm);

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

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}

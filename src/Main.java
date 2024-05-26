import Model.EpicTask;
import Model.SubTask;
import Model.Task;
import Model.Enums.TaskStatus;
import Service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager tm = new TaskManager();
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
        System.out.println(tm.getTasks());
        System.out.println(tm.getSubTasks());
        System.out.println(tm.getEpicTasks());

        tm.updateTask(new Task("first", "to do", t1.getId(), TaskStatus.DONE));
        tm.updateTask(new Task("second", "to do", t2.getId(), TaskStatus.IN_PROGRESS));
        tm.updateSubTask(new SubTask("second st", "sub 2", st2.getId(), TaskStatus.IN_PROGRESS));
        tm.updateSubTask(new SubTask("first st", "sub 1", st1.getId(), TaskStatus.DONE));
        tm.updateSubTask(new SubTask("third st", "sub 3", st3.getId(), TaskStatus.DONE));
        tm.updateEpicTask(new EpicTask("first epic", st1.getName() + "; " + st2.getName(), et1.getId()));

        System.out.println("\n___________________");
        System.out.println("updated tasks:");
        System.out.println(tm.getTasks());
        System.out.println(tm.getSubTasks());
        System.out.println(tm.getEpicTasks());

        tm.removeTaskById(t2.getId());
        tm.removeEpicTaskById(et2.getId());

        System.out.println("\n___________________");
        System.out.println("after removing tasks:");
        System.out.println(tm.getTasks());
        System.out.println(tm.getSubTasks());
        System.out.println(tm.getEpicTasks());

    }
}

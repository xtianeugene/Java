import java.util.ArrayList;
import java.util.Scanner;

public class TodoList {

    static class Task{
        String title;
        boolean done;

        Task(String title) { this.title = title; this.done = false;}

        @Override
        public String toString(){
            return (done ? "[✓]" : "[ ]") + title;
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (true){
            System.out.println("\n TO-DO LIST");
            System.out.println("1. Add task");
            System.out.println("2. View tasks");
            System.out.println("3. Mark as done");
            System.out.println("4. Delete task");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            if (!sc.hasNextInt()) {sc.next(); continue; }
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1 -> {
                    System.out.println("Task name: ");
                    String title = sc.nextLine();
                    tasks.add(new Task(title));
                    System.out.println("Added.");
                }
                case 2 -> {
                    if (tasks.isEmpty()){
                        System.out.println("(empty)");
                    } else {
                        for (int i = 0; i < tasks.size(); i++){
                            System.out.println((i + 1) + ". " + tasks.get(i));
                        }
                    }
                }
                case 3 -> {
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks."); break; }
                        System.out.print("Task number to mark done: ");
                        int i = sc.nextInt() - 1;
                        if (i >= 0 && i < tasks.size()) {
                            tasks.get(i).done = true;
                            System.out.println("Marked.");
                        } else System.out.println("Invalid. ");
                }
                case 4 -> {
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks."); break;
                    }
                    System.out.println("Task number to delete: ");
                    int i = sc.nextInt() - 1;
                    if (i >= 0 && i < tasks.size()) {
                        tasks.remove(i);
                        System.out.println("Deleted.");
                    } else System.out.println("Invalid.");
                }
                case 5 -> {
                    System.out.println("Bye!"); sc.close(); return;
                }
                default -> System.out.println("Invalid.");

            }
        }
    }
}
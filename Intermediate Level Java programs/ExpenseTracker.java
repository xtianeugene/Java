import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    static class Expense {
        LocalDate date;
        String category;
        String description;
        double amount;

        Expense(LocalDate date, String category, String description, double amount){
            this.date = date;
            this.category = category;
            this.description = description;
            this.amount = amount;
        }

        String toCSV(){
            return String.join(",", date.toString(), category, description, String.valueOf(amount));
        }
        static Expense fromCSV(String line){
            String[] p = line.split(",", 4);
            return new Expense(LocalDate.parse(p[0]), p[1], p[2], Double.parseDouble(p[3]));
        }

        @Override
        public String toString() {
            return String.format("%s | %-10s | %-20s | $%.2f", date, category, description, amount);
        }
    }

    static final String FILE = "expenses.csv";
    static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        load();
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("\n EXPENSE TRACKER");
            System.out.println("1. Add expense");
            System.out.println("2. List all");
            System.out.println("3. Total by Category");
            System.out.println("4. Total this month");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            if (!sc.hasNextInt()) { sc.next(); continue; }
            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1 -> {
                    System.out.print("Category: ");
                    String cat = sc.nextLine();
                    System.out.print("Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Amount: $");
                    double amt = sc.nextDouble();
                    sc.nextLine();
                    expenses.add(new Expense(LocalDate.now(), cat, desc, amt));
                    save();
                    System.out.println("Added.");
                }
                case 2 -> {
                    if (expenses.isEmpty()) System.out.println("(none)");
                    else expenses.forEach(System.out::println);
                }
                case 3 -> {
                    Map<String, Double> byCat = new TreeMap<>();
                    for (Expense e : expenses)
                        byCat.merge(e.category, e.amount, Double::sum);
                    byCat.forEach((k, v) -> System.out.printf(" %-10s $%.2f%n", k, v));
                }
                case 4 -> {
                    LocalDate now = LocalDate.now();
                    double total = expenses.stream()
                            .filter(e -> e.date.getYear() == now.getYear()
                                       && e.date.getMonth() == now.getMonth())
                            .mapToDouble(e -> e.amount).sum();
                    System.out.printf("This month: $%.2f%n", total);
                }
                case 5 -> { sc.close(); return; }
                default -> System.out.println("Invalid.");

            }
        }
    }
    static void load() throws IOException {
        Path path = Paths.get(FILE);
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null){
                if (line.isBlank()) continue;
                expenses.add(Expense.fromCSV(line));
            }
        }
        System.out.println(" Loaded " + expenses.size() + "expenses.");
    }

    static void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(FILE))) {
            for (Expense e : expenses) {
                bw.write(e.toCSV());
                bw.newLine();
            }
        }
    }

}

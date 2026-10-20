import java.util.*;
import java.util.stream.Collectors;

public class GradeTracker {
    static class Student{
        String name;
        List<Integer> grades;

        Student(String name, List<Integer> grades){
            this.name = name;
            this.grades = grades;
        }

        double average(){
            return grades.stream().mapToInt(Integer::intValue).average().orElse(0);
        }
        String letterGrade(){
            double a = average();
            if (a >= 90) return "A";
            if (a >= 80) return "B";
            if (a >= 70) return "C";
            if (a >= 60) return "D";
            return "F";
        }
        @Override
        public String toString(){
            return String.format("%-10s avg=%.2f (%s) grades=%s", name, average(), letterGrade(), grades);
        }
    }

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", List.of(92, 88, 95)));
        students.add(new Student("Bob", List.of(70, 65, 72)));
        students.add(new Student("Charlie", List.of(85, 90, 88)));
        students.add(new Student("Diana", List.of(55, 60, 58)));
        students.add(new Student("Eve", List.of(100, 98, 97)));

        System.out.println("All students:");
        students.forEach(System.out::println);

        System.out.println("\n Sorted by average (descending): ");
        students.stream()
                .sorted(Comparator.comparingDouble(Student::average).reversed())
                .forEach(s -> System.out.printf(" %.2f -- %s%n", s.average(), s.name));
        System.out.println("\n Class average: " + String.format("%.2f", students.stream().mapToDouble(Student::average).average().orElse(0)));
        System.out.println("\n Top Student: " + students.stream().max(Comparator.comparingDouble(Student::average)).get().name);
        System.out.println("\n Letter grade distribution: ");
        Map<String, Long> dist = students.stream()
                .collect(Collectors.groupingBy(Student::letterGrade, TreeMap::new, Collectors.counting()));
        dist.forEach((g, count) -> System.out.println(" " + g + ": " + count));
        System.out.println("\n Students needing help (<70): ");
        students.stream()
                .filter(s -> s.average() < 70)
                .forEach(s -> System.out.println(" " + s.name));
        System.out.println("\n Honor roll (>=90):");
        students.stream()
                .filter(s -> s.average() >= 90)
                .map(s -> s.name)
                .forEach(n -> System.out.println(" \uD83C\uDF93 " + n));
    }
}
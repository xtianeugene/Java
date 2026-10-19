import java.util.Scanner;

public class Calculator {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("Simple Calculator");

        while (running) {
            System.out.println("\nChoose operation: ");
            System.out.println("1. Add (+)");
            System.out.println("2. Subtract (-)");
            System.out.println("3. Multiply (*)");
            System.out.println("4. Divide (/)");
            System.out.println("5. Modulo (%)");
            System.out.println("6. Power (^)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            if (choice == 0) {
                running = false;
                System.out.println("Goodbye!");
                continue;
            }

            if (choice < 1 || choice > 6) {
                System.out.println("Invalid choice.");
                continue;
            }

            System.out.print("Enter first number: ");
            double a = sc.nextDouble();
            System.out.print("Enter second number: ");
            double b = sc.nextDouble();

            double result;
            switch (choice) {
                case 1 -> result = a + b;
                case 2 -> result = a - b;
                case 3 -> result = a * b;
                case 4 -> {
                    if (b == 0) {
                        System.out.println("Cannot divide by zero.");
                        continue;
                    }
                    result = a / b;
                }
                case 5 -> {
                    if (b == 0) {
                        System.out.println("Cannot modulo by zero.");
                        continue;
                    }
                    result = a % b;
                }
                case 6 -> result = Math.pow(a, b);
                default -> result = 0;
            }

            System.out.printf("Result: %.4f%n", result);

        }
        sc.close();
    }
}


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankApp {
    static class Account{
        private final String owner;
        private double balance;
        private final List<String> history = new ArrayList<>();

        Account(String owner, double initial){
            this.owner = owner;
            this.balance = initial;
            history.add("Account-opened with $" + initial);
        }

        void deposit(double amount){
            if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
            balance += amount;
            history.add("Deposit: +$" + amount);
        }

        void withdraw(double amount){
            if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
            if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
            balance -= amount;
            history.add("Withdraw: -$" + amount);
        }

        double getBalance() { return balance;}
        String getOwner() { return owner; }
        List<String> getHistory() {return history; }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to SimpleBank");
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Initial deposit: $");
        double initial = sc.nextDouble();

        Account account = new Account(name, initial);
        System.out.printf("Account created for %s with $%.2f%n", name, initial);
        while (true){
            System.out.println("\n1. Check balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction history");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            if (!sc.hasNextInt()) { sc.next(); continue;}
            int choice = sc.nextInt();

            try {
                switch (choice){
                    case 1 -> System.out.printf("Balance: $%.2f%n", account.getBalance());
                    case 2 -> {
                        System.out.println("Deposit amount: $");
                        double amt = sc.nextDouble();
                        account.deposit(amt);
                        System.out.printf("Deposited. New balance: $%.2f%n", account.getBalance());
                    }
                    case 3 -> {
                            System.out.print("Withdraw amount: $");
                            double amt = sc.nextDouble();
                            account.withdraw(amt);
                            System.out.printf("Withdrew. New balance: $%.2f%n", account.getBalance());}
                    case 4 -> {
                        System.out.println("\n History:");
                        for (String h : account.getHistory()) System.out.println(" * " + h);
                    }
                    case 5 -> {
                        System.out.println("Goodbye!"); sc.close(); return;
                    }
                    default -> System.out.println("Invalid choice.");

                    }
            } catch (IllegalArgumentException e){
                System.out.println("❌" + e.getMessage());
            }
            }

        }
    }










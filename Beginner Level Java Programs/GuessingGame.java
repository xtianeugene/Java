import java.util.Random;
import java.util.Scanner;

public class GuessingGame{
    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();
    static int bestScore = Integer.MAX_VALUE;

    public static void main(String[] args){
        System.out.println("Number Guessing Game!");

        boolean playAgain = true;
        while (playAgain){
            playGame();

            System.out.println("\nPlay again? (y/n): ");
            String answer = sc.next();
            playAgain = answer.equalsIgnoreCase("y");
        }

        System.out.println("\nThanks for playing! Best score this session: " + (bestScore == Integer.MAX_VALUE ? "N/A" : bestScore + " attempts"));
        sc.close();
    }

    static void playGame(){
        int[] config = chooseDifficulty();
        int maxNumber = config[0];
        int maxAttempts = config[1];

        int secret = random.nextInt(maxNumber) + 1;
        int attempts = 0;

        System.out.println("\nI'm thinking of a number between 1 and " + maxNumber);
        System.out.println("You have " + maxAttempts + " attempts. Good luck!\n");

        while (attempts < maxAttempts){
            int guess = getValidGuess(1, maxNumber);
            attempts++;

            if (guess == secret){
                System.out.println("Correct! You got it in " + attempts + "attempts!");
                if (attempts < bestScore){
                    bestScore = attempts;
                    System.out.println("New best score!");
                }
                return;
            }

            giveHint(guess, secret);

            int remaining = maxAttempts - attempts;
            if (remaining > 0){
                System.out.println(" Attempts left: " + remaining);
            }
        }
        System.out.println("Out of attempts! The number was " + secret);
    }

    static int[] chooseDifficulty(){
        while (true){
            System.out.println("\nChoose difficulty: ");
            System.out.println("1. Easy (1-50, 10 attempts)");
            System.out.println("2. Medium (1-100, 7 attempts)");
            System.out.println("3. Hard (1-500, 6 attempts)");
            System.out.print("choice: ");
            int choice = sc.nextInt();
            switch (choice){
                case 1: return new int[]{50, 10};
                case 2: return new int[]{100, 7};
                case 3: return new int[]{500,6};
                default:
                    System.out.println("Invalid. Try again.");
            }
        }
    }
    static int getValidGuess(int min, int max){
        while (true){
            System.out.print("Your guess: ");
            if (!sc.hasNextInt()){
                sc.next();
                System.out.println("Please enter a number.");
                continue;
            }
            int g = sc.nextInt();
            if (g < min || g > max){
                System.out.println("Guest must be between " + min + " and " + max);
                continue;
            }
            return g;
        }
    }

    static void giveHint(int guess, int secret){
        int diff = Math.abs(guess - secret);

        String direction = guess < secret ? "Too low!" : "Too high!";
        String temperature;
        if (diff <= 5) temperature = "Very close!";
        else if (diff <= 15) temperature = "Warm";
        else temperature = "Cold";

        System.out.println(direction + " " + temperature);
    }
}
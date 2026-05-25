import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class App {

    static int maxGuess = 21;

    public enum Winner {
        BOT,
        User
    }

    public enum BoundKey {
        low, high
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the number guessing game");

        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        Winner winner = null;
        int secretBotNumber = random.nextInt(maxGuess);

        System.out.printf("What is the number you want the bot to guess between (1 - %d): ", maxGuess - 1);
        int userNumber = scanner.nextInt();

        HashMap<BoundKey, Integer> botBounds = new HashMap<>();

        while (winner == null) {
            try {
                System.out.println("Guess the bot's number: ");

                int userGuess = scanner.nextInt();
                if (userGuess == secretBotNumber) {
                    winner = Winner.User;
                    System.out.println("You correctly guessed the bot number which is : " + secretBotNumber);
                } else if (userGuess < secretBotNumber) {
                    System.out.println("Go higher ");
                } else {
                    System.out.println("Go Lower ");
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("You need to enter a valid number");
                continue;
            }

            int botGuess = random.nextInt(botBounds.getOrDefault(BoundKey.low, 0),
                    botBounds.getOrDefault(BoundKey.high, maxGuess));

            if (botGuess == userNumber) {
                winner = Winner.BOT;
                System.out.println("The bot guessed your number correctly which is: " + userNumber);

            } else if (botGuess < userNumber) {
                botBounds.put(BoundKey.low, botGuess);
            } else {
                botBounds.put(BoundKey.high, botGuess);
            }

        }

        System.out.println("The winner is : " + winner);

        scanner.close();

    }
}

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Wordle game = new Wordle();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to my Wordle! You have 6 attempts to guess a 5-letter word");

        while (!game.gameOver()) {
            System.out.print("Enter your guess: ");
            String guess = scanner.next().toLowerCase();
            String result = game.makeGuess(guess);
            System.out.println(result);
        }
        scanner.close();
    }
}
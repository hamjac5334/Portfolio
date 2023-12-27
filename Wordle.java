import java.util.Random;
public class Wordle {
    private static final int totalAttempts = 6;
    private int attemptsLeft;
    private String wordleWord;
    private char[] userGuessWord;
    private static final String[] words = {
            "pears", "chain", "trail", "train", "nails", "water", "phone",
            "stacks", "Javas", "kites", "chair", "reach", "angel", "breed", "crown", "drown",
            "codes"};
    private String randomWord(){
        Random random = new Random();
        int index = random.nextInt(words.length);
        return words [index];
    }
    public Wordle() {
        wordleWord = randomWord();
        attemptsLeft = totalAttempts;
        userGuessWord = new char[5];
        for (int i = 0; i < 5; i++) {
            userGuessWord[i] = ' ';
        }
    }
    private boolean wordGuessed() {
        for (int i = 0; i < 5; i++) {
            if (userGuessWord[i] == ' ') {
                return false;
            }
        }
        return true;
    }
    public boolean gameOver() {
        return attemptsLeft <= 0 || wordGuessed();
    }

    public String makeGuess(String guess) {
        if (gameOver()== true) {
            
            return "The game is over!!!!";
        }

        if (guess.length() != 5) {
            return "Your guess must be a 5-letter word.";
        }
        attemptsLeft--;
        char[] guessChars = guess.toCharArray();
        StringBuilder wordHints =new StringBuilder();


        for (int i = 0; i <5; i++) {
            char c = guessChars[i];
            char targetChar = wordleWord.charAt(i);

            if (c ==targetChar) {
                wordHints.append(Character.toUpperCase(c));
                userGuessWord[i] = c;
            } else if (wordleWord.indexOf(c) !=-1) {
                wordHints.append(Character.toLowerCase(c));
            } else {

                wordHints.append('x');
            }
        }

        if (gameOver() == true) {
            if (wordGuessed()==true) {
                return "Congratulations!!! You guessed the word: " + wordleWord;
            } else {
                return "Sorry, you're out of tries. The word was: " + wordleWord;
            }
        } else {
            return "Guess: " + guess + "  Hints: " + wordHints.toString() + "  Attempts left: " + attemptsLeft;
        }
    }
}

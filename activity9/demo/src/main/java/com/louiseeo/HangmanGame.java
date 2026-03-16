package com.louiseeo;

/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HangmanGame {
    static Scanner sc = new Scanner(System.in); // Static scanner for accesing of all methods

    public static void main(String[] args) {

        String[] playerNames = new String[50]; // for storing number of players
        int[] playerScores = new int[50]; // for storing player scores
        int playerCount = 0; // for storing number of players that will play

        int maxIncorrect = 8; // max number of wrong guesses
        int maxScore = 30; // max points

        boolean continueGame = true;

        while (continueGame) {
            // get the name of the player
            String name = getPlayerName();
            playerNames[playerCount] = name;

            // make user choose what difficulty to play
            System.out.println("""
                    Difficulty:
                        [1] Easy
                        [2] Medium
                        [3] Hard
                    """);
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            while (true) {
                String filename;
                switch (choice) {
                    case 1:
                        filename = "easy.txt";
                        break;
                    case 2:
                        filename = "medium.txt";
                        break;
                    case 3:
                        filename = "hard.txt";
                        break;
                    default:
                        System.out.println("Invalid choice! Pick from 1 to 3.");
                        break;
                }
            }
            // play the game and get the score
            int score = playGame(loadWordbank(name), maxIncorrect, maxScore);
            playerScores[playerCount] = score;
            playerCount++;

            // ask if another player wants to play
            continueGame = anotherPlayer();
        }
        // for displaying the leaderboard
        displayLeaderboard(playerNames, playerScores, playerCount);
    }

    // Get the name of the player
    public static String getPlayerName() {
        System.out.print("Player Name: ");
        return sc.nextLine();
    }

    // Pick a random word from the wordbank array
    public static String selectRandomWord(List<String> words) {
        int indexWord = (int) (Math.random() * words.size()); // A random word is picked from the wordbank
        return words[indexWord];
    }

    // Make the hiddenWord word change into asterisk
    public static String initializeHiddenWord(String word) {
        String hiddenWord = "";
        for (int i = 0; i < word.length(); i++) {
            hiddenWord += "*"; // Picked word is masked with '*'
        }
        return hiddenWord;
    }

    // The game play
    public static int playGame(List<String> wordBank, int maxIncorrect, int maxScore) {
        // Calls the selectRandomWord to get a random word
        String word = selectRandomWord(wordBank);

        // Initializes the hiddenWord word with asterisks
        String hiddenWord = initializeHiddenWord(word);

        // Array to track guessed letters
        char[] guessedLetters = new char[26];
        int guessCount = 0;

        int incorrectCount = 0;
        int score = 0;
        boolean gameOver = false;

        // Game loop
        while (!gameOver) {
            System.out.print("\nEnter a letter in word " + hiddenWord + " > ");
            char guess = getLetterGuess();

            // Check if letter is already guessed
            if (letterAlreadyGuessed(guess, guessedLetters, guessCount)) {
                System.out.println(guess + " is already in the word");
                continue;
            }

            // Adds to the guessed letter
            guessedLetters[guessCount] = guess;
            guessCount++;

            boolean correct = ifGuessCorrect(word, guess); // Checks if guess is correct

            if (correct) {
                hiddenWord = updateHiddenWord(word, hiddenWord, guess); // Updates hiddenWord word to reveal the letter
                                                                        // guessed
                score = awardPointForCorectLetter(true, score); // A point is made if user guessed a letter correct
            } else {
                System.out.println(guess + " is not in the word"); // Prints if the guess is incorrect
                incorrectCount++;
            }

            // Checks if the word is fully guessed
            if (isWordFullyGuessed(hiddenWord)) {
                System.out.println("\nCongratulations! You guessed the word " + word + ".");
                score = calculateScore(true, maxScore, incorrectCount); // Calculate the final score
                gameOver = true;
            }

            // Checks if user already reached max number of guesses
            if (incorrectCount >= maxIncorrect) {
                System.out.println("GAME OVER");
                System.out.println("\nThe word is " + word + ".");
                gameOver = true;
            }
        }
        return score;
    }

    // Get the guess letter of user
    public static char getLetterGuess() {

        while (true) {
            char guess = sc.next().charAt(0);
            sc.nextLine();
            guess = Character.toLowerCase(guess); // makes letter to lowercase if capital letter is entered

            // Checks if user entered an invalid letter
            if (!Character.isLetter(guess)) {
                System.out.print("Invalid input! Enter a letter. > ");
            } else {
                return guess; // If input is correct
            }
        }
    }

    // Check if guess is in the word
    public static boolean ifGuessCorrect(String word, char guess) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                return true; // returns true if letter is correct
            }
        }

        return false; // returns false if not
    }

    // Checks if the letter is already given
    public static boolean letterAlreadyGuessed(char guess, char[] guessedLetters, int count) {
        for (int i = 0; i < count; i++) {
            if (guessedLetters[i] == guess) {
                return true; // if a letter is entered again
            }
        }
        return false; // if not entered already
    }

    // Update hiddenWord word to reveal guessed letter
    public static String updateHiddenWord(String word, String hiddenWord, char guess) {
        String newHiddenWord = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                newHiddenWord += guess; // Reveals the guessed letter
            } else {
                newHiddenWord += hiddenWord.charAt(i); // Keeps what was before
            }
        }
        return newHiddenWord;
    }

    // Check if word is fully guessed
    public static boolean isWordFullyGuessed(String hiddenWord) {
        for (int i = 0; i < hiddenWord.length(); i++) {
            if (hiddenWord.charAt(i) == '*') {
                return false; // If word is not fully guessed
            }
        }
        return true; // If the word is complete
    }

    // Award point for correct letter
    public static int awardPointForCorectLetter(boolean correct, int currentScore) {
        if (correct) {
            return currentScore + 1; // Adds 1 point if a letter at guessed
        }
        return currentScore;
    }

    // Calculate final score
    public static int calculateScore(boolean wholeWordGuessed, int maxScore, int incorrectCount) {
        if (wholeWordGuessed) {
            int finalScore = maxScore - incorrectCount; // Subtracts incorrect guesses to get final score
            return finalScore;
        }
        return 0;
    }

    // Ask if another player wants to play
    public static boolean anotherPlayer() {
        // Checks if the user entered either y or n
        while (true) {
            System.out.print("\nAnother Player? Enter y or n: ");
            char answer = sc.next().charAt(0); // Reads the entered answer
            sc.nextLine();
            if (answer == 'y' || answer == 'Y')
                return true;
            else if (answer == 'n' || answer == 'N')
                return false;
            else
                System.out.println("Invalid input! Enter y or n only.");
        }
    }

    // Display the arranged leaderboard from highest to lowest
    public static void displayLeaderboard(String[] names, int[] scores, int count) {
        System.out.println("\n===== LEADERBOARD =====");

        // Arrange from highest to lowest score
        for (int i = 0; i < count - 1; i++) { // Loop for frequency of sorting process
            for (int j = 0; j < count - i - 1; j++) { // Compares 2 player each time
                if (scores[j] < scores[j + 1]) { // Check ifcurrent players score is higher than the other one
                    // Swap scores
                    int tempScore = scores[j]; // Stores current score temporarily
                    scores[j] = scores[j + 1]; // Moves the higher score forward
                    scores[j + 1] = tempScore; // Put the lower score in next position

                    // Swap names
                    String tempName = names[j]; // Stores current name temporarily
                    names[j] = names[j + 1]; // Moves the name with higher score
                    names[j + 1] = tempName; // Put other name in next position
                }
            }
        }

        // Display sorted leaderboard
        for (int i = 0; i < count; i++) {
            System.out.println(names[i] + " - " + scores[i] + " points");
        }
    }

    public static List<String> loadWordbank(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!words.isEmpty()) {
            Random rand = new Random();

        }
    }
}

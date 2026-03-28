package com.louiseeo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.louiseeo.model.Player;

public class GameService {
    static int maxIncorrect;
    static int maxScore;

    /**
     * Plays a single round of the Hangman game for the given player.
     *
     * @param p The player who is playing the round
     * @param in  : the input stream to read client responses
     * @param out : the output stream to send messages to the client
     * @throws IOException
     */
    public static void playRound(Player p, BufferedReader in, PrintWriter out) throws IOException {
        // Make user choose the difficulty they want
        String filename = chooseDifficulty(in, out);

        // play the game and get the score
        int score = playGame(FileService.loadWordbank(filename, out), maxIncorrect, maxScore, in, out);
        p.setScore(p.getScore() + score);

        out.println("Total score: " + p.getScore());
    }

    /**
     * Lets the player choose a difficulty and sets maxScore and maxIncorrect
     * accordingly.
     *
     * @param in  : the input stream to read the client's choice
     * @param out : the output stream to send the difficulty menu to the client
     * @return The filename of the word bank for the chosen difficulty
     * @throws IOException
     */
    public static String chooseDifficulty(BufferedReader in, PrintWriter out) throws IOException {
        String filename;

        // make user choose what difficulty to play
        out.println("""
                Difficulty:
                    [1] Easy
                    [2] Medium
                    [3] Hard
                """);

        filename = "";
        boolean valid = false;
        int choice2 = 0;

        while (!valid) {
            out.println("Choice: ");
            try {
                choice2 = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                out.println("Invalid input! Pick from 1 to 3.\n");
                continue;
            }
            switch (choice2) {
                case 1:
                    filename = "data/easy.txt";
                    maxScore = 20;
                    maxIncorrect = 10;
                    valid = true;
                    break;
                case 2:
                    filename = "data/medium.txt";
                    maxScore = 40;
                    maxIncorrect = 7;
                    valid = true;
                    break;
                case 3:
                    filename = "data/hard.txt";
                    maxScore = 60;
                    maxIncorrect = 5;
                    valid = true;
                    break;
                default:
                    out.println("Invalid input! Pick from 1 to 3.\n");
            }
        }
        return filename;
    }

    /**
     * Selects a random word from the chosen txt file difficulty.
     * 
     * @param words : the list of possible words
     * @param out   : the output stream to send error messages to the client
     * @return a randomly selected word from the list
     */
    public static String selectRandomWord(List<String> words, PrintWriter out) {
        if (words.isEmpty()) {
            out.println("Wordbank is empty or file not found!");
            return null;
        }

        int indexWord = (int) (Math.random() * words.size());
        return words.get(indexWord);
    }

    /**
     * Initializes the hidden version of a word by hiding it using *
     * 
     * @param word : the word to be hidden
     * @return the hidden word
     */
    public static String initializeHiddenWord(String word) {
        String hiddenWord = "";
        for (int i = 0; i < word.length(); i++) {
            hiddenWord += "*"; // Picked word is masked with '*'
        }
        return hiddenWord;
    }

    /**
     * Facilitates the gameplay
     *
     * @param wordBank     : the list of words for the round
     * @param maxIncorrect : maximum number of incorrect guesses allowed
     * @param maxScore     : maximum points possible for the round
     * @param in         : the input stream to read client guesses
     * @param out          : the output stream to send game state to the client
     * @return the score earned in this round
     * @throws IOException
     */
    public static int playGame(List<String> wordBank, int maxIncorrect, int maxScore, BufferedReader in, PrintWriter out)
            throws IOException {

        // Calls the selectRandomWord to get a random word
        String word = selectRandomWord(wordBank, out);

        if (word == null) {
            return 0;
        }

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
            int remainingGuesses = maxIncorrect - incorrectCount;
            out.println("\nGuesses left: " + remainingGuesses);
            out.println("Enter a letter in word " + hiddenWord);
            char guess = getLetterGuess(in, out);

            // Check if letter is already guessed
            if (letterAlreadyGuessed(guess, guessedLetters, guessCount)) {
                out.println(guess + " is already guessed in the word");
                continue;
            }
            // Adds to the guessed letter
            if (guessCount < guessedLetters.length) {
                guessedLetters[guessCount] = guess;
                guessCount++;
            } else {
                out.println("Maximum guesses reached!");
            }

            boolean correct = ifGuessCorrect(word, guess); // Checks if guess is correct

            if (correct) {
                hiddenWord = updateHiddenWord(word, hiddenWord, guess); // Updates hiddenWord word to reveal the letter
            } else {
                out.println(guess + " is not in the word"); // Prints if the guess is incorrect
                incorrectCount++;
            }

            // Checks if the word is fully guessed
            if (isWordFullyGuessed(hiddenWord)) {
                out.println("\nCongratulations! You guessed the word " + word + ".");
                score = calculateScore(true, maxScore, incorrectCount); // Calculate the final score
                gameOver = true;
            }

            // Checks if user already reached max number of guesses
            if (incorrectCount >= maxIncorrect) {
                out.println("GAME OVER");
                out.println("\nThe word is " + word + ".");
                gameOver = true;
            }
        }
        return score;
    }

    /**
     * Prompts the player to guess a single letter.
     *
     * @param in  : the input stream to read the client's guess
     * @param out : the output stream to send prompts and error messages to the client
     * @return the letter guessed by the player
     * @throws IOException
     */
    public static char getLetterGuess(BufferedReader in, PrintWriter out) throws IOException {

        while (true) {
            String input = in.readLine();
            if (input == null || input.isEmpty()) {
                out.println("\nPlease enter a letter!");
                continue;
            }

            char guess = Character.toLowerCase(input.charAt(0)); // makes letter to lowercase if capital letter entered

            // Checks if user entered an invalid letter
            if (!Character.isLetter(guess)) {
                out.println("\nInvalid input! Enter a letter.");
            } else {
                return guess; // If input is correct
            }
        }
    }

    /**
     * Checks if a guessed letter exists in the word.
     *
     * @param word  : the word to check against
     * @param guess : the letter guessed by the player
     * @return true if the guess is in the word, false otherwise
     */
    public static boolean ifGuessCorrect(String word, char guess) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                return true; // returns true if letter is correct
            }
        }
        return false; // returns false if not
    }

    /**
     * Checks whether a guessed letter has already been guessed before.
     *
     * @param guess          : the letter guessed
     * @param guessedLetters : array of letters already guessed
     * @param count          : number of valid guessed letters stored in the array
     * @return true if the letter has already been guessed, false otherwise
     */
    public static boolean letterAlreadyGuessed(char guess, char[] guessedLetters, int count) {
        for (int i = 0; i < count; i++) {
            if (guessedLetters[i] == guess) {
                return true; // if a letter is entered again
            }
        }
        return false; // if not entered already
    }

    /**
     * Updates the hidden word to reveal the guessed letter(s) if correct.
     *
     * @param word       : the original word
     * @param hiddenWord : the current state of the hidden word
     * @param guess      : the guessed letter
     * @return the updated hidden word with guessed letters revealed
     */
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

    /**
     * Checks if the player has fully guessed the word.
     *
     * @param hiddenWord : the current hidden word
     * @return true if no '*' remain, false otherwise
     */
    public static boolean isWordFullyGuessed(String hiddenWord) {
        for (int i = 0; i < hiddenWord.length(); i++) {
            if (hiddenWord.charAt(i) == '*') {
                return false; // If word is not fully guessed
            }
        }
        return true; // If the word is complete
    }

    /**
     * Calculates the final score for the player in a round.
     *
     * @param wholeWordGuessed : true if player guessed the whole word
     * @param incorrectCount   : number of incorrect guesses made
     * @param maxScore         : maximum points possible for the round
     * @return the final calculated score
     */
    public static int calculateScore(boolean wholeWordGuessed, int maxScore, int incorrectCount) {
        if (wholeWordGuessed) {
            int finalScore = maxScore - incorrectCount; // Subtracts incorrect guesses to get final score
            return finalScore;
        }
        return 0;
    }
}
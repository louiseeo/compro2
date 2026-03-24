package com.louiseeo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.louiseeo.model.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Type;

/**
 * Represents the Hangman game.
 * Handles player login, rounds, scoring, and leaderboard.
 * 
 * @author louiseeo
 */
public class HangmanGame {
    // Static fields for easy access of all methods
    static Scanner sc = new Scanner(System.in);
    static List<Player> players = new ArrayList<>();
    static int maxScore;
    static int maxIncorrect;

    /**
     * Main method. Starts the game loop where players can log in,
     * play rounds, and view the leaderboard.
     */
    public static void main(String[] args) {
        players = loadPlayers("data/leaderboard.json");

        boolean continueGame = true;

        while (continueGame) {
            // make user either login or create new user
            Player p = playerLogin();

            // play the game
            playRound(p);

            // save players
            savePlayers("data/leaderboard.json", players);

            // ask if another player wants to play
            continueGame = anotherPlayer();
        }
        // for displaying the leaderboard
        displayLeaderboard(players);
    }

    /**
     * Plays a single round of the Hangman game for the given player.
     *
     * @param p The player who is playing the round
     */
    public static void playRound(Player p) {
        // Make user choose the difficulty they want
        String filename = chooseDifficulty();

        // play the game and get the score
        int score = playGame(loadWordbank(filename), maxIncorrect, maxScore);
        p.setScore(p.getScore() + score);

        System.out.println("Total score: " + p.getScore());
    }

    /**
     * Handles player login or creation of a new player.
     * Loops until a valid player is returned.
     *
     * @return The Player object representing the logged-in or newly created player
     */
    public static Player playerLogin() {
        // check if player would like to sign in
        System.out.println("""
                \nWelcome to Hangman Game!
                [1] Sign in
                [2] Sign up
                """);

        Player p = null;
        int choice1 = 0;
        boolean val = false;

        while (!val) {
            System.out.print("Choice: ");
            try {
                choice1 = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter 1 or 2.\n");
                sc.nextLine();
                continue;
            }

            if (choice1 == 1) {
                while (true) {
                    String name = getPlayerName().trim();
                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty.\n");
                        continue;
                    }
                    p = findPlayer(players, name);
                    if (p != null) {
                        System.out.print("Enter password: ");
                        String pw = sc.nextLine();

                        if (!pw.equals(p.getPassword())) {
                            System.out.println("Incorrect password! Try again.\n");
                            continue;
                        }
                        System.out.println("Welcome back, " + p.getName() + "!\n");
                        val = true;
                        break;
                    } else {
                        System.out.println("Player not found. Try again.\n");
                    }
                }
            } else if (choice1 == 2) {
                while (true) {
                    String name = getPlayerName().trim();

                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty.\n");
                        continue;
                    }
                    p = findPlayer(players, name);
                    if (p == null) {
                        System.out.print("Set password: ");
                        String pw = sc.nextLine();

                        if (pw.isEmpty()) {
                            System.out.println("Password cannot be empty.\n");
                        }
                        System.out.println("\nCreating new account...");
                        p = new Player(name, 0, pw);
                        players.add(p);
                        System.out.println("Welcome to the game, " + p.getName() + "!\n");
                        val = true;
                        break; // exit inner loop
                    } else {
                        System.out.println("The name already exists. Try another.\n");
                    }
                }
            } else {
                System.out.println("Invalid input. Enter 1 or 2.\n");
            }
        }
        return p;
    }

    /**
     * Lets the player choose a difficulty and sets maxScore and maxIncorrect
     * accordingly.
     *
     * @return The filename of the word bank for the chosen difficulty
     */
    public static String chooseDifficulty() {
        // make user choose what difficulty to play
        System.out.println("""
                Difficulty:
                    [1] Easy
                    [2] Medium
                    [3] Hard
                """);
        System.out.print("Choice: ");
        String filename = "";
        boolean valid = false;
        int choice2 = 0;

        while (!valid) {
            try {
                choice2 = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice! Pick from 1 to 3.\n");
                sc.nextLine();
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
                    System.out.println("Invalid choice! Pick from 1 to 3.");
            }
        }
        return filename;
    }

    /**
     * Prompts user to enter their name.
     * 
     * @return the name entered by the user
     */
    public static String getPlayerName() {
        System.out.print("Player Name: ");
        return sc.nextLine();
    }

    /**
     * Selects a random word from the chosen txt file difficulty.
     * 
     * @param words : the list of possible words
     * @return a randomly selected word from the list
     */
    public static String selectRandomWord(List<String> words) {
        if (words.isEmpty()) {
            System.out.println("Wordbank is empty or file not found!");
            return null;
        }

        int indexWord = (int) (Math.random() * words.size());
        return words.get(indexWord);
    }

    /**
     * Initializes the hidden version of a word by hiding it using *
     * 
     * @param word
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
     * @return the score earned in this round
     */
    public static int playGame(List<String> wordBank, int maxIncorrect, int maxScore) {

        // Calls the selectRandomWord to get a random word
        String word = selectRandomWord(wordBank);

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
            System.out.print("\nEnter a letter in word " + hiddenWord + " > ");
            char guess = getLetterGuess();

            // Check if letter is already guessed
            if (letterAlreadyGuessed(guess, guessedLetters, guessCount)) {
                System.out.println(guess + " is already guessed in the word");
                continue;
            }
            // Adds to the guessed letter
            if (guessCount < guessedLetters.length) {
                guessedLetters[guessCount] = guess;
                guessCount++;
            } else {
                System.out.println("Maximum guesses reached!");
            }

            boolean correct = ifGuessCorrect(word, guess); // Checks if guess is correct

            if (correct) {
                hiddenWord = updateHiddenWord(word, hiddenWord, guess); // Updates hiddenWord word to reveal the letter
                score++;
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

    /**
     * Prompts the player to guess a single letter.
     *
     * @return the letter guessed by the player
     */
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
     * @return true if no underscores remain, false otherwise
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
     * Awards points for a correct letter guess.
     *
     * @param correct      : whether the letter guessed was correct
     * @param currentScore : the player's current score
     * @return the updated score after applying points for the guess
     */
    public static int awardPointForCorrectLetter(boolean correct, int currentScore) {
        if (correct) {
            return currentScore + 1; // Adds 1 point if a letter at guessed
        }
        return currentScore;
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

    /**
     * Asks the player if another player wants to play another round.
     *
     * @return true if another player wants to play, false otherwise
     */
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

    /**
     * Displays the arranged leaderboard from highest to lowest score.
     *
     * @param players : the list of all players to display
     */
    public static void displayLeaderboard(List<Player> players) {
        System.out.println("\n===== LEADERBOARD =====");

        // Bubble sort style, just like your original code
        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = 0; j < players.size() - i - 1; j++) {
                if (players.get(j).getScore() < players.get(j + 1).getScore()) {
                    // Swap players
                    Player temp = players.get(j);
                    players.set(j, players.get(j + 1));
                    players.set(j + 1, temp);
                }
            }
        }

        // Display sorted leaderboard
        for (Player p : players) {
            System.out.println(p.getName() + " - " + p.getScore() + " points");
        }
    }

    /**
     * Loads the word bank from the specified file.
     *
     * @param filename : the path to the file containing words
     * @return a list of words loaded from the file
     */
    public static List<String> loadWordbank(String filename) {
        List<String> words = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Error: File not found -> " + filename);
            return words; // return empty list safely
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + filename);
        }
        return words;
    }

    /**
     * Searches for a player by name in the list of players.
     *
     * @param players : the list of all players
     * @param name    : the name of the player to search for
     * @return the Player object if found, null otherwise
     */
    public static Player findPlayer(List<Player> players, String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Loads the list of players from a JSON file.
     *
     * @param filename : the path to the JSON file containing player data
     * @return a list of players loaded from the file
     */
    public static List<Player> loadPlayers(String filename) {
        List<Player> playerList = new ArrayList<>();

        try (FileReader fr = new FileReader(filename)) {
            Gson gson = new Gson();

            Type playerListType = new TypeToken<List<Player>>() {
            }.getType();

            List<Player> pl = gson.fromJson(fr, playerListType);

            if (pl != null) {
                playerList = pl;
            }

        } catch (IOException e) {
            System.out.println("Error loading file: " + filename);
        }
        return playerList;
    }

    /**
     * Saves the list of players to JSON file.
     * 
     * @param filename : the path to the JSON file where player data will be saved
     * @param players  : the list of players to be saved
     */
    public static void savePlayers(String filename, List<Player> players) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(players, fw);
            System.out.println("\nPlayer data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + filename);
        }
    }
}

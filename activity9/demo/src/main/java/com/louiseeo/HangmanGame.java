package com.louiseeo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.louiseeo.model.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.lang.reflect.Type;

public class HangmanGame {
    static Scanner sc = new Scanner(System.in); // Static scanner for accesing of all methods

    static List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        players = loadPlayers("activity9/demo/data/leaderboard.json");

        int playerCount = 0; // for storing number of players that will play

        int maxIncorrect = 8; // max number of wrong guesses
        int maxScore = 30; // max points

        boolean continueGame = true;

        while (continueGame) {
            // check if player would like to sign in
            System.out.println("""
                    \nWelcome to Hangman game!
                    [1] Sign in
                    [2] New Player
                    """);

            boolean val = false;
            Player p = null;

            while (!val) {
                System.out.print("Choice: ");
                int choice1 = sc.nextInt();
                sc.nextLine();

                if (choice1 == 1) {
                    String name = getPlayerName();
                    p = findPlayer(players, name);
                    if (p != null) {
                        System.out.println("Welcome back, " + p.getName() + "!\n");
                        val = true;
                    } else {
                        System.out.println("Player not found. Try again.\n");
                    }
                } else if (choice1 == 2) {
                    boolean exist = false;
                    while (!exist) {
                        String name = getPlayerName();
                        p = findPlayer(players, name);

                        if (p == null) {
                            System.out.println("Creating new account...\n");
                            p = new Player(name, 0);
                            players.add(p);
                            val = true;
                            exist = true; // exit inner loop
                        } else {
                            System.out.println("The name already exists. Try another.\n");
                            // loop continues, asks for another name
                        }
                    }

                }

                else {
                    System.out.println("Invalid choice.\n");
                }
            }

            // make user choose what difficulty to play
            System.out.println("""
                    Difficulty:
                        [1] Easy
                        [2] Medium
                        [3] Hard
                    """);

            String filename = "";
            boolean valid = false;
            while (!valid) {
                System.out.print("Choice: ");
                int choice2 = sc.nextInt();
                sc.nextLine();

                switch (choice2) {
                    case 1:
                        filename = "activity9/demo/data/easy.txt";
                        valid = true;
                        break;
                    case 2:
                        filename = "activity9/demo/data/medium.txt";
                        valid = true;
                        break;
                    case 3:
                        filename = "activity9/demo/data/hard.txt";
                        valid = true;
                        break;
                    default:
                        System.out.println("Invalid choice2! Pick from 1 to 3.");
                }
            }

            // play the game and get the score
            int score = playGame(loadWordbank(filename), maxIncorrect, maxScore);
            p.setScore(p.getScore() + score);
            playerCount++;

            // ask if another player wants to play
            continueGame = anotherPlayer();
        }

        // for displaying the leaderboard
        displayLeaderboard(players);
        savePlayers("activity9/demo/data/leaderboard.json", players);
    }

    // Get the name of the player
    public static String getPlayerName() {
        System.out.print("Player Name: ");
        return sc.nextLine();
    }

    // Pick a random word from the wordbank array
    public static String selectRandomWord(List<String> words) {
        if (words.isEmpty()) {
            System.out.println("Wordbank is empty or file not found!");
            return null;
        }

        int indexWord = (int) (Math.random() * words.size());
        return words.get(indexWord);
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
                System.out.println(guess + " is already in the word");
                continue;
            }

            // Adds to the guessed letter
            guessedLetters[guessCount] = guess;
            guessCount++;

            boolean correct = ifGuessCorrect(word, guess); // Checks if guess is correct

            if (correct) {
                hiddenWord = updateHiddenWord(word, hiddenWord, guess); // Updates hiddenWord word to reveal the letter                                            
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
        return words;
    }

    public static Player findPlayer(List<Player> players, String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public static List<Player> loadPlayers(String filename) {
        List<Player> playerList = new ArrayList<>();

        try (FileReader fr = new FileReader(filename)) {
            Gson gson = new Gson();

            Type playerListType = new TypeToken<List<Player>>() {}.getType();

            List<Player> pl = gson.fromJson(fr, playerListType);

            if (pl != null) {
                playerList = pl;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    public static void savePlayers(String filename, List<Player> players) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(players, fw);
            System.out.println("\nPlayers saved to JSON!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

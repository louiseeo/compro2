package com.louiseeo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.louiseeo.model.Player;

public class PlayerService {
    /**
     * Handles player login or creation of a new player.
     * Loops until a valid player is returned.
     *
     * @param players : list of all players
     * @param in      : input stream to read client responses
     * @param out     : output stream to send messages to the client
     * @return The Player object representing the logged-in or newly created player
     * @throws IOException
     */

    public static Player playerLogin(List<Player> players, BufferedReader in, PrintWriter out) throws IOException {
        // check if player would like to sign in
        out.println("""
                \nWelcome to Hangman Game!
                [1] Sign in
                [2] Sign up
                """);

        Player p = null;
        int choice1 = 0;
        boolean val = false;

        while (!val) {
            out.println("Choice: ");
            try {
                choice1 = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                out.println("Invalid input. Enter 1 or 2.\n");
                continue;
            }

            if (choice1 == 1) {
                while (true) {
                    String name = getPlayerName(in, out).trim();
                    if (name.isEmpty()) {
                        out.println("Name cannot be empty.\n");
                        continue;
                    }
                    p = findPlayer(players, name);
                    if (p != null) {
                        out.println("Enter password: ");
                        String pw = in.readLine();

                        if (!pw.equals(p.getPassword())) {
                            out.println("Incorrect password! Try again.\n");
                            continue;
                        }
                        out.println("Welcome back, " + p.getName() + "!\n");
                        val = true;
                        break;
                    } else {
                        out.println("Player not found. Try again.\n");
                    }
                }
            } else if (choice1 == 2) {
                while (true) {
                    String name = getPlayerName(in, out).trim();

                    if (name.isEmpty()) {
                        out.println("Name cannot be empty.\n");
                        continue;
                    }
                    p = findPlayer(players, name);
                    if (p == null) {
                        out.println("Set password: ");
                        String pw = in.readLine();

                        if (pw.isEmpty()) {
                            out.println("Password cannot be empty.\n");
                        }
                        out.println("\nCreating new account...");
                        p = new Player(name, 0, pw);
                        players.add(p);
                        out.println("Welcome to the game, " + p.getName() + "!\n");
                        val = true;
                        break; // exit inner loop
                    } else {
                        out.println("The name already exists. Try another.\n");
                    }
                }
            } else {
                out.println("Invalid input. Enter 1 or 2.\n");
            }
        }
        return p;
    }

    /**
     * Prompts user to enter their name.
     * 
     * @param in  : the input stream to read the client's response
     * @param out : the output stream to send the prompt to the client
     * @return the name entered by the user
     * @throws IOException
     */
    public static String getPlayerName(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Player Name: ");
        return in.readLine();
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
     * Displays the arranged leaderboard from highest to lowest score.
     *
     * @param players : the list of all players to display
     * * @param out   : the output stream to send the leaderboard to the client
     */
    public static void displayLeaderboard(List<Player> players, PrintWriter out) {
        out.println("\n===== LEADERBOARD =====");

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
            out.println(p.getName() + " - " + p.getScore() + " points");
        }
    }

    /**
     * Asks the player if another player wants to play another round.
     *
     * @param in  : the input stream to read the client's response
     * @param out : the output stream to send the prompt to the client
     * @return true if another player wants to play, false otherwise
     * @throws IOException
     */
    public static boolean anotherPlayer(BufferedReader in, PrintWriter out) throws IOException {
        while (true) {
            out.println("\nAnother Player? (y/n): ");
            String input = in.readLine();

            // Handle client disconnection or empty input
            if (input == null)
                return false;
            input = input.trim().toLowerCase();

            if (input.isEmpty())
                continue;

            char answer = input.charAt(0);
            if (answer == 'y')
                return true;
            if (answer == 'n')
                return false;

            out.println("Invalid input! Please enter 'y' or 'n'.");
        }
    }
}
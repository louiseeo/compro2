package com.louiseeo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.louiseeo.model.Account;

public class ClientService {

    /**
     * Handles player login or creation of a new player.
     * Loops until a valid player is returned.
     *
     * @param accounts : list of all accounts
     * @param in       : input stream to read client responses
     * @param out      : output stream to send messages to the client
     * @return The Player object representing the logged-in or newly created player
     * @throws IOException
     */

    public static Account playerLogin(List<Account> accounts, BufferedReader in, PrintWriter out) throws IOException {
        // check if player would like to sign in
        out.println("""
                \nWelcome to the ROCK, PAPER, SCISSOR Game!
                [1] Sign in
                [2] Sign up
                """);

        Account a = null;
        boolean val = false;

        while (!val) {
            out.println("Choice: ");
            int choice1;
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
                    a = findPlayer(accounts, name);
                    if (a != null) {
                        out.println("Enter password: ");
                        String pw = in.readLine();

                        if (!pw.equals(a.getPassword())) {
                            out.println("Incorrect password! Try again.\n");
                            continue;
                        }
                        out.println("Welcome back, " + a.getUsername() + "!\n");
                        out.println("Waiting for another player...");
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
                    a = findPlayer(accounts, name);
                    if (a == null) {
                        out.println("Set password: ");
                        String pw = in.readLine();

                        if (pw.isEmpty()) {
                            out.println("Password cannot be empty.\n");
                        }
                        out.println("\nCreating new account...");
                        a = new Account(name, pw);
                        accounts.add(a);
                        out.println("Welcome to the game, " + a.getUsername() + "!\n");
                        out.println("Waiting for another player...\n");
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
        return a;
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
     * Searches for a player by name in the list of accounts.
     *
     * @param accounts : the list of all accounts
     * @param name     : the name of the player to search for
     * @return the Player object if found, null otherwise
     */
    public static Account findPlayer(List<Account> accounts, String name) {
        for (Account a : accounts) {
            if (a.getUsername().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Displays the arranged leaderboard from highest to lowest score.
     *
     * @param accounts : the list of all accounts to display
     * @param out : the output stream to send the leaderboard to
     *                 the client
     */
    public static void displayLeaderboard(List<Account> accounts, PrintWriter out) {
        out.println("\n======= MATCH OVER =======");

        // Bubble sort style, just like your original code
        for (int i = 0; i < accounts.size() - 1; i++) {
            for (int j = 0; j < accounts.size() - i - 1; j++) {
                if (accounts.get(j).getWins() < accounts.get(j + 1).getWins()) {
                    // Swap accounts
                    Account temp = accounts.get(j);
                    accounts.set(j, accounts.get(j + 1));
                    accounts.set(j + 1, temp);
                }
            }
        }

        // Display sorted leaderboard
        for (Account a : accounts) {
            out.println(a.getUsername() + " - " + a.getWins() + " wins | " + a.getLosses() + " losses");
        }
    }

    /**
     * Asks the player to enter their RPS choice.
     * Validates input is 0, 1, or 2.
     *
     * @param in  : input stream to read client response
     * @param out : output stream to send prompt to client
     * @return valid int choice (0, 1, or 2)
     * @throws IOException
     */
    public static int handleChoice(BufferedReader in, PrintWriter out) throws IOException {
        while (true) {
            out.println("Enter choice (0=Rock, 1=Paper, 2=Scissors): ");
            try {
                int choice = Integer.parseInt(in.readLine());
                if (choice >= 0 && choice <= 2)
                    return choice;
                out.println("Invalid input! Enter 0, 1, or 2.\n");
            } catch (NumberFormatException e) {
                out.println("Invalid input! Enter 0, 1, or 2.\n");
            }
        }
    }

}

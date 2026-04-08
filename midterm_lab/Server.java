package com.louiseeo;

import com.louiseeo.model.Account;
import com.louiseeo.model.GameMove;
import com.louiseeo.model.GameResult;
import com.louiseeo.model.GameSession;
import com.louiseeo.model.Player;
import com.louiseeo.service.ClientService;
import com.louiseeo.service.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the Rock Paper Scissors server.
 * Accepts two client connections, handles authentication,
 * runs a 10 round match, displays the leaderboard,
 * and saves account data persistently to JSON.
 *
 * @author louiseeo
 */
public class Server {
    static List<Account> accs = new ArrayList<>();

    /**
     * Main method. Starts the server, accepts two clients,
     * authenticates both, runs the game loop for 10 rounds,
     * displays leaderboard, and saves results.
     *
     */
    public static void main(String[] args) {
        int port = 8000;

        System.out.println("Waiting for players...");

        try (ServerSocket server = new ServerSocket(port)) {

            // Accept Player 1 to the server
            Socket p1Socket = server.accept();
            PrintWriter out1 = new PrintWriter(p1Socket.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(p1Socket.getInputStream()));
            System.out.println("Player 1 connected!");

            // Accept Player 2 to the server
            Socket p2Socket = server.accept();
            PrintWriter out2 = new PrintWriter(p2Socket.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(p2Socket.getInputStream()));
            System.out.println("Player 2 connected!");

            // Load JSON accounts 
            accs = FileService.loadAccounts("data/accounts.json");
            
            // Log players to the game
            Account a1 = ClientService.playerLogin(accs, in1, out1);
            Account a2 = ClientService.playerLogin(accs, in2, out2);

            // Create Player objects 
            Player p1 = new Player(a1.getUsername());
            Player p2 = new Player(a2.getUsername());

            // Create GameSession
            GameSession gs = new GameSession(p1, p2);

            // Notify both players that game is starting
            out1.println("\nBoth players connected! Game starting...");
            out2.println("\nBoth players connected! Game starting...");

            // Loop game to 10 rounds
            for (int round = 1; round <= 10; round++) {

                // State round number to both players
                out1.println("\n============== Round " + round + " of 10 ==============");
                out2.println("\n============== Round " + round + " of 10 ==============");

                // Get GameMove from both players
                GameMove m1 = ClientService.handleChoice(in1, out1);
                GameMove m2 = ClientService.handleChoice(in2, out2);

                // Set moves on Player objects
                p1.setCurrentMove(m1);
                p2.setCurrentMove(m2);

                // Determine round winner and update scores
                GameResult result = gs.determineWinner();

                // Send result to both players
                out1.println(gs.formatResult(result));
                out2.println(gs.formatResult(result));

                // Show current scores
                out1.println("Score -> " + p1.getName() + ": " + p1.getScore() + " | " + p2.getName() + ": " + p2.getScore());
                out2.println("Score -> " + p1.getName() + ": " + p1.getScore() + " | " + p2.getName() + ": " + p2.getScore());

                // Reset moves for next round
                gs.resetRound();
            }

            // Update accounts based on session scores
            a1.addWins(p1.getScore());
            a1.addLoses(p1.getScore());
            a2.addWins(p2.getScore());
            a2.addLoses(p2.getScore());

            // Announce match over
            out1.println("\n------------------- MATCH OVER ------------------");
            out2.println("\n------------------- MATCH OVER ------------------");

            // Display leaderboard to both players
            ClientService.displayLeaderboard(accs, out1);
            ClientService.displayLeaderboard(accs, out2);

            // Save updated accounts to JSON
            FileService.saveAccounts("data/accounts.json", accs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.louiseeo;

import com.louiseeo.model.Account;
import com.louiseeo.model.GameResult;
import com.louiseeo.model.Player;
import com.louiseeo.service.ClientService;
import com.louiseeo.service.FileService;
import com.louiseeo.service.GameService;

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
     */
    public static void main(String[] args) {
        int port = 8000;

        System.out.println("Waiting for players...");

        try (ServerSocket server = new ServerSocket(port)) {

            // Accept Player 1 and create streams
            Socket player1 = server.accept();
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            System.out.println("Player 1 connected!");

            // Load accounts and authenticate Player 1
            accs = FileService.loadAccounts("data/accounts.json", out1);
            Account a1 = ClientService.playerLogin(accs, in1, out1);

            // Accept Player 2 and create streams
            Socket player2 = server.accept();
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            System.out.println("Player 2 connected!");
            
            // Make Player 2 login
            Account a2 = ClientService.playerLogin(accs, in2, out2);

            // Create Player and GameService objects
            Player p1 = new Player(a1);
            Player p2 = new Player(a2);
            GameService gs = new GameService(p1, p2);

            // Notify both players game is starting
            out1.println("\nBoth players connected! Game starting...");
            out2.println("\nBoth players connected! Game starting...");

            // Game loop 
            for (int round = 1; round <= 10; round++) {
                // Announce round number to two players
                out1.println("\n=== Round " + round + " of 10 ===");
                out2.println("\n=== Round " + round + " of 10 ===");

                // get choice from both players
                int c1 = ClientService.handleChoice(in1, out1);
                int c2 = ClientService.handleChoice(in2, out2);

                // Set choices on players
                p1.setChoice(c1);
                p2.setChoice(c2);

                // Determine winner
                GameResult result = gs.determineWinner();

                // Send result to both players
                out1.println(gs.formatResult(result));
                out2.println(gs.formatResult(result));

                // Reset round
                gs.resetRound();

            }

            // Display leaderboard to both
            ClientService.displayLeaderboard(accs, out1);
            ClientService.displayLeaderboard(accs, out2);

            // Save accounts
            FileService.saveAccounts("data/accounts.json", accs, out1);
            FileService.saveAccounts("data/accounts.json", accs, out2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

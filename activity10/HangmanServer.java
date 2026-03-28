package com.louiseeo;

import com.louiseeo.model.Player;
import com.louiseeo.service.GameService;
import com.louiseeo.service.PlayerService;
import com.louiseeo.service.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents the Hangman game.
 * Handles player login, rounds, scoring, and leaderboard.
 * 
 * @author louiseeo
 */
public class HangmanServer {
    static List<Player> players = new ArrayList<>();

    /**
     * Main method. Starts the game loop where players can log in,
     * play rounds, and view the leaderboard.
     * 
     * @author louiseeo
     */
    public static void main(String[] args) {
        PrintWriter out;
        BufferedReader in;
        int port = 8000;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Waiting for client to be connected...");
            Socket client = server.accept();

            System.out.println("Client is connected...");

            try (PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

                out = writer;
                in = reader;

                players = FileService.loadPlayers("data/leaderboard.json", out);

                boolean continueGame = true;

                while (continueGame) {
                    // make user either login or create new user
                    Player p = PlayerService.playerLogin(players, in, out);

                    // play the game
                    GameService.playRound(p, in, out);

                    // save players
                    FileService.savePlayers("data/leaderboard.json", players, out);

                    // ask if another player wants to play
                    continueGame = PlayerService.anotherPlayer(in, out);
                }
                // for displaying the leaderboard
                PlayerService.displayLeaderboard(players, out);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
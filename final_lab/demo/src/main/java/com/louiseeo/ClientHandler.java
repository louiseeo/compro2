package com.louiseeo;

import java.io.*;
import java.net.Socket;

import com.louiseeo.enums.GamePhase;
import com.louiseeo.model.CitizenPlayer;
import com.louiseeo.model.Player;
import com.louiseeo.service.ChatService;
import com.louiseeo.service.GameService;
import com.louiseeo.service.VoteService;

/**
 * Handles communication between the server and a single client.
 * Each client runs in its own thread.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Player player;
    private int messageCount = 0;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Main execution for each client thread.
     */
    @Override
    public void run() {
        try {
            setupStreams(); // initialize I/O streams
            registerPlayer(); // register player

            // Add client to server list and notify others
            ChatService.addClient(this);

            handleChat(); // start receiving messages
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            cleanup(); // Always run when client disconnects
        }
    }

    /**
     * Sets up input and output streams for communication.
     */
    public void setupStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Prompts the client to enter a name.
     */
    public void registerPlayer() throws IOException {
        String username;
        do {
            // Note: Use sendMessage() or out.println() depending on your setup
            out.println("Enter your username:");
            username = in.readLine();
            if (username != null)
                username = username.trim();
        } while (username == null || username.isBlank());

        player = new CitizenPlayer(username, "");
        GameService.addPlayer(player);

        System.out.println(username + " joined the game.");
    }

    /**
     * Continuously listens for messages from the client
     * and broadcasts them to other users.
     */
    public void handleChat() throws IOException {
        String message;
        while ((message = in.readLine()) != null) {

            // PLAY AGAIN PHASE
            if (GameService.getCurrentPhase() == GamePhase.PLAY_AGAIN) {

                if (message.equalsIgnoreCase("yes")) {

                    VoteService.resetVotes();

                    GameService.startGame();
                } else if (message.equalsIgnoreCase("no")) {

                    sendMessage("Goodbye!");

                    socket.close();
                }

                continue;
            }
            // VOTING PHASE
            else if (GameService.getCurrentPhase() == GamePhase.VOTING) {

                VoteService.submitVote(this, message);
            }

            // PLAYER WANTS TO START VOTING
            else if (message.equalsIgnoreCase("vote")) {

                if (messageCount >= 3) {

                    VoteService.handleVote(this);

                } else {

                    sendMessage("You need at least 3 messages before voting!");
                }
            }

            // NORMAL CHAT
            else {

                messageCount++;

                String formatted = "[" + player.getUsername() + "]: " + message;

                ChatService.broadcast(formatted, this);

                sendMessage("[You]: " + message);
            }
        }

    }

    /**
     * Sends a message to this specific client.
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Cleans up resources and notifies others when client leaves.
     */
    public void cleanup() {
        try {
            if (player != null) {
                String leaveMsg = player.getUsername() + " left the game.";
                System.out.println(leaveMsg);
                ChatService.broadcastAll(leaveMsg);
                GameService.removePlayer(player);
            }

            ChatService.removeClient(this); // removes client in server list
            VoteService.resetVotes();
            // close socket connection
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            if (GameService.getPlayers().size() < 3) {
                GameService.setCurrentPhase(GamePhase.LOBBY);
                ChatService.broadcastAll("Not enough players. Returning to lobby.");
            }

        } catch (IOException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    public void resetMessageCount() {
        messageCount = 0;
    }

}

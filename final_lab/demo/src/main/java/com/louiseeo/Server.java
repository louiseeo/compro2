package com.louiseeo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.louiseeo.enums.GamePhase;
import com.louiseeo.model.Player;
import com.louiseeo.model.WordPair;
import com.louiseeo.service.FileService;

/**
 * Main server class for the chat system.
 * Responsible for accepting clients and managing communication between them.
 */
public class Server {
    private static final int PORT = 8000;

    // For networking layer
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    // For game data layer
    private static final List<Player> players = Collections.synchronizedList(new ArrayList<>());

    private static GamePhase currentPhase = GamePhase.LOBBY;
    private static int voteCount;

    public static void main(String[] args) {
        System.out.println("=== Chat Server Started ===");
        startGame();
        try (ServerSocket server = new ServerSocket(PORT)) {
            // Continuously listen for new client connections
            while (true) {

                Socket clientSocket = server.accept();
                System.out.println("Client connected: " + clientSocket.getPort());

                // Create handler for the connected client
                ClientHandler handler = new ClientHandler(clientSocket);

                // Named thread
                Thread thread = new Thread(handler, "Client-" + clientSocket.getPort());
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }

    /**
     * Sends a message to all clients except the sender.
     */
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    /**
     * Removes a client from the active clients list.
     */
    public static void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    /**
     * Adds a new client to the active clients list.
     */
    public static void addClient(ClientHandler handler) {
        clients.add(handler);
    }

    public static void broadcastAll(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    // Check number of players
    public static void startGame() {
        if (players.size() < 3) {
            broadcastAll("Waiting for more players...");
            return;
        }
        assignRoles();
        currentPhase = GamePhase.CHAT;

    }

    public static void assignRoles() {
        // Load word pairs from JSON
        List<WordPair> wordBank = FileService.loadWordbank("data/words.json");

        if (wordBank.isEmpty()) {
            broadcastAll("Error: Word bank is empty!!");
            return;
        }

        // Pick a random word pair
        Random random = new Random();
        WordPair selectedPair = wordBank.get(random.nextInt(wordBank.size()));

        // Pick a random player to be the Imposter
        int imposterIndex = random.nextInt(players.size());

        // Assign words to each player
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (i == imposterIndex) {
                // Give imposter the decoy word
                player.setWord(selectedPair.getDecoy());
            } else {
                // Give citizens the real word
                player.setWord(selectedPair.getReal());
            }
        }

        // Notify each player privately
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage("=== Game Starting!! ===");
                client.sendMessage("Your secret word is: " + client.getPlayer().getWord());
                client.sendMessage("Give clues without revealing your word directly!!");
            }
        }
    }
}

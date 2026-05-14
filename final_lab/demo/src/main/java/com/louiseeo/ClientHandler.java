package com.louiseeo;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.louiseeo.model.Player;

/**
 * Handles communication between the server and a single client.
 * Each client runs in its own thread.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private Player player;

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
            clientName(); // get client name

            // Add client to server list and notify others
            Server.addClient(this);
            Server.broadcast(getTimestamp() + " " + clientName + " joined the chat.", this);

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
    public void clientName() throws IOException {
        do {
            out.println("Enter your name:");
            clientName = in.readLine();
        } while (clientName == null || clientName.isBlank());

        System.out.println(clientName + " joined the chat.");
    }

    /**
     * Continuously listens for messages from the client
     * and broadcasts them to other users.
     */
    public void handleChat() throws IOException {
        String message;

        while ((message = in.readLine()) != null) {
            if (message.equalsIgnoreCase("bye")) {
                break;
            }

            String formatted = getTimestamp() + " " + clientName + ": " + message;

            Server.broadcast(formatted, this);
            sendMessage(getTimestamp() + " You: " + message);
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
            if (clientName != null) {
                String leaveMsg = getTimestamp() + " " + clientName + " left the chat.";
                System.out.println(leaveMsg);
                Server.broadcast(leaveMsg, this);
            }

            Server.removeClient(this); // removes client in server list

            // close socket connection
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    /**
     * Generates a timestamp in [HH:mm] format.
     */
    public String getTimestamp() {
        return "[" + LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm")) + "]";
    }

}

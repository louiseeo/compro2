import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Main server class for the chat system.
 * Responsible for accepting clients and managing communication between them.
 */
public class Server {
    private static final int PORT = 8000;

    // Thread-safe list
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("=== Chat Server Started ===");

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
}
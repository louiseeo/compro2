import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 8000;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to Chat App");

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started at port " + PORT);

            while (true) {
                Socket client = server.accept(); // wait for client to connect
                System.out.println("New client connected: " + client);

                ClientHandler handler = new ClientHandler(client);

                // lock the clients list and add item to it
                synchronized (clients) {
                    clients.add(handler);
                }

                // start a dedicated thread for this client
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error " + e.getMessage());
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    if (client != sender) {
                        // send the message
                        client.sendMessage(message);
                    }
                }
            }
        }

    }

    public static void removeClient(ClientHandler handler) {
            synchronized(clients) {
            clients.remove(handler);
        }
        }
}

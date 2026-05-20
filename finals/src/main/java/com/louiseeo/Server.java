package com.louiseeo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("--- UnderCoven Server Started on Port " + PORT + " ---");

            while (true) {
                // Wait for an actual new physical terminal connection
                Socket clientSocket = server.accept();
                System.out.println("New physical connection from: " + clientSocket.getRemoteSocketAddress());

                // Create one handler for this unique socket connection
                ClientHandler client = new ClientHandler(clientSocket);

                // Start one background thread execution loop
                Thread clientThread = new Thread(client);
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Server critical failure: " + e.getMessage());
        }
    }
}
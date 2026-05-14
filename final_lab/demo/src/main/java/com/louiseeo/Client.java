package com.louiseeo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client program for connecting to the chat server.
 * Handles user input and displays messages from the server.
 */
public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)) {

            System.out.println("=== Connected to Chat Server ===");

            // Receive prompt for name from server
            String namePrompt = in.readLine();
            System.out.print(namePrompt + " ");
            String name = scanner.nextLine();
            out.println(name);

            // Listener thread
            Thread listener = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("\n" + msg);
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            listener.setDaemon(true);
            listener.start();

            System.out.print("> ");
            while (true) {
                String input = scanner.nextLine();
                out.println(input);

                if (input.equalsIgnoreCase("bye")) {
                    break;
                }
            }

            System.out.println("Closing connection...");

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}

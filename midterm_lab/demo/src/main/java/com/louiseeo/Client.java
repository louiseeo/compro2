package com.louiseeo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Entry point for the Rock Paper Scissors client.
 * Connects to the server, displays messages from
 * the server, and sends player input when prompted.
 *
 * @author louiseeo
 */
public class Client {

    /**
     * Main method. Connects to the server via socket,
     * reads messages from the server and prints them,
     * and sends user input back when the server prompts.
     */
    public static void main(String[] args) {
        String SERVER = "localhost";
        int PORT = 8000;

        try (Socket socket = new Socket(SERVER, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to server!");

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);

                if (message.contains("Choice") ||
                        message.contains("Player Name") ||
                        message.contains("Set password") ||
                        message.contains("Enter password") ||
                        message.contains("Enter choice")) {
                    System.out.print("> ");
                    out.println(sc.nextLine());
                }
            }

        } catch (IOException e) {
            if (e.getMessage().contains("Connection refused")) {
                System.out.println("Cannot connect to server. Make sure the server is running!");
            } else {
                System.out.println("\nConnection lost. Game ended.");
            }
        }
    }
}
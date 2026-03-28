package com.louiseeo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HangmanClient {
    public static void main(String[] args) {
        String server = "192.168.100.45"; // same as 127.0.0.1
        int port = 8000;

        try (Socket socket = new Socket(server, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner sc = new Scanner(System.in);) {

            System.out.println("Connected to the server. Welcome!");

            String message;
            while (true) {
                message = in.readLine();
                if (message == null)
                    break;

                System.out.println(message);

                // set words where user can interact with
                if (message.contains("Choice") ||
                        message.contains("Enter password") ||
                        message.contains("Set password") ||
                        message.contains("Player Name") ||
                        message.contains("Another") ||
                        message.contains("Enter a letter") ||
                        message.contains("Please enter a letter!")) {
                    System.out.print("> ");
                    String input = sc.nextLine();
                    out.println(input);
                }
            }

        } catch (IOException e) {
            System.out.println("Can't connect right now...");
        }

    }
}
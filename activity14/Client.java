import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
            ){
            System.out.println("--- Connected to Server ---");

            // create a thread for listening to server's broadcast
            Thread listener = new Thread(() -> {
                String serverMessage;
                try {
                    while((serverMessage = in.readLine()) != null) {
                        System.out.println("\n" + serverMessage);
                        System.out.println("> "); // prompt user
                    }
                } catch (IOException e) {
                    System.err.println("Connection to server lost.");
                }
            });

            listener.start();

            // main thread
            System.out.println("> ");
            while (true) {
                String userInput = scanner.nextLine();
                out.println(userInput);

                if (userInput.equalsIgnoreCase("bye")) {
                    break;
                }
            }

            System.out.println("Closing connection...");
        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}

import java.io.*;
import java.lang.invoke.StringConcatFactory;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        
    }

    @Override
    public void run() {
        //create I/O streams
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // First message from the client
            out.println("Enter your name: ");
            clientName = in.readLine();
            if (clientName == null) return;

            String joinMsg = clientName + " has joined the chat.";
            System.out.println(joinMsg);


            String inputLine;
            while((inputLine = in.readLine()) != null){
                if(inputLine.equals("bye")) {
                    
                }
            }
            
        }catch(IOException e){
            System.out.println("Can't connect right now...");
        }

    }

    
}

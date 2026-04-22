import java.io.*;
import java.net.Socket;

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
            Server.broadcast(joinMsg, this);

            String inputLine;
            while((inputLine = in.readLine()) != null){
                if(inputLine.equals("bye")) {
                    break;
                }

                Server.broadcast(inputLine, this);
            }
            
        }catch(IOException e){
           System.err.println("Error handling client " + clientName + " " + e.getMessage());
        } finally {
            String exitMessage = clientName + " has lest the chat";
            //cleanup when client leaves
            System.out.println(exitMessage);
            Server.removeClient(this);
            Server.broadcast(exitMessage, this);
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

}

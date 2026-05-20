package com.louiseeo;

import java.io.*;
import java.net.Socket;
import com.louiseeo.enums.GamePhase;

import com.louiseeo.model.CitizenPlayer;
import com.louiseeo.model.Player;
import com.louiseeo.service.ChatService;
import com.louiseeo.service.GameService;
import com.louiseeo.service.VoteService;
import com.louiseeo.service.AccountService;
import com.louiseeo.service.LeaderboardService;
import com.louiseeo.service.UIService;

/**
 * Handles communication between the server and one client.
 * Processes login, chat, voting, and gameplay interactions.
 *
 * @author louiseeo
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Player player;
    private int messageCount = 0;
    private boolean returningToMenu = false;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Main execution method for the client thread.
     * Handles setup, registration, chat processing,
     * and cleanup when disconnected.
     */
    @Override
    public void run() {
        try {
            setupStreams();
            while (true) {
                registerPlayer();

                if (socket.isClosed())
                    return;
                if (player == null)
                    return;

                ChatService.addClient(this);
                handleChat();

                if (socket.isClosed())
                    return;

                ChatService.removeClient(this);
                if (player != null) {
                    // print leave message here
                    System.out.println(player.getUsername() + " left the game.");
                    GameService.removePlayer(player);
                    player = null;
                }

                resetMessageCount();
                returningToMenu = false;
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            returningToMenu = false;
            cleanup();
        }
    }

    /**
     * Initializes input and output streams for client communication.
     *
     * @throws IOException if stream setup fails
     */
    public void setupStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Displays the main menu and handles
     * login, signup, leaderboard viewing, or exiting.
     *
     * @throws IOException if communication fails
     */
    public void registerPlayer() throws IOException {
        while (true) {
            out.println(UIService.mainMenu());
            out.flush();

            String choice = in.readLine();
            if (choice == null)
                return;

            switch (choice.trim()) {
                case "1" -> {
                    handleLogin();
                    return;
                }
                case "2" -> {
                    handleSignup();
                    return;
                }
                case "3" -> sendMessage(LeaderboardService.displayLeaderboard());
                case "0" -> {
                    sendMessage(
                            "\n"
                                    + UIService.thickDivider() + "\n"
                                    + "Thank you for playing UnderCoven. Bye!\n"
                                    + UIService.thickDivider() + "\n");
                    closeConnection();
                    return;
                }
                default -> sendMessage(UIService.error("Invalid choice. Try again.") + "\n");
            }
        }
    }

    /**
     * Processes player login credentials and adds the player to the game.
     *
     * @throws IOException if communication fails
     */
    private void handleLogin() throws IOException {
        if (GameService.getCurrentPhase() != GamePhase.LOBBY) {
            sendMessage(UIService.error("A game is already in progress. Please wait.") + "\n");
            registerPlayer();
            return;
        }

        out.println("Username:");
        out.flush();
        String username = in.readLine();
        if (username == null)
            return;

        out.println("Password:");
        out.flush();
        String password = in.readLine();
        if (password == null)
            return;

        String result = AccountService.login(username.trim(), password.trim());

        switch (result) {
            case "success" -> {
                player = new CitizenPlayer(username.trim(), "");
                GameService.addPlayer(player);
                sendMessage(
                        "\n"
                                + UIService.thickDivider() + "\n"
                                + UIService.success("Login successful! Welcome, " + username.trim() + ".") + "\n"
                                + UIService.thickDivider() + "\n");
                System.out.println(username.trim() + " logged in.");
            }
            case "already_logged_in" -> {
                sendMessage(UIService.error("This account is already logged in.") + "\n");
                registerPlayer();
            }
            default -> {
                sendMessage(UIService.error("Invalid credentials. Try again.") + "\n");
                registerPlayer();
            }
        }
    }

    /**
     * Creates a new player account and adds the player to the game.
     *
     * @throws IOException if communication fails
     */
    private void handleSignup() throws IOException {
        if (GameService.getCurrentPhase() != GamePhase.LOBBY) {
            sendMessage(UIService.error("A game is already in progress. Please wait.") + "\n");
            registerPlayer();
            return;
        }

        out.println("New username:");
        out.flush();
        String username = in.readLine();
        if (username == null)
            return;

        out.println("New password:");
        out.flush();
        String password = in.readLine();
        if (password == null)
            return;

        boolean success = AccountService.signup(username.trim(), password.trim());

        if (success) {
            player = new CitizenPlayer(username.trim(), "");
            GameService.addPlayer(player);
            sendMessage(
                    "\n"
                            + UIService.thickDivider() + "\n"
                            + UIService.success("Account created! Welcome, " + username.trim() + ".") + "\n"
                            + UIService.thickDivider() + "\n");
            System.out.println(username.trim() + " signed up.");
        } else {
            sendMessage(UIService.error("Username already exists. Try again.") + "\n");
            registerPlayer();
        }
    }

    /**
     * Continuously handles player messages,
     * gameplay actions, voting, and chat.
     * Returns when game ends so run() loops back to menu.
     *
     * @throws IOException if communication fails
     */
    public void handleChat() throws IOException {
        socket.setSoTimeout(1000); // start timeout only during game
        try {
            while (true) {
                String message;
                try {
                    message = in.readLine();
                } catch (java.net.SocketTimeoutException e) {
                    if (returningToMenu)
                        break;
                    continue;
                }

                if (message == null)
                    break;
                message = message.trim();

                // ── LOBBY ──────────────────────────────────────────────────────────
                if (GameService.getCurrentPhase() == GamePhase.LOBBY) {
                    if (message.equalsIgnoreCase("ready")) {
                        GameService.handleReady(this);
                    } else {
                        sendMessage(
                                UIService.error("Invalid command.") + "\n"
                                        + UIService.tip("Type 'ready' to start the game.") + "\n"
                                        + UIService.divider());
                    }
                    continue;
                }

                // ── PLAY AGAIN ─────────────────────────────────────────────────────
                if (GameService.getCurrentPhase() == GamePhase.PLAY_AGAIN) {
                    boolean shouldExit = VoteService.handlePlayAgain(this, message);
                    if (shouldExit) {
                        synchronized (ChatService.getClients()) {
                            for (ClientHandler receiver : ChatService.getClients()) {
                                for (ClientHandler c : ChatService.getClients()) {
                                    if (receiver == c) {
                                        receiver.sendMessage(UIService.system("You left the game."));
                                    } else {
                                        receiver.sendMessage(
                                                UIService.system(c.getPlayer().getUsername() + " left the game."));
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                        }
                        synchronized (ChatService.getClients()) {
                            for (ClientHandler c : ChatService.getClients()) {
                                c.returningToMenu = true;
                            }
                        }
                        break;
                    }
                    continue;
                }

                // ── VOTING ─────────────────────────────────────────────────────────
                if (GameService.getCurrentPhase() == GamePhase.VOTING) {
                    VoteService.submitVote(this, message);
                    continue;
                }

                // ── REQUEST VOTE ───────────────────────────────────────────────────
                if (message.equalsIgnoreCase("vote")) {
                    if (messageCount >= 3) {
                        VoteService.handleVote(this);
                    } else {
                        sendMessage(
                                UIService.error("You need at least 3 messages before voting.") + "\n"
                                        + UIService.divider());
                    }
                    continue;
                }

                // ── NORMAL CHAT ────────────────────────────────────────────────────
                messageCount++;
                ChatService.broadcast("[" + player.getUsername() + "]: " + message, this);
                sendMessage("[You]: " + message);
            }
        } finally {
            socket.setSoTimeout(0); // clear timeout when exiting — 0 means block forever again
        }
    }

    /**
     * Sends a message to this specific client.
     *
     * @param message : message to send
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Removes the player, closes the socket,
     * and cleans up resources after disconnecting.
     */
    public void cleanup() {
        try {
            if (!returningToMenu && player != null) {
                System.out.println(player.getUsername() + " left the game.");
                ChatService.broadcastAll(UIService.system(player.getUsername() + " left the game."));

                // handle play again BEFORE removing from lists
                if (GameService.getCurrentPhase() == GamePhase.PLAY_AGAIN) {
                    boolean shouldExit = VoteService.handlePlayAgain(this, "no");
                    if (shouldExit) {
                        synchronized (ChatService.getClients()) {
                            for (ClientHandler c : ChatService.getClients()) {
                                if (c != this) {
                                    c.returningToMenu = true;
                                }
                            }
                        }
                    }
                }

                GameService.removePlayer(player);
            }

            ChatService.removeClient(this);

            if (!returningToMenu) {
                if (GameService.getCurrentPhase() == GamePhase.VOTING) {
                    VoteService.resetVotes();
                    ChatService.broadcastAll(
                            UIService.system("A player left during voting. Restarting vote..."));
                    if (GameService.getPlayers().size() >= 3) {
                        VoteService.startVoting();
                    } else {
                        GameService.setCurrentPhase(GamePhase.LOBBY);
                        ChatService.broadcastAll(
                                UIService.system("Not enough players. Returning to lobby."));
                    }
                } else if (GameService.getPlayers().size() < 3) {
                    VoteService.resetVotes();
                    GameService.setCurrentPhase(GamePhase.LOBBY);
                    ChatService.broadcastAll(
                            UIService.system("Not enough players. Returning to lobby."));
                }
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    /**
     * Closes the client's socket connection.
     * Prints the client's address to the server console.
     */
    public void closeConnection() {
        try {
            System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public void resetMessageCount() {
        messageCount = 0;
    }
}
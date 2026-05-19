package com.louiseeo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.louiseeo.ClientHandler;
import com.louiseeo.enums.GamePhase;
import com.louiseeo.model.Player;
import com.louiseeo.model.WordPair;
import com.louiseeo.model.ImposterPlayer;
import com.louiseeo.model.CitizenPlayer;

/**
 * Handles the main game logic of UnderCoven.
 * Manages players, roles, game phases,
 * word assignment, and win conditions.
 *
 * @author louiseeo
 */
public class GameService {

    private static GamePhase currentPhase = GamePhase.LOBBY;
    private static WordPair currentWordPair;

    private static final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private static final Set<ClientHandler> readyPlayers = Collections.synchronizedSet(new HashSet<>());

    public static List<Player> getPlayers() {
        return players;
    }

    public static void setCurrentPhase(GamePhase phase) {
        currentPhase = phase;
    }

    /**
     * Marks a player as ready before the game starts.
     * Starts the game once all players are ready.
     *
     * @param client : the client who typed ready
     */
    public static synchronized void handleReady(ClientHandler client) {
        if (readyPlayers.contains(client)) {
            client.sendMessage(UIService.error("You already typed ready!"));
            return;
        }

        if (getPlayers().size() < 3) {
            client.sendMessage(
                    UIService.error("Not enough players.") + "\n"
                            + UIService.tip("At least 3 players are required.") + "\n"
                            + UIService.divider());
            return;
        }

        readyPlayers.add(client);
        ChatService.broadcastAll(
                UIService.system(
                        client.getPlayer().getUsername()
                                + " is ready.  ("
                                + readyPlayers.size() + "/" + getPlayers().size() + ")")
                        + "\n"
                        + UIService.divider());

        if (readyPlayers.size() >= getPlayers().size()) {
            readyPlayers.clear();
            startGame();
        }
    }

    /**
     * Starts a new game session.
     * Resets voting data, assigns player roles,
     * and changes the game phase to CHAT.
     */
    public static void startGame() {
        readyPlayers.clear();
        VoteService.resetVotes();

        for (ClientHandler client : ChatService.getClients()) {
            client.resetMessageCount();
        }

        ChatService.broadcastAll(
                "\n"
                        + UIService.thickDivider() + "\n"
                        + "All players connected. Game is starting!\n"
                        + UIService.thickDivider() + "\n"
                        + "\n"
                        + UIService.tip("Winners get +30 pts") + "\n"
                        + UIService.tip("Losers get -20 pts") + "\n"
                        + UIService.divider());

        assignRoles();
        currentPhase = GamePhase.CHAT;
        ChatService.broadcastAll(UIService.chatPhase());
    }

    /**
     * Randomly assigns one imposter and multiple citizens.
     * Citizens receive the secret word while the imposter
     * only receives a hint related to the word.
     */
    public static void assignRoles() {
        List<WordPair> wordBank = FileService.loadWordbank("data/words.json");

        if (wordBank.isEmpty()) {
            ChatService.broadcastAll(UIService.error("Word bank is empty!"));
            return;
        }

        Random random = new Random();
        WordPair selectedPair = wordBank.get(random.nextInt(wordBank.size()));
        int imposterIndex = random.nextInt(players.size());

        for (int i = 0; i < players.size(); i++) {
            Player oldPlayer = players.get(i);
            String username = oldPlayer.getUsername();

            if (i == imposterIndex) {
                ImposterPlayer imposter = new ImposterPlayer(username, selectedPair.getHint());
                players.set(i, imposter);
            } else {
                CitizenPlayer citizen = new CitizenPlayer(username, selectedPair.getReal());
                players.set(i, citizen);
            }
        }

        currentWordPair = selectedPair;

        synchronized (ChatService.getClients()) {
            for (ClientHandler client : ChatService.getClients()) {
                for (Player updatedPlayer : players) {
                    if (updatedPlayer.getUsername().equals(client.getPlayer().getUsername())) {
                        client.setPlayer(updatedPlayer);
                        break;
                    }
                }

                if (client.getPlayer() instanceof ImposterPlayer) {
                    client.sendMessage(UIService.imposterRole(client.getPlayer().getWord()));
                } else {
                    client.sendMessage(UIService.citizenRole(client.getPlayer().getWord()));
                }
            }
        }
    }

    /**
     * Determines the winner after voting ends.
     * Updates leaderboard points and starts the play-again phase.
     *
     * @param eliminated : the player eliminated during voting
     */
    public static void checkWinCondition(Player eliminated) {
        Player imposter = null;
        for (Player p : players) {
            if (p.getRole().equals("Imposter")) {
                imposter = p;
                break;
            }
        }

        String imposterName = imposter != null ? imposter.getUsername() : "unknown";
        String winner;

        if (eliminated.getRole().equals("Imposter")) {
            winner = "CITIZENS WIN!";
            for (Player p : players) {
                if (p.getRole().equals("Citizen")) {
                    LeaderboardService.updatePoints(p.getUsername(), 30);
                } else {
                    LeaderboardService.updatePoints(p.getUsername(), -20);
                }
            }
        } else {
            winner = "IMPOSTER WINS!";
            for (Player p : players) {
                if (p.getRole().equals("Imposter")) {
                    LeaderboardService.updatePoints(p.getUsername(), 30);
                } else {
                    LeaderboardService.updatePoints(p.getUsername(), -20);
                }
            }
        }

        ChatService.broadcastAll(
                UIService.gameResults(
                        eliminated.getUsername(),
                        imposterName,
                        currentWordPair.getReal(),
                        winner));

        currentPhase = GamePhase.RESULTS;
        handlePlayAgain();
    }

    /**
     * Starts the play-again voting phase.
     */
    public static void handlePlayAgain() {
        currentPhase = GamePhase.PLAY_AGAIN;
        ChatService.broadcastAll(UIService.playAgain());
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static void removePlayer(Player player) {
        players.remove(player);
    }

    public static GamePhase getCurrentPhase() {
        return currentPhase;
    }
}
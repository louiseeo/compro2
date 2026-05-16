package com.louiseeo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.louiseeo.ClientHandler;
import com.louiseeo.enums.GamePhase;
import com.louiseeo.model.GameResult;
import com.louiseeo.model.Player;
import com.louiseeo.model.WordPair;
import com.louiseeo.model.ImposterPlayer;
import com.louiseeo.model.CitizenPlayer;

public class GameService {
    private static GamePhase currentPhase = GamePhase.LOBBY;
    private static WordPair currentWordPair;
    private static final List<Player> players = Collections.synchronizedList(new ArrayList<>());

    public static List<Player> getPlayers() {
        return players;
    }

    public static void setCurrentPhase(GamePhase phase) {
        currentPhase = phase;
    }

    // Check number of players
    public static void startGame() {
        VoteService.resetVotes();
        ChatService.broadcastAll("-+-+-+-+- 3 Players connected! Game is starting! -+-+-+-+-");
        for (ClientHandler client : ChatService.getClients()) {

            client.resetMessageCount();
        }
        assignRoles();
        currentPhase = GamePhase.CHAT;
        ChatService.broadcastAll(
                "=== CHAT PHASE: Give clues about your word! Type 'vote' when you know who the imposter is! ===");

    }

    public static void assignRoles() {
        List<WordPair> wordBank = FileService.loadWordbank("data/words.json");

        if (wordBank.isEmpty()) {
            ChatService.broadcastAll("Error: Word bank is empty!!");
            return;
        }

        Random random = new Random();
        WordPair selectedPair = wordBank.get(random.nextInt(wordBank.size()));
        int imposterIndex = random.nextInt(players.size());

        // 1. Assign Roles and Words/Hints
        for (int i = 0; i < players.size(); i++) {
            Player oldPlayer = players.get(i);
            String username = oldPlayer.getUsername();

            if (i == imposterIndex) {
                // The Imposter gets the category hint stored as their "word"
                ImposterPlayer imposter = new ImposterPlayer(username, selectedPair.getHint());
                players.set(i, imposter);
            } else {
                // Citizens get the actual secret word
                CitizenPlayer citizen = new CitizenPlayer(username, selectedPair.getReal());
                players.set(i, citizen);
            }
        }

        currentWordPair = selectedPair;

        // 2. Notify clients with customized dynamic prompts
        synchronized (ChatService.getClients()) {
            for (ClientHandler client : ChatService.getClients()) {
                client.sendMessage("\n=== Game Starting!! ===");

                // Sync ClientHandler's player reference to the new subclass instance
                for (Player updatedPlayer : players) {
                    if (updatedPlayer.getUsername().equals(client.getPlayer().getUsername())) {
                        client.setPlayer(updatedPlayer);
                        break;
                    }
                }

                // Check if this specific connection belongs to the Imposter or a Citizen
                if (client.getPlayer() instanceof ImposterPlayer) {
                    client.sendMessage("🕵️ YOU ARE THE IMPOSTER!");
                    client.sendMessage(
                            "You don't know the word! Your category hint is: " + client.getPlayer().getWord());
                    client.sendMessage("Blend in! Try to figure out the real word from others' clues.");
                } else {
                    client.sendMessage("🧑‍🌾 YOU ARE A CITIZEN!");
                    client.sendMessage("Your secret word is: " + client.getPlayer().getWord());
                    client.sendMessage("Give clever clues to find out who doesn't know the word!");
                }
            }
        }
    }

    public static void checkWinCondition(Player eliminated) {
        ChatService.broadcastAll("=== " + eliminated.getUsername() + " has been eliminated!! ===");

        if (eliminated.getRole().equals("Imposter")) {
            ChatService.broadcastAll("=== " + eliminated.getUsername() + " WAS the Imposter!! ===");
            ChatService.broadcastAll("=== CITIZENS WIN!! 🎉 ===");
            FileService.saveGameHistory("data/game_history.json",
                    new GameResult(eliminated.getUsername(), currentWordPair.getReal(), currentWordPair.getHint(),
                            "Citizens"));
        } else {
            ChatService.broadcastAll("=== " + eliminated.getUsername() + " was NOT the Imposter!! ===");
            ChatService.broadcastAll("=== IMPOSTER WINS!! ===");
            FileService.saveGameHistory("data/game_history.json",
                    new GameResult(eliminated.getUsername(), currentWordPair.getReal(), currentWordPair.getHint(),
                            "Imposter"));
        }

        currentPhase = GamePhase.RESULTS;
        handlePlayAgain();
    
    }

    public static void handlePlayAgain() {

        currentPhase = GamePhase.PLAY_AGAIN;

        ChatService.broadcastAll(
                "Play again? Type 'yes' or 'no':");
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

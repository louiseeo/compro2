package com.louiseeo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.louiseeo.ClientHandler;
import com.louiseeo.enums.GamePhase;

public class ChatService {
    // For networking layer
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static List<ClientHandler> getClients() {
        return clients;
    }

    /**
     * Sends a message to all clients except the sender.
     */
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void broadcastAll(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Removes a client from the active clients list.
     */
    public static void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    /**
     * Adds a new client to the active clients list.
     */
    public static void addClient(ClientHandler handler) {
        clients.add(handler);
        broadcastAll(
                handler.getPlayer().getUsername()
                        + " joined the game.");
        if (GameService.getPlayers().size() >= 3 && GameService.getCurrentPhase() == GamePhase.LOBBY) {
            GameService.startGame();
        }
    }
}

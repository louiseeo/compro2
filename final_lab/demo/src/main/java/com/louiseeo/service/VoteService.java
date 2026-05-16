package com.louiseeo.service;

import java.util.HashMap;
import java.util.Map;

import com.louiseeo.ClientHandler;
import com.louiseeo.enums.GamePhase;
import com.louiseeo.model.Player;

public class VoteService {
    private static int voteCount;
    private static Map<ClientHandler, Boolean> voteRequests = new HashMap<>();
    private static Map<ClientHandler, Integer> votes = new HashMap<>();

    public static void setVoteCount(int voteCount) {
        VoteService.voteCount = voteCount;
    }

    public static int getVoteCount() {
        return voteCount;
    }

    public static synchronized void handleVote(ClientHandler voter) {

        if (voteRequests.containsKey(voter)) {
            voter.sendMessage("You already requested voting!");
            return;
        }

        voteRequests.put(voter, true);

        voteCount++;

        ChatService.broadcastAll(
                voter.getPlayer().getUsername()
                        + " wants to vote!! ("
                        + voteCount + "/"
                        + GameService.getPlayers().size() + ")");

        int majority = (GameService.getPlayers().size() / 2) + 1;

        if (voteCount >= majority) {

            voteCount = 0;

            voteRequests.clear();

            GameService.setCurrentPhase(GamePhase.VOTING);

            startVoting();
        }
    }

    public static void startVoting() {

        ChatService.broadcastAll(
                "=== VOTING PHASE! Who is the Imposter? ===");

        synchronized (ChatService.getClients()) {

            for (int i = 0; i < ChatService.getClients().size(); i++) {

                ChatService
                        .broadcastAll("[" + (i + 1) + "] " + ChatService.getClients()
                                .get(i)
                                .getPlayer()
                                .getUsername());
            }
        }

        ChatService.broadcastAll(
                "Enter the number of who you think is the Imposter:");
    }

    public static synchronized void submitVote(ClientHandler voter, String input) {

        try {

            int voteIndex = Integer.parseInt(input) - 1;

            // invalid player number
            if (voteIndex < 0 ||
                    voteIndex >= ChatService.getClients().size()) {

                voter.sendMessage("Invalid player number!");
                return;
            }

            // already voted
            if (votes.containsKey(voter)) {

                voter.sendMessage("You already voted!");
                return;
            }

            if (ChatService.getClients().get(voteIndex) == voter) {

                voter.sendMessage("You cannot vote for yourself!");

                return;
            }

            votes.put(voter, voteIndex);
            voter.sendMessage("Vote submitted!");

            // everyone voted
            if (votes.size() == GameService.getPlayers().size()) {

                countVotes();
            }

        } catch (NumberFormatException e) {

            voter.sendMessage("Please enter a valid number!");
        }
    }

    public static void countVotes() {

        Map<Integer, Integer> tally = new HashMap<>();
        boolean tie = false;
        for (int vote : votes.values()) {

            tally.put(vote,
                    tally.getOrDefault(vote, 0) + 1);
        }

        int maxVotes = 0;
        int eliminatedIndex = 0;

        for (Map.Entry<Integer, Integer> entry : tally.entrySet()) {

            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                eliminatedIndex = entry.getKey();
                tie = false;
            } else if (entry.getValue() == maxVotes) {
                tie = true;
            }
        }

        if (tie) {

            ChatService.broadcastAll(
                    "Tie vote! Nobody is eliminated!");

            votes.clear();

            GameService.setCurrentPhase(GamePhase.CHAT);

            return;
        }

        Player eliminated = ChatService.getClients()
                .get(eliminatedIndex)
                .getPlayer();

        votes.clear();

        GameService.checkWinCondition(eliminated);
    }

    public static void resetVotes() {
        voteCount = 0;
        votes.clear();
        voteRequests.clear();
    }

}

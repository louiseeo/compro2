package com.louiseeo.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.louiseeo.ClientHandler;
import com.louiseeo.enums.GamePhase;
import com.louiseeo.model.Player;

/**
 * Handles all voting-related features in the game.
 * Manages vote requests, vote submissions,
 * vote counting, and play-again responses.
 *
 * @author louiseeo
 */
public class VoteService {

    private static int voteCount;
    private static Map<ClientHandler, Boolean> voteRequests = new HashMap<>();
    private static Map<ClientHandler, Integer> votes = new HashMap<>();
    private static Map<ClientHandler, String> playAgainResponses = Collections.synchronizedMap(new HashMap<>());

    public static void setVoteCount(int voteCount) {
        VoteService.voteCount = voteCount;
    }

    public static int getVoteCount() {
        return voteCount;
    }

    /**
     * Handles a player's request to start voting.
     * Begins the voting phase once majority is reached.
     *
     * @param voter : the player requesting to vote
     */
    public static synchronized void handleVote(ClientHandler voter) {
        if (voteRequests.containsKey(voter)) {
            voter.sendMessage(UIService.error("You already requested voting!"));
            return;
        }

        voteRequests.put(voter, true);
        voteCount++;

        ChatService.broadcastAll(
                UIService.system(
                        voter.getPlayer().getUsername()
                                + " wants to vote.  ("
                                + voteCount + "/" + GameService.getPlayers().size() + ")"));

        int majority = (GameService.getPlayers().size() / 2) + 1;
        if (voteCount >= majority) {
            voteCount = 0;
            voteRequests.clear();
            GameService.setCurrentPhase(GamePhase.VOTING);
            startVoting();
        }
    }

    /**
     * Displays the list of players and starts the voting phase.
     */
    public static void startVoting() {
        StringBuilder table = new StringBuilder();
        table.append(UIService.votingPhase());

        synchronized (ChatService.getClients()) {
            for (int i = 0; i < ChatService.getClients().size(); i++) {
                String name = ChatService.getClients().get(i).getPlayer().getUsername();
                table.append(String.format("  %-4d  %s\n", i + 1, name));
            }
        }

        table.append("\n").append(UIService.divider()).append("\n");
        table.append("Enter the number of who you think is the imposter:");
        ChatService.broadcastAll(table.toString());
    }

    /**
     * Records a player's vote during the voting phase.
     * Counts votes once all players have voted.
     *
     * @param voter : the client submitting a vote
     * @param input : selected player number
     */
    public static synchronized void submitVote(ClientHandler voter, String input) {
        try {
            int voteIndex = Integer.parseInt(input) - 1;

            if (voteIndex < 0 || voteIndex >= ChatService.getClients().size()) {
                voter.sendMessage(UIService.divider() + "\n" + UIService.error("Invalid player number.") + "\n"
                        + UIService.divider());
                return;
            }

            if (votes.containsKey(voter)) {
                voter.sendMessage(UIService.divider() + "\n" + UIService.error("You already voted.") + "\n"
                        + UIService.divider());
                return;
            }

            if (ChatService.getClients().get(voteIndex) == voter) {
                voter.sendMessage(UIService.divider() + "\n" + UIService.error("You cannot vote for youself.") + "\n"
                        + UIService.divider());
                return;
            }

            votes.put(voter, voteIndex);
            voter.sendMessage(UIService.success(
                    "You voted for " + ChatService.getClients().get(voteIndex).getPlayer().getUsername() + "."));

            if (votes.size() == GameService.getPlayers().size()) {
                countVotes();
            }

        } catch (NumberFormatException e) {
            voter.sendMessage(UIService.divider() + "\n" + UIService.error("Please enter a valid number.") + "\n"
                    + UIService.divider());
        }
    }

    /**
     * Counts all submitted votes and determines
     * which player is eliminated.
     * Handles tie situations when necessary.
     */
    public static void countVotes() {
        Map<Integer, Integer> tally = new HashMap<>();
        boolean tie = false;

        for (int vote : votes.values()) {
            tally.put(vote, tally.getOrDefault(vote, 0) + 1);
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
                    "\n"
                            + UIService.system("It's a tie! Nobody eliminated.") + "\n"
                            + UIService.tip("Starting another vote...") + "\n"
                            + UIService.divider());
            votes.clear();
            voteRequests.clear();
            GameService.setCurrentPhase(GamePhase.VOTING);
            startVoting();
            return;
        }

        Player eliminated = ChatService.getClients().get(eliminatedIndex).getPlayer();
        votes.clear();
        GameService.checkWinCondition(eliminated);
    }

    /**
     * Clears all stored voting data for a new game round.
     */
    public static void resetVotes() {
        voteCount = 0;
        votes.clear();
        voteRequests.clear();
        playAgainResponses.clear();
    }

    /**
     * Handles player responses for playing again.
     * Starts a new game if majority votes yes,
     * otherwise disconnects all clients.
     *
     * @param client   : responding client
     * @param response : yes or no response
     */
    public static synchronized boolean handlePlayAgain(ClientHandler client, String response) {
        if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
            client.sendMessage(UIService.error("Please type 'yes' or 'no' only."));
            return false;
        }

        if (playAgainResponses.containsKey(client)) {
            client.sendMessage(UIService.error("You already responded."));
            return false;
        }

        playAgainResponses.put(client, response);
        int totalPlayers = ChatService.getClients().size();

        ChatService.broadcastAll(
                UIService.system(
                        client.getPlayer().getUsername()
                                + " voted " + response + ".  ("
                                + playAgainResponses.size() + "/" + totalPlayers + " responded)"));

        if (playAgainResponses.size() < totalPlayers) {
            return false;
        }

        long yesCount = playAgainResponses.values().stream()
                .filter(r -> r.equalsIgnoreCase("yes")).count();
        long noCount = totalPlayers - yesCount;

        if (yesCount > noCount) {
            // majority yes — start new game
            resetVotes();
            GameService.startGame();
            return false;
        } else if (noCount > yesCount) {
            // majority no — return to menu
            ChatService.broadcastAll(
                    "\n"
                            + UIService.system("Majority voted no. Returning to main menu...") + "\n"
                            + UIService.thickDivider());
            resetVotes();
            GameService.setCurrentPhase(GamePhase.LOBBY);
            return true;
        } else {
            // tie = ask the players again
            ChatService.broadcastAll(
                    "\n"
                            + UIService.system("It's a tie! Vote again.") + "\n"
                            + UIService.divider());
            playAgainResponses.clear();
            ChatService.broadcastAll(UIService.playAgain());
            return false;
        }
    }
}
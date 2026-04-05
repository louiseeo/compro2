package com.louiseeo.service;

import com.louiseeo.model.GameResult;
import com.louiseeo.model.Player;

/**
 * Handles all Rock Paper Scissors game logic.
 * Responsible for determining round winners,
 * updating scores, and tracking round progression.
 *
 * @author louiseeo
 */
public class GameService {
    private Player player1;
    private Player player2;

    /**
     * Constructs a GameService for two players.
     *
     * @param player1 : the first player
     * @param player2 : the second player
     */
    public GameService(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Resets both players' choices after each round
     * and increments the round counter.
     */
    public void resetRound() {
        player1.resetChoice();
        player2.resetChoice();
    }

    /**
     * Compares both players' choices and determines
     * the round winner. Updates wins and losses on
     * the corresponding Account objects.
     *
     * @return GameResult containing winner and choices
     */
    public GameResult determineWinner() {
        switch (player1.getChoice()) {
            case 0:
                if (player2.getChoice() == 0) {
                    return new GameResult("Draw", 0, 0);
                } else if (player2.getChoice() == 1) {
                    player2.getAccount().incrementWins();
                    player1.getAccount().incrementLosses();
                    return new GameResult(player2.getUsername(), 0, 1);
                } else if (player2.getChoice() == 2) {
                    player1.getAccount().incrementWins();
                    player2.getAccount().incrementLosses();
                    return new GameResult(player1.getUsername(), 0, 2);
                }
                break;
            case 1:
                if (player2.getChoice() == 0) {
                    player1.getAccount().incrementWins();
                    player2.getAccount().incrementLosses();
                    return new GameResult(player1.getUsername(), 1, 0);
                } else if (player2.getChoice() == 1) {
                    return new GameResult("Draw", 1, 1);
                } else if (player2.getChoice() == 2) {
                    player2.getAccount().incrementWins();
                    player1.getAccount().incrementLosses();
                    return new GameResult(player2.getUsername(), 1, 2);
                }
                break;
            case 2:
                if (player2.getChoice() == 0) {
                    player2.getAccount().incrementWins();
                    player1.getAccount().incrementLosses();
                    return new GameResult(player2.getUsername(), 2, 0);
                } else if (player2.getChoice() == 1) {
                    player1.getAccount().incrementWins();
                    player2.getAccount().incrementLosses();
                    return new GameResult(player1.getUsername(), 2, 1);
                } else if (player2.getChoice() == 2) {
                    return new GameResult("Draw", 2, 2);
                }
                break;
        }
        return null;
    }

     /**
     * Formats the GameResult into a readable string
     * using GameResult's toString() method.
     *
     * @param result : the GameResult to format
     * @return formatted result string
     */
    public String formatResult(GameResult result) {
        return result.toString();
    }

}

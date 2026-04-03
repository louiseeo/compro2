package com.louiseeo.service;

import com.louiseeo.model.GameResult;
import com.louiseeo.model.Player;

public class GameService {
    private Player player1;
    private Player player2;
    private int round;
    private static final int MAX_ROUNDS = 10;

    public GameService(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.round = 0;
    }

    public boolean bothPlayersChose() {
        return (player1.getChoice() != -1 && player2.getChoice() != -1);

    }

    public boolean isMatchOver() {
        return round == MAX_ROUNDS;
    }

    public void resetRound() {
        player1.resetChoice();
        player2.resetChoice();
        round++;
    }

    public GameResult determineWinner() {
        switch (player1.getChoice()) {
            case 0:
                if (player2.getChoice() == 0) {
                    return new GameResult("Draw", 0, 0);
                } else if (player2.getChoice() == 1) {
                    player2.getAccount().incrementWins();
                    player1.getAccount().incrementLosses();
                    return new GameResult(player2.getName(), 0, 1);
                } else if (player2.getChoice() == 2) {
                    player1.getAccount().incrementWins();
                    player2.getAccount().incrementLosses();
                    return new GameResult(player1.getName(), 0, 2);
                }
                break;
            case 1:
                if (player2.getChoice() == 0) {
                    player1.getAccount().incrementWins();
                    player2.getAccount().incrementLosses();
                    return new GameResult(player1.getName(), 1, 0);
                } else if (player2.getChoice() == 1) {
                    return new GameResult("Draw", 1, 1);
                } else if (player2.getChoice() == 2) {
                    player2.getAccount().incrementWins();
                    player1.getAccount().incrementLosses();
                    return new GameResult(player2.getName(), 1, 2);
                }
                break;
            case 2:
                if (player2.getChoice() == 0) {
                    player2.getAccount().incrementWins();
                    player1.getAccount().incrementLosses();
                    return new GameResult(player2.getName(), 2, 0);
                } else if (player2.getChoice() == 1) {
                    player1.getAccount().incrementWins();
                    player2.getAccount().incrementLosses();
                    return new GameResult(player1.getName(), 2, 1);
                } else if (player2.getChoice() == 2) {
                    return new GameResult("Draw", 2, 2);
                }
                break;
        }
        return null;
    }

    public String getMatchWinner() {
        double p1Percentage = (double) player1.getAccount().getWins() / MAX_ROUNDS * 100;
        double p2Percentage = (double) player2.getAccount().getWins() / MAX_ROUNDS * 100;

        if (p1Percentage > p2Percentage)
            return player1.getName();
        else if (p1Percentage < p2Percentage)
            return player2.getName();
        else
            return "Draw";

    }

    public String formatResult(GameResult result) {
        return result.toString();
    }

}

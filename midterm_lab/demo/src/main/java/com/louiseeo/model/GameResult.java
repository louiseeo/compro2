package com.louiseeo.model;

/**
 * Represents the result of a single round in the match.
 * Stores the winner and both players' choices,
 * and formats them into a readable string.
 *
 * @author louiseeo
 */
public class GameResult {
    private String winner;
    private int p1Choice;
    private int p2Choice;

    /**
     * Constructs a GameResult for a completed round.
     *
     * @param winner   : username of the winner or "Draw"
     * @param p1Choice : Player 1's choice (0=Rock, 1=Paper, 2=Scissors)
     * @param p2Choice : Player 2's choice (0=Rock, 1=Paper, 2=Scissors)
     */
    public GameResult(String winner, int p1Choice, int p2Choice) {
        this.winner = winner;
        this.p1Choice = p1Choice;
        this.p2Choice = p2Choice;
    }

    /**
     * Returns a human readable result of the round.
     * Converts int choices to words (Rock, Paper, Scissors).
     * @return formatted result string
     */
    @Override
    public String toString() {
        String p1Word = (p1Choice == 0) ? "Rock" : (p1Choice == 1) ? "Paper" : "Scissors";
        String p2Word = (p2Choice == 0) ? "Rock" : (p2Choice == 1) ? "Paper" : "Scissors";

        if (winner.equals("Draw")) {
            return p1Word + " vs. " + p2Word + " > It's a draw!";
        }
        return p1Word + " vs. " + p2Word + " > " + winner + " wins!";
    }

}

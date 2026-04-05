package com.louiseeo.model;

/**
 * Represents the result of a single round in the match.
 * Stores the winner and both players' moves,
 * and formats them into a human readable string.
 *
 * @author louiseeo
 */
public class GameResult {
    private String winner;
    private GameMove p1Move;
    private GameMove p2Move;

    /**
     * Constructs a GameResult for a completed round.
     *
     * @param winner : username of the winner or "Draw"
     * @param p1Move : Player 1's GameMove
     * @param p2Move : Player 2's GameMove
     */
    public GameResult(String winner, GameMove p1Move, GameMove p2Move) {
        this.winner = winner;
        this.p1Move = p1Move;
        this.p2Move = p2Move;
    }

    /**
     * Returns a human readable result of the round.
     *
     * @return formatted result string
     */
    @Override
    public String toString() {
        if (winner.equals("Draw"))
            return p1Move.getMoveName() + " vs. " + p2Move.getMoveName() + " > It's a draw!";
        return p1Move.getMoveName() + " vs. " + p2Move.getMoveName() + " > " + winner + " wins!";
    }
}
package com.louiseeo.model;

/**
 * Abstract base class representing a game move.
 * Concrete subclasses Rock, Paper, and Scissors
 * implement the compare logic.
 *
 * @author louiseeo
 */
public abstract class GameMove {
    private String moveName;

    public GameMove(String moveName) {
        this.moveName = moveName;
    }

    public String getMoveName() {
        return moveName;
    }

    /**
     * Compares this move against another.
     * @param other : the opponent's move
     * @return 1 if this move wins, -1 if it loses, 0 for a tie
     */
    public abstract int compare(GameMove other);
}

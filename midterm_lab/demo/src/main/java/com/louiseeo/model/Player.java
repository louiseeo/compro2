package com.louiseeo.model;

/**
 * Represents an active player in a game session.
 * All fields are private and strictly encapsulated.
 *
 * @author louiseeo
 */
public class Player {
    private String name;
    private int score;
    private GameMove currentMove;

    /**
     * Constructs a Player with a name.
     *
     * @param name : the player's username
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.currentMove = null;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public GameMove getCurrentMove() { return currentMove; }
    public void setCurrentMove(GameMove move) { this.currentMove = move; }
    public void resetMove() { this.currentMove = null; }

    /**
     * Safely increments the player's win count.
     */
    public void incrementScore() { score++; }
}
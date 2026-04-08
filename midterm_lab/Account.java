package com.louiseeo.model;

/**
 * Represents a player's account with persistent data.
 * Stores login credentials and match statistics
 * that are saved across sessions via JSON.
 *
 * @author louiseeo
 */
public class Account {
    private String username;
    private String password;
    private int wins;
    private int losses;

    /**
     * Constructs a new Account with zero wins and losses.
     *
     * @param username : the player's chosen username
     * @param password : the player's chosen password
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.wins = 0;
        this.losses = 0;
    }

    /**
     * Returns the player's username.
     * 
     * @return username as String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the player's password.
     * 
     * @return password as String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the player's total wins across all sessions.
     * 
     * @return wins as int
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the player's total losses across all sessions.
     * 
     * @return losses as int
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Increments the player's win count by 1.
     */
    public void incrementWins() {
        wins++;
    }

    /**
     * Increments the player's loss count by 1.
     */
    public void incrementLosses() {
        losses++;
    }

    /**
     * Adds the soecified amount to the player's win count.
     * Used to record total round wins after the match.
     * 
     * @param amount
     */
    public void addWins(int amount) {
        wins += amount;
    }

    /**
     * Adds the soecified amount to the player's loss count.
     * Used to record total round losses after the match.
     * 
     * @param amount
     */
    public void addLoses(int amount) {
        losses += amount;
    }

    /**
     * Returns the player's win rate as a percentage.
     * Returns 0 if no games played yet.
     * 
     * @return win rate as double
     */
    public double getWinRate() {
        if (wins + losses == 0)
            return 0;
        return (double) wins / (wins + losses) * 100;
    }

    /**
     * Returns a summary of the player's match statistics.
     * 
     * @return formatted string showing wins and losses
     */
    public String getSummary() {
        return String.format("Wins: %d | Losses: %d | Win Rate: %.1f%%", wins, losses, getWinRate());
    }

}

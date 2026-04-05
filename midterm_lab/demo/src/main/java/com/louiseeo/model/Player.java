package com.louiseeo.model;

/**
 * Represents an active player during a game session.
 * Wraps an Account and tracks the player's current
 * round choice. Choice is not saved persistently.
 *
 * @author louiseeo
 */
public class Player {
    private Account account;
    private int choice = -1;

    /**
     * Constructs a Player linked to the given Account.
     *
     * @param account : the logged in Account for this player
     */
    public Player(Account account) {
        this.account = account;
    }

    /**
     * Returns the Account linked to this player.
     * @return Account object
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Returns the player's username from their Account.
     * @return username as String
     */
    public String getUsername() {
        return account.getUsername();
    }

    /**
     * Returns the player's current round choice.
     * Returns -1 if no choice has been made yet.
     * @return choice as int (0=Rock, 1=Paper, 2=Scissors)
     */
    public int getChoice() {
        return choice;
    }

    /**
     * Sets the player's choice for the current round.
     * @param choice : 0=Rock, 1=Paper, 2=Scissors
     */
    public void setChoice(int choice) {
        this.choice = choice;
    }

    /**
     * Resets the player's choice to -1 after each round.
     */
    public void resetChoice() {
        this.choice = -1;
    }

}

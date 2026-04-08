package com.louiseeo.model;

/**
 * Represents a game session between two players.
 * Encapsulates the 10 round logic and protects
 * the state of the match from external modification.
 *
 * @author louiseeo
 */
public class GameSession {
    private Player player1;
    private Player player2;
    private int round;
    private static final int MAX_ROUNDS = 10;

    /**
     * Constructs a GameSession for two players.
     *
     * @param player1 : the first player
     * @param player2 : the second player
     */
    public GameSession(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.round = 0;
    }

    public int getRound() { return round; }
    public boolean isMatchOver() { return round == MAX_ROUNDS; }

    /**
     * Resets both players' moves and increments round.
     */
    public void resetRound() {
        player1.resetMove();
        player2.resetMove();
        round++;
    }

    /**
     * Determines the winner of the current round
     * using each GameMove's compare() method.
     * Updates scores accordingly.
     *
     * @return GameResult containing winner and moves
     */
    public GameResult determineWinner() {
        GameMove m1 = player1.getCurrentMove();
        GameMove m2 = player2.getCurrentMove();
        int result = m1.compare(m2);

        if (result == 0)
            return new GameResult("Draw", m1, m2);
        else if (result == 1) {
            player1.incrementScore();
            return new GameResult(player1.getName(), m1, m2);
        } else {
            player2.incrementScore();
            return new GameResult(player2.getName(), m1, m2);
        }
    }

    /**
     * Formats the GameResult into a readable string.
     *
     * @param result : the GameResult to format
     * @return formatted result string
     */
    public String formatResult(GameResult result) {
        return result.toString();
    }
}
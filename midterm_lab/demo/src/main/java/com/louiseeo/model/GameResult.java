package com.louiseeo.model;

public class GameResult {
    private String winner;
    private String message;
    private String p1Choice;
    private String p2Choice;

    public GameResult(String winner, String message, String p1Choice, String p2Choice) {
        this.winner = winner;
        this.message = message;
        this.p1Choice = p1Choice;
        this.p2Choice = p2Choice;
    }

    public String getWinner() {
        return winner;
    }

    public String getMessage() {
        return message;
    }

    public String getP1Choice() {
        return p1Choice;
    }

    public String getP2Choice() {
        return p2Choice;
    }

    public String toString() {
        return String.format(p1Choice + " vs. " + p2Choice + " > " + winner + " wins!");
    }

}

package com.louiseeo.model;

public class GameResult {
    private String winner;
    private int p1Choice;
    private int p2Choice;

    public GameResult(String winner, int p1Choice, int p2Choice) {
        this.winner = winner;
        this.p1Choice = p1Choice;
        this.p2Choice = p2Choice;
    }

    public String getWinner() {
        return winner;
    }


    public int getP1Choice() {
        return p1Choice;
    }

    public int getP2Choice() {
        return p2Choice;
    }

    @Override
    public String toString() {
        if (winner.equals("Draw")) {
            return p1Choice + " vs. " + p2Choice + " > It's a draw!";
        }
        return p1Choice + " vs. " + p2Choice + " > " + winner + " wins!";
    }

}

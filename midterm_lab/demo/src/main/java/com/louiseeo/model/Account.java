package com.louiseeo.model;

public class Account {
    private String username;
    private String password;
    private int wins;
    private int losses;

    public Account(String username, String password, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    public String getSummary() {
        return "Wins: " + wins + " || Losses: " + losses;
    }

}

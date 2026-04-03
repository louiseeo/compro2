package com.louiseeo.model;

public class Account {
    private String username;
    private String password;
    private int wins;
    private int losses;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.wins = 0;
        this.losses = 0;
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

    public double getWinPercentage() {
        if (wins + losses == 0)
            return 0;
        return (double) wins / (wins + losses) * 100;
    }

}

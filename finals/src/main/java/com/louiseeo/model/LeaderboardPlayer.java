package com.louiseeo.model;

public class LeaderboardPlayer {

    private String username;
    private int points;

    public LeaderboardPlayer() {
    }

    public LeaderboardPlayer(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
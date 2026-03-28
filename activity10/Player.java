package com.louiseeo.model;

public class Player {
    private String name;
    private int score;
    private String password;

    public Player(String name, int score, String password) {
        this.name = name;
        this.score = score;
        this.password = password;
    }
    public Player(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return String.format("""
                Player name: %s
                Score: %d
                """, name, score);
    }
}
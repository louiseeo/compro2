package com.louiseeo.model;

public abstract class Player {
    private String username;
    private String word;

    public Player(String username, String word) {
        this.username = username;
        this.word = word;
    }

    public String getUsername() {
        return username;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public abstract String getRole();

}
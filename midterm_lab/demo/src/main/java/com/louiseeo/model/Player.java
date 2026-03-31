package com.louiseeo.model;

public class Player {
    private Account account;
    private String choice;

    public Player(Account account, String choice) {
        this.account = account;
        this.choice = choice;
    }

    public Account getAccount() {
        return account;
    }

    public String getChoice() {
        return choice;
    }

    public String getName() {
        return account.getUsername();
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void resetChoice() {
        this.choice = null;
    }
    
}

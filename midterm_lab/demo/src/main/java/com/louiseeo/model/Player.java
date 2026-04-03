package com.louiseeo.model;

public class Player {
    private Account account;
    private int choice;

    public Player(Account account, int choice) {
        this.account = account;
        this.choice = choice;
    }

    public Account getAccount() {
        return account;
    }

    public int getChoice() {
        return choice;
    }

    public String getName() {
        return account.getUsername();
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public void resetChoice() {
        this.choice = -1;
    }
    
}

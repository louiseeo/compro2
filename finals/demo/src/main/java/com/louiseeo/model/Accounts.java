package com.louiseeo.model;

/**
 * Represents a player account.
 * Stores username and password data.
 *
 * @author louiseeo
 */
public class Accounts {
    private String username;
    private String password;

    public Accounts() {}

    public Accounts(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

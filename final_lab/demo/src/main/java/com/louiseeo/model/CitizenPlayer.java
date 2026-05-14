package com.louiseeo.model;

public class CitizenPlayer extends Player{

    public CitizenPlayer(String username, String word) {
        super(username, word);
    }

    @Override
    public String getRole() {
        return "Citizen";
    }
}

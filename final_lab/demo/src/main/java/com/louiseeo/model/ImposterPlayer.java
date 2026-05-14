package com.louiseeo.model;

public class ImposterPlayer extends Player {

    public ImposterPlayer(String username, String word) {
        super(username, word);
    }

    @Override
    public String getRole() {
        return "Imposter";
    }
}

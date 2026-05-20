package com.louiseeo.model;

/**
 * Represents a citizen player.
 * Citizens know the secret word and
 * attempt to identify the imposter.
 *
 * @author louiseeo
 */
public class CitizenPlayer extends Player{

    public CitizenPlayer(String username, String word) {
        super(username, word);
    }

    @Override
    public String getRole() {
        return "Citizen";
    }
}

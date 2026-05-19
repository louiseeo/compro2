package com.louiseeo.model;

/**
 * Represents the imposter player.
 * The imposter only receives a hint
 * and must blend in with the citizens.
 *
 * @author louiseeo
 */
public class ImposterPlayer extends Player {

    public ImposterPlayer(String username, String word) {
        super(username, word);
    }

    @Override
    public String getRole() {
        return "Imposter";
    }
}

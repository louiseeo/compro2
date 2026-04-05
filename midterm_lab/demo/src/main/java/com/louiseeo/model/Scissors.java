package com.louiseeo.model;

/**
 * Represents the Scissors move.
 * Scissors beats Paper, loses to Rock, ties with Scissors.
 *
 * @author louiseeo
 */
public class Scissors extends GameMove {

    public Scissors() {
        super("Scissors");
    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Scissors) return 0;
        if (other instanceof Paper) return 1;
        return -1; // loses to Rock
    }
}
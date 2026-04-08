package com.louiseeo.model;

/**
 * Represents the Rock move.
 * Rock beats Scissors, loses to Paper, ties with Rock.
 *
 * @author louiseeo
 */
public class Rock extends GameMove {

    public Rock() {
        super("Rock");
    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Rock) return 0;
        if (other instanceof Scissors) return 1;
        return -1; // loses to Paper
    }
}
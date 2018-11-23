package com.questmark.input;

/**
 * Represents one of the four cardinal directions.
 *
 * @author Ming Li
 */
public enum Direction {

    Up(0),
    Down(1),
    Left(2),
    Right(3);

    private final int dir;

    Direction(int dir) {
        this.dir = dir;
    }

    public int toNumeric() {
        return dir;
    }

}

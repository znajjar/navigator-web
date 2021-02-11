package com.world.navigator.game;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public Direction getRight() {
        switch (this) {
            case EAST -> {
                return SOUTH;
            }
            case WEST -> {
                return NORTH;
            }
            case NORTH -> {
                return EAST;
            }
            default -> {
                return WEST;
            }
        }
    }

    public Direction getLeft() {
        return getRight().getRight().getRight();
    }

    public Direction getOpposite() {
        return getRight().getRight();
    }
}

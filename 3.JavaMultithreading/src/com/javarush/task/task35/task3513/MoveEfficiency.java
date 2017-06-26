package com.javarush.task.task35.task3513;

/**
 * Created by ASentsov on 26.06.2017.
 */
public class MoveEfficiency implements Comparable<MoveEfficiency>{
    private int numberOfEmptyTiles, score;
    private Move move;

    public Move getMove() {
        return move;
    }

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    @Override
    public int compareTo(MoveEfficiency another) {
        if (numberOfEmptyTiles != another.numberOfEmptyTiles) {
            return Integer.compare(numberOfEmptyTiles, another.numberOfEmptyTiles);
        } else {
            return Integer.compare(score, another.score);
        }
    }
}


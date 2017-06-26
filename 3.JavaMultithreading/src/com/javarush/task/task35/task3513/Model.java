package com.javarush.task.task35.task3513;

import java.util.*;

/**
 * Created by ASentsov on 23.06.2017.
 */
public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    public int score, maxTile;
    public Stack<Tile[][]> previousStates = new Stack<>();
    public Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    private List<Tile> getEmptyTiles(){
        List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty())
                    emptyTiles.add(gameTiles[i][j]);

            }
        }
        return emptyTiles;
    }

    private void addTile(){
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            int k = (int) (emptyTiles.size() * Math.random());
            int newVal = Math.random() < 0.9 ? 2 : 4;
            emptyTiles.get(k).setValue(newVal);
        }
    }

    public void resetGameTiles(){
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
        score = 0;
        maxTile = 2;
    }

    private boolean compressTiles(Tile[] tiles){
        boolean wasChanged = true;
        boolean wasCompressed = false;
        while (wasChanged){
            wasChanged = false;
            for (int i = 0; i < tiles.length-1; i++) {
                if ((tiles[i].isEmpty()) && (!tiles[i+1].isEmpty())){
                    Tile tmp = tiles[i];
                    tiles[i] = tiles[i+1];
                    tiles[i+1] = tmp;
                    wasChanged = true;
                    wasCompressed = true;
                }
            }
        }
        return wasCompressed;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean wasMerged = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if ((!tiles[i].isEmpty()) && (tiles[i].value == tiles[i + 1].value)) {
                tiles[i].setValue(tiles[i].value * 2);
                for (int j = i + 1; j < tiles.length - 1; j++) {
                    tiles[j] = tiles[j + 1];
                }
                tiles[tiles.length - 1] = new Tile();

                score += tiles[i].value;
                if (maxTile < tiles[i].value)
                    maxTile = tiles[i].value;

                wasMerged = true;
            }
        }
        return wasMerged;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    private void rotateGameField(){
        Tile[][] tmpTile = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tmpTile[i][j] = gameTiles[FIELD_WIDTH - j -1][i];
            }
        }
        gameTiles = tmpTile;
    }

    void right(){
        saveState(gameTiles);
        rotateGameField();
        rotateGameField();
        left();
        rotateGameField();
        rotateGameField();
    }

    void up(){
        saveState(gameTiles);
        rotateGameField();
        rotateGameField();
        rotateGameField();
        left();
        rotateGameField();

    }

    void down(){
        saveState(gameTiles);
        rotateGameField();
        left();
        rotateGameField();
        rotateGameField();
        rotateGameField();
    }

    void left(){
        if (isSaveNeeded)
            saveState(gameTiles);
        boolean wasChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            wasChanged = wasChanged | (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i]));
        }
        if (wasChanged)
            addTile();
        isSaveNeeded = true;
    }

    public boolean canMove(){
        for (int i = 0; i < FIELD_WIDTH-1; i++) {
            for (int j = 0; j < FIELD_WIDTH-1; j++) {
                if ((gameTiles[i][j].value == 0) ||
                        (gameTiles[i][j].value == gameTiles[i][j+1].value) ||
                        (gameTiles[i][j].value == gameTiles[i+1][j].value))
                    return true;
            }
        }
        if ((gameTiles[FIELD_WIDTH-1][FIELD_WIDTH-1].value == 0) ||
                (gameTiles[FIELD_WIDTH-1][FIELD_WIDTH-1].value == gameTiles[FIELD_WIDTH-1][FIELD_WIDTH-2].value) ||
                (gameTiles[FIELD_WIDTH-1][FIELD_WIDTH-1].value == gameTiles[FIELD_WIDTH-2][FIELD_WIDTH-1].value))
            return true;
        return false;
    }

    private void saveState(Tile[][] tiles){
        Tile[][] new_tiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                new_tiles[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(new_tiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }
    public void rollback(){
        if ((!previousStates.isEmpty()) && (!previousScores.isEmpty())) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public void randomMove(){
        int k = ((int) (Math.random() * 100)) % 4;
        switch (k){
            case 0: left(); break;
            case 1: right(); break;
            case 2: up(); break;
            case 3: down(); break;
        }
    }

    public boolean hasBoardChanged(){
        Tile[][] prev_tiles = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (prev_tiles[i][j].value != gameTiles[i][j].value)
                    return true;
            }
        }
        return false;
    }

    public MoveEfficiency getMoveEfficiency(Move move){
        move.move();
        MoveEfficiency resEfficiency;
        if (!hasBoardChanged()){
            resEfficiency = new MoveEfficiency(-1,0,move);
        }
        else {
            resEfficiency = new MoveEfficiency(getEmptyTiles().size(),score,move);
        }
        rollback();
        return resEfficiency;
    }

    public void autoMove(){
        PriorityQueue<MoveEfficiency> pq = new PriorityQueue(4,Collections.reverseOrder());
        pq.add(getMoveEfficiency(this::left));
        pq.add(getMoveEfficiency(this::up));
        pq.add(getMoveEfficiency(this::down));
        pq.add(getMoveEfficiency(this::right));
        pq.peek().getMove().move();
    }
}

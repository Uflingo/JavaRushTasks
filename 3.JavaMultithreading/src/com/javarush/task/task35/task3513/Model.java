package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ASentsov on 23.06.2017.
 */
public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    public int score, maxTile;


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
        rotateGameField();
        rotateGameField();
        left();
        rotateGameField();
        rotateGameField();
    }

    void up(){
        rotateGameField();
        rotateGameField();
        rotateGameField();
        left();
        rotateGameField();

    }

    void down(){
        rotateGameField();
        left();
        rotateGameField();
        rotateGameField();
        rotateGameField();
    }

    void left(){
        boolean wasChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            wasChanged = wasChanged | (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i]));
        }
        if (wasChanged)
            addTile();
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

}

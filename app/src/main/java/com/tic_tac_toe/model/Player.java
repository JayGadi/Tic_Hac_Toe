package com.tic_tac_toe.model;

import java.io.Serializable;

/**
 * Created by Jay on 12/9/2015.
 */
public class Player implements Serializable {

    private String name;
    private char playerMarker;
    private int score;

    public Player(String name, char playerMarker, int score){
        this.name = name;
        this.playerMarker = playerMarker;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getPlayerMarker() {
        return playerMarker;
    }

    public void setPlayerMarker(char playerMarker) {
        this.playerMarker = playerMarker;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

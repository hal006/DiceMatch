package com.example.hal.dicematch.gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom class to interpret Game logic,
 */
public class Game implements Serializable {

    int humanPlayers;
    int aiPlayers;
    int roundNumber;
    int activePlayer;
    List<Player> players;
    int rows;

    /**
     * @param aiPlayers not yet used / prepared for future development
     * @param humanPlayers not yet used/ prepared for future development
     */
    public Game(int humanPlayers, int aiPlayers) {
        this.humanPlayers = humanPlayers;
        this.aiPlayers = aiPlayers;
        this.roundNumber = 0;
        this.activePlayer = 0;
        this.rows = 16;
        this.players = new ArrayList<>();
        for (int i = 0; i < humanPlayers; i++) {
            this.players.add(new Player(rows));
        }
    }

    public Player getActivePlayer () {
        return this.players.get(activePlayer);
    }

    /**
     * Method prepared for future development.
     */
    void nextPlayer () {
        this.activePlayer = (this.activePlayer + 1) % (humanPlayers+aiPlayers);
        if ((this.activePlayer + 1) / (humanPlayers+aiPlayers) > 0) {
            this.nextRound();
        }
    }

    void nextRound () {
        this.roundNumber++;
    }
}
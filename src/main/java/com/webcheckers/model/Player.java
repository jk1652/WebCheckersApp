package com.webcheckers.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A single player
 *
 * @author Zane Kitchen Lipski
 */
public class Player {
    private static final Logger LOG = Logger.getLogger(Player.class.getName());
    private String name;
    private Map<String, Game> saved;

    /**
     * Constructor for Player, creates player with name
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * returns name of player
     */
    public String getName() {
        return this.name;
    }

    public void saveGame(Game game) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String gameInfo = "";
        gameInfo = "vs. " + game.getOpponentName(this.name) + " @ " + formatter.format(date);
        saved.put(gameInfo, game);
    }

    /**
     * checks if name is same between players as it should be unique
     */
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Player)) {
            return false;
        }
        Player objPlayer = (Player) obj;
        return objPlayer.getName().equals(this.getName());
    }


}

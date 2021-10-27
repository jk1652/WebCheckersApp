package com.webcheckers.model;


import java.util.List;
import java.util.logging.Logger;

/**
 * A single player
 *
 * @author Zane Kitchen Lipski
 */
public class Player {
    private static final Logger LOG = Logger.getLogger(Player.class.getName());
    private String name;

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

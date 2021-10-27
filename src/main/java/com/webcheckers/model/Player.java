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
     *
     * @param name name of player
     */
    public Player(String name) {
        //LOG.fine("user created " + name);
        this.name = name;
    }

    /**
     *
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param obj the player to be compared
     * @return if they are equal or not
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

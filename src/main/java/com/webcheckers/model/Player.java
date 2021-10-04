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

    public Player(String name) {
        LOG.fine("user created " + name);
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Player)) {
            return false;
        }
        Player objPlayer = (Player) obj;
        return objPlayer.getName().equals(this.getName());
    }

}

package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayerLobby {
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    private ArrayList<Player> names;

    public PlayerLobby() {
        names = new ArrayList<>();
    }

    /**
     * adds player to player list
     * @param name
     * @return false if cannot add player OR
     * true if player was added
     */
    public Boolean addPlayer(String name) {
        Player player = new Player(name);
        if(names.contains(player)) {
            return false;
        }
        names.add(player);

        System.out.println(player.getName());

        return true;
    }







}

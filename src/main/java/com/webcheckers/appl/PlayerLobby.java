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
        if (name == null) {
            return false;
        }

        Player player = new Player(name);
        if(names.contains(player)) {
            return false;
        }
        LOG.fine("user added " + name);
        names.add(player);
        return true;
    }

    public int numberofPlayers() {
        return names.size();
    }


    public ArrayList<Player> getPlayerList() {
        return names;
    }

    public Boolean isValidName(String name){
        if (name.matches("[a-zA-Z_0-9]+([a-zA-Z_0-9]|\\s)*$")) {
            return true;
        }
        else {
            return false;
        }
    }







}

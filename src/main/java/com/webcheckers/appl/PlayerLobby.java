package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

public class PlayerLobby {
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    private final ArrayList<Player> players;

    public PlayerLobby() {
        players = new ArrayList<>();
    }

    /**
     * adds player to player list
     * @param name name of player
     * @return false if cannot add player OR
     * true if player was added
     */
    public Boolean addPlayer(String name) {
        if (name == null) {
            return false;
        }

        Player player = new Player(name);
        if(players.contains(player)) {
            return false;
        }
        LOG.fine("user added " + name);
        players.add(player);
        return true;
    }

    public Boolean checkPlayerExist(String name) {
        Player player = new Player(name);
        return players.contains(player);
    }

    public int numberOfPlayers() {
        return players.size();
    }


    public ArrayList<Player> getPlayerList() {
        return players;
    }

    public Boolean isValidName(String name){
        return name.matches("[a-zA-Z_0-9]+([a-zA-Z_0-9]|\\s)*$");
    }

    public void removePlayer(String name) {
        Player player = new Player(name);

        players.remove(player);

    }

    public Player getPlayer(String name) {
        for (Player x : players) {
            if (x.getName().equals(name)) {
                return x;
            }
        }
        return null;
    }


}

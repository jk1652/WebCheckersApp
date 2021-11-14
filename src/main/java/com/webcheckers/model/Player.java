package com.webcheckers.model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A single player
 *
 * @author Zane Kitchen Lipski
 */
public class Player {
    private final String name;
    private boolean Saved_Games_went_up = false;
    private final Map<String, Game> saved = new HashMap<>();

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
        String gameInfo;
        if(game.getSinglePlayer()){
            gameInfo = "vs. " + game.getAIOpponentDifficulty() + " AI @ " + formatter.format(date);
        }
        else{
            gameInfo = "vs. " + game.getOpponentName(this.name) + " @ " + formatter.format(date);
        }
        saved.put(gameInfo, game);
    }

    public void removeSaveGame(String key) {
        saved.remove(key);
    }

    public Map<String, Game> getSaved() {
        return saved;
    }

    public void savedGamesDidGoUp() {
        Saved_Games_went_up = true;
    }

    public void savedGamesOnLVL() {
        Saved_Games_went_up = false;
    }

    public boolean currentSavedGamesWentUp() {
        return Saved_Games_went_up;
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

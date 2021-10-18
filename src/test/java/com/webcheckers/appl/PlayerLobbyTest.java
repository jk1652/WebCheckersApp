package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



@Tag("Application-tier")
public class PlayerLobbyTest {

    @Test
    public void test_addPlayer() {
        final PlayerLobby CuT = new PlayerLobby();

        // try to add player
        CuT.addPlayer("test");

        // player succsefully added
        assertEquals(true, CuT.checkPlayerExist("test"));

        // cannot add player that already exsists
        assertEquals(false, CuT.addPlayer("test"));

    }

    @Test
    public void test_checkPlayerExist() {
        final PlayerLobby CuT = new PlayerLobby();

        // player was added
        CuT.addPlayer("test");

        // check that they were added
        assertEquals(true, CuT.checkPlayerExist("test"));

        // check that some player not added is not there
        assertEquals(false, CuT.checkPlayerExist("check"));

    }

    @Test
    public void test_isValidName() {
        final PlayerLobby CuT = new PlayerLobby();

        // valid login name check
        assertEquals(true, CuT.isValidName("test"));

        // invalid login name check
        assertEquals(false, CuT.isValidName(""));
    }

    @Test
    public void test_removePlayer() {
        final PlayerLobby CuT = new PlayerLobby();

        // add player
        CuT.addPlayer("test");
        // check they are there
        assertEquals(true, CuT.checkPlayerExist("test"));

        // remove player
        CuT.removePlayer("test");
        // check they are gone
        assertEquals(false, CuT.checkPlayerExist("test"));
    }
}

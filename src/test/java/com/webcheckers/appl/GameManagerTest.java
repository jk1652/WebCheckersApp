package com.webcheckers.appl;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Application-tier")
public class GameManagerTest {
    @Test
    public void test_getGame(){
        GameManager CuT = new GameManager();

        Game mockgame = CuT.createGame(mock(Player.class),mock(Player.class));

        assertNull(CuT.getInactiveGame(mockgame.getGameID()));

        assertEquals(mockgame,CuT.getActiveGame(mockgame.getGameID()));

        assertNull(CuT.getActiveGame(234345));


    }

    @Test
    public void test_findPlayerGame() {
        GameManager CuT = new GameManager();

        Game mockgame = CuT.createGame(mock(Player.class),mock(Player.class));

        assertEquals(mockgame,CuT.findPlayerGame(mockgame.getWhitePlayer()));

        assertEquals(mockgame,CuT.findPlayerGame(mockgame.getRedPlayer()));

        assertNull(CuT.findPlayerGame(mock(Player.class)));
    }
}

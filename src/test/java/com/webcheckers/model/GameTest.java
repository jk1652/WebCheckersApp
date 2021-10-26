package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class GameTest {

    @Test
    public void test_viewMode(){
        Game CuT = new Game(mock(Player.class),mock(Player.class));
        assertTrue(CuT.isPermittedViewMode(Game.View.PLAY,CuT.getRedPlayer()));

        assertTrue(CuT.isPermittedViewMode(Game.View.PLAY,CuT.getWhitePlayer()));

        assertFalse(CuT.isPermittedViewMode(Game.View.PLAY,mock(Player.class)));
    }


    @Test
    public void test_isParticipant() {
        Game CuT = new Game(mock(Player.class),mock(Player.class));

        assertTrue(CuT.isParticipant(CuT.getWhitePlayer()));

        assertTrue(CuT.isParticipant(CuT.getRedPlayer()));

        assertFalse(CuT.isParticipant(mock(Player.class)));
    }
}

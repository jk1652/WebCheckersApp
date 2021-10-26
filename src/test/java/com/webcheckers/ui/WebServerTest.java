package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.TemplateEngine;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("UI-tier")
class WebServerTest {

    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameManager gameManager;
    private Gson gson;

    @BeforeEach
    public void setup() {
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        gson = new Gson();
    }

    @Test
    public void testInitialize(){
        WebServer server = new WebServer(engine,playerLobby,gameManager,gson);
        try{server.initialize();}
        catch(Exception exception){fail();}
    }
}
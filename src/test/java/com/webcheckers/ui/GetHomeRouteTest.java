package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class GetHomeRouteTest {

    private GetHomeRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private PlayerLobby playerLobby;
    private GameManager gameManager;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = new PlayerLobby();
        gameManager = new GameManager();


        // create a unique CuT for each test

        CuT = new GetHomeRoute(engine, playerLobby, gameManager);
    }

    @Test
    public void NewPage() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());


        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

    }

    @Test
    public void GameExists() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        gameManager.createGame("test", "test2");


        try {
            CuT.handle(request, response);

        } catch (HaltException e) {
            // expected
        }
    }

    @Test
    public void AddPlayer() {
        when(session.attribute(PostSignInRoute.USERNAME)).thenReturn("test");

        playerLobby.addPlayer("test");

        playerLobby.addPlayer("test2");


        try {
            CuT.handle(request, response);

        } catch (HaltException e) {
            // expected
        }


    }

    @Test
    public void messageExists() {
        when(session.attribute("message")).thenReturn("test");


        assertNotNull(session.attribute("message"));


        try {
            CuT.handle(request, response);
        } catch (HaltException e) {
            // expected
        }



    }

}

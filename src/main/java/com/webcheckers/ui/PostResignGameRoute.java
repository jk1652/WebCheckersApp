package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.util.Message;
import spark.*;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class PostResignGameRoute implements Route {

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    public PostResignGameRoute(final TemplateEngine templateEngine, final GameManager gameManager){
        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, Object> vm = new HashMap<>();

        String name = request.session().attribute(PostSignInRoute.USERNAME); //get player's name
        gameManager.finishGame(name);

        Message msg = Message.error("Resigned from game, please redirect to the home page!");

        //request.session().attribute("message", msg);
        //vm.put("message", msg);

        //request.session().attribute(msg.getText());
        //return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        //response.redirect(WebServer.HOME_URL);
        //halt();

        // TODO:
        // MAKE PAGE REDIRECT

        return msg; //dont do this
    }
}

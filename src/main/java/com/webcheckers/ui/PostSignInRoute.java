package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static spark.Spark.halt;

public class PostSignInRoute implements Route {

    static final String  USERNAME_IN_USE = "Invalid Username: Username in use.";

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param playerLobby
     *    {@Link PlayerLobby} that holds over statistics
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NullPointerException
     *    when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    PostSignInRoute(PLayerLobby playerLobby, TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    //
    // TemplateViewRoute method
    //

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException
     *    when an invalid result is returned after making a guess
     */
    @Override
    public String handle(Request request, Response response) {
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
        vm.put(GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);

        // retrieve the game object
        final Session session = request.session();
        final PlayerServices playerServices = session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if(playerServices != null) {
            vm.put(GetGameRoute.GAME_BEGINS_ATTR, playerServices.isStartingGame());
            vm.put(GetGameRoute.GUESSES_LEFT_ATTR, playerServices.guessesLeft());

            // retrieve request parameter
            final String guessStr = request.queryParams(GUESS_PARAM);

            // convert the input
            int guess = -1;
            try {
                guess = Integer.parseInt(guessStr);
            } catch (NumberFormatException e) {
                // re-display the guess form with an error message
                return templateEngine.render(error(vm, makeBadArgMessage(guessStr)));
            }

            // make the guess and create the appropriate ModelAndView for rendering
            ModelAndView mv;
            switch (playerServices.makeGuess(guess)) {
                case INVALID:
                    mv = error(vm, makeInvalidArgMessage(guessStr));
                    break;

                case WRONG:
                    vm.put(GetGameRoute.GUESSES_LEFT_ATTR, playerServices.guessesLeft());
                    mv = error(vm, BAD_GUESS);
                    break;

                case WON:
                    mv = youWon(vm, playerServices);
                    break;

                case LOST:
                    mv = youLost(vm, playerServices);
                    break;

                default:
                    // All the GuessResult values are in case statements so we should never get here.
                    throw new NoSuchElementException("Invalid result of guess received.");
            }

            return templateEngine.render(mv);
        }
        else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }

    //
    // Private methods
    //

    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, VIEW_NAME);
    }

    private ModelAndView youWon(final Map<String, Object> vm, final PlayerServices playerServices) {
        return endGame(true, vm, playerServices);
    }

    private ModelAndView youLost(final Map<String, Object> vm, final PlayerServices playerServices) {
        return endGame(false, vm, playerServices);
    }

    private ModelAndView endGame(final boolean youWonLost, final Map<String, Object> vm,
                                 final PlayerServices playerServices) {
        playerServices.finishedGame();
        // report application-wide game statistics
        vm.put(GetHomeRoute.GAME_STATS_MSG_ATTR, gameCenter.getGameStatsMessage());
        vm.put(YOU_WON_ATTR, youWonLost);
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }
}

package ch.uzh.ifi.hase.soprafs21.controller;

//TODO see if class game should exist as entity like user
// TODO frage was braucht die Scoreboard klasse?

import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * GameController is used to manage incoming REST request coming from the client
 * that are related to the gameplay itself
 */
public class GameController {

    private final String mainGame = "/mainGame"; // TODO this ok??

   // private user

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }

    @PostMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void initGame() {
//        game init
//        - 16 Bilder für Gameboard auswählen
//        - coordinaten zuweisen
//        - sets zuweisen
        // TODO was gibt man ans FE zurück?
    }

    public void showScreenshots(){
        // return user list or pictures list
    }

    @PutMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    public void updateGuess(){
        // TODO
    }

    @GetMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getUserPoints(){
        // TODO
    }

    @GetMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void exitGame(){
        // TODO
    }

    @GetMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void playAgain(){
        // TODO
    }



}

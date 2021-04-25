package ch.uzh.ifi.hase.soprafs21.controller;

//TODO see if class game should exist as entity like user
// TODO frage was braucht die Scoreboard klasse?

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PicturesGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GameController is used to manage incoming REST request coming from the client
 * that are related to the gameplay itself
 */
public class GameController {

    private final String mainGame = "/board";
    private final String pictures = "/pictures";
    private final String guesses = "/guess";

    // TODO this ok??

   // private user

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }

    @PostMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void initGame() {
        // game init
        // - 16 Bilder für Gameboard auswählen  ==> separat  gemacht da es mehrmals benötigt werden wird
        // - coordinaten zuweisen ==> coordinaten werden ebenfalls bereits mit den bildern zugewiesen
        // - sets zuweisen

        // TODO get users names list from FE
        String[] userNames = {"a", "b", "c"};

        gameService.initGame(userNames);
        User[] usersList = gameService.getPlayingUsers(userNames);

        // TODO return DTO mapper instance
        // return DTOMapper.INSTANCE.convertUserPostDTOtoEntity(usersList);

    }

    @PostMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void postUserGuesses() {
        int[] userGuesses = {0, 15, 4, 5}; // TODO get user guesses from FE

        gameService.handleGuesses(userGuesses,"Muster");

    }

    public void showScreenshots(){
        // return user list or pictures list
    }


    @PutMapping(guesses)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitGuesses(@RequestBody UserPutDTO userPutDTO){
       User  currentUser =  DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
       Long userID = currentUser.getId();
       Map<User,String> guesses =  currentUser.getGuesses();
       gameService.handleGuesses(guesses,userID);
    }

    /**
     * Used to send a List of picture Elements to frontend
     * Pictures are already mapped to a coordinate.
     */
    @GetMapping(pictures)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PicturesGetDTO> getPictureURL(){

        List<Picture> pictures = gameService.selectPictures();
        List<PicturesGetDTO> picturesGetDTOs = new ArrayList();
        for(Picture picture : pictures){
           // picturesGetDTOs.add(DTOMapper.INSTANCE.convertEntityTOPicturesGetDTO(picture));
        }

        return picturesGetDTOs;
    }


    @GetMapping(guesses)
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

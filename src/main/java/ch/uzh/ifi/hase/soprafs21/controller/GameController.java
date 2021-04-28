package ch.uzh.ifi.hase.soprafs21.controller;

//TODO see if class game should exist as entity like user
// TODO frage was braucht die Scoreboard klasse?

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
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
@RestController
public class GameController {

    private final String mainGame = "/board";
    private final String pictures = "/pictures";
    private final String guesses = "/guess";

    private final GameService gameService;

    GameController(GameService gameService){ this.gameService = gameService; }

    @GetMapping("/board")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> initGame() {

        // TODO get users names list from FE
        String[] userNames = {"a", "b", "c", "d"};
        //List<User> users = userService.getUsers();

        gameService.initGame(userNames);
        User[] usersList = gameService.getPlayingUsers(userNames);

        List<UserGetDTO> initedUsersDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : usersList) {
            initedUsersDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }

        return initedUsersDTOs;
    }

    @PostMapping("/board/guess")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void postUserGuesses() {
        int[] userGuesses = {0, 15, 4, 5}; // TODO get user guesses from FE

        //gameService.handleGuesses(userGuesses,"Muster");

    }

    public void showScreenshots(){
        // return user list or pictures list
    }

    @PutMapping(guesses)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitGuesses(@RequestBody UserPutDTO userPutDTO){
       User currentUser = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
       //Long userID = currentUser.getId();
       //Map<User,String> guesses =  currentUser.getGuesses();
       gameService.handleGuesses(currentUser);
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


//    @GetMapping(guesses)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void getUserPoints(){
//        // TODO
//    }
//
//    @GetMapping(mainGame)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void exitGame(){
//        // TODO
//    }
//
//    @GetMapping(mainGame)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void playAgain(){
//        // TODO
//    }



}

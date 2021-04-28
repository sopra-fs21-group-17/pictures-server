package ch.uzh.ifi.hase.soprafs21.controller;

//TODO see if class game should exist as entity like user
// TODO frage was braucht die Scoreboard klasse?

import ch.uzh.ifi.hase.soprafs21.entity.Screenshot;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * GameController is used to manage incoming REST request coming from the client
 * that are related to the gameplay itself
 */
public class GameController {

    private final String mainGame = "/board";
    private final String pictures = "/pictures";
    private final String guesses = "/guess";
    private final String picture = "/picture";
    private final String screenshot = "/screenshot";



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

        //gameService.handleGuesses(userGuesses,"Muster");

    }

    /**
     * Used to save screenshot URLs to the Back end
     * @param screenshotPutDTO
     */
    @PutMapping(screenshot)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveScreenshots(@RequestBody ScreenshotPutDTO screenshotPutDTO){
        Screenshot submittedShot = DTOMapper.INSTANCE.convertScreenshotPutDTOtoEntity(screenshotPutDTO);
        gameService.saveScreenshot(submittedShot);
    }

    /**
     *
     * @return Return a List of Screenshots for the guessing screen
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ScreenshotGetDTO> showScreenshots(){
        List<Screenshot> screenshots = gameService.getScreenshots();
        List<ScreenshotGetDTO> screenshotGetDTOs = new ArrayList<>();
        for(Screenshot shot : screenshots){
            screenshotGetDTOs.add(DTOMapper.INSTANCE.convertEntityToScreenshotGetDTO(shot));
        }
        return screenshotGetDTOs;
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

        List<Picture> pictures = gameService.getListOfPictures();  // is changed to take from gameplay
        List<PicturesGetDTO> picturesGetDTOs = new ArrayList();
        for(Picture picture : pictures){
           // picturesGetDTOs.add(DTOMapper.INSTANCE.convertEntityTOPicturesGetDTO(picture));
        }

        return picturesGetDTOs;
    }

    /**
     *
     * @param userGetDTO
     * @return returns a Gameplay DTO that will contain a single pictureURL that has been stored in the gameplay entity
     */
    @GetMapping(picture)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PicturesGetDTO getCorrespondingPicture(@RequestBody UserGetDTO userGetDTO){
        User currentUser = DTOMapper.INSTANCE.convertUserGetDTOtoEntity(userGetDTO);
        int assignedToken = currentUser.getAssignedCoordinates();

        Picture correspondingPicture = gameService.getCorrespondingToUser(assignedToken);
        PicturesGetDTO pictureResult =  DTOMapper.INSTANCE.convertEntityToPicturesGetDTO(correspondingPicture);
        return pictureResult;
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
        // TODO will have to delete the corresponding Gameplay instance using the gameID
    }

    @GetMapping(mainGame)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void playAgain(){
        // TODO will have to reset the Gameplay instances
    }



}

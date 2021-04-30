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
import java.util.Map;

/**
 * GameController is used to manage incoming REST request coming from the client
 * that are related to the gameplay itself
 */
@RestController
public class GameController {

    // TODO delete this...
    private final String mainGame = "/board";
    private final String pictures = "/pictures";
    private final String guesses = "/guess";
    private final String picture = "/picture";
    private final String screenshot = "/screenshot";

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

//    @PostMapping(mainGame)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void postUserGuesses() {
//        int[] userGuesses = {0, 15, 4, 5}; // TODO get user guesses from FE
//
//        //gameService.handleGuesses(userGuesses,"Muster");
//
//    }

    @GetMapping("/screenshots")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ArrayList<String>> getScreenshots(){
        ArrayList<ArrayList<String>> response = gameService.getUsersScreenshots();

        return response;
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
    @GetMapping(screenshot)
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

    @PutMapping("/guesses")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitGuesses(@RequestBody UserPutDTO userPutDTO){
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        gameService.handleGuesses(user);
    }

    @GetMapping("/correctedGuesses")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Map<String, String>> returnCorrectedGuesses(){
        Map<String, Map<String, String>> correctedGuesses = gameService.returnCorrectedGuesses();
        System.out.println(correctedGuesses.values());

        return correctedGuesses;
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
            picturesGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPicturesGetDTO(picture));
        }

        return picturesGetDTOs;
    }

    /**
     *
     * @param userGetDTO
     */
    @GetMapping(picture)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody

    public PicturesGetDTO getCorrespondingPicture(@RequestBody UserGetDTO userGetDTO) {
        User currentUser = DTOMapper.INSTANCE.convertUserGetDTOtoEntity(userGetDTO);
        String assignedToken = currentUser.getToken();

        Picture correspondingPicture = gameService.getCorrespondingToUser(assignedToken);
        PicturesGetDTO pictureResult =  DTOMapper.INSTANCE.convertEntityToPicturesGetDTO(correspondingPicture);
        return pictureResult;
    }

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

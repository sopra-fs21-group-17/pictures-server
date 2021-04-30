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
import java.util.Set;

/**
 * GameController is used to manage incoming REST request coming from the client
 * that are related to the gameplay itself
 */
@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService){ this.gameService = gameService; }

    @GetMapping("/board")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> initGame() {
        String lobbyId = "test";
        Set<User> usersList = gameService.initGame(lobbyId);

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

    @GetMapping("/screenshots/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<ArrayList<String>> getScreenshots(@PathVariable String lobbyId){
        ArrayList<ArrayList<String>> response = gameService.getUsersScreenshots(lobbyId);
        return response;
    }

    /**
     * Used to save screenshot URLs to the Back end
     * @param screenshotPutDTO
     */
    @PutMapping("/screenshot")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveScreenshots(@RequestBody ScreenshotPutDTO screenshotPutDTO){
        Screenshot submittedShot = DTOMapper.INSTANCE.convertScreenshotPutDTOtoEntity(screenshotPutDTO);
        gameService.saveScreenshot(submittedShot);
    }

    /**
     *
     * @return Return a List of Screenshots for the guessing screen
     */
    @GetMapping("/screenshot")
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

    // since get/correcteddGuesses not returning anything at the moment, using this instead
    // when it works use @PutMapping("/guesses") again...
    @PostMapping("/guesses")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String submitGuesses(@RequestBody UserPostDTO userPostDTO){
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        return gameService.handleGuesses(user);
    }

    @GetMapping("/correctedGuesses/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Map<String, String>> returnCorrectedGuesses(@PathVariable String lobbyId){
        return gameService.returnCorrectedGuesses(lobbyId);
    }

    /**
     * Used to send a List of picture Elements to frontend
     * Pictures are already mapped to a coordinate.
     */
    @GetMapping("/pictures")
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
    @GetMapping("/picture")
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

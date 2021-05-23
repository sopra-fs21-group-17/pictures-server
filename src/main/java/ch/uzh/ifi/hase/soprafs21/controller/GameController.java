package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Screenshot;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * GameController is used to manage incoming REST request coming from the client
 * that are related to the gameplay itself
 */
@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * call the gameService for initailizing the game Round
     * @param lobbyId
     * @return
     */
    @GetMapping("/board/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> initGame(@PathVariable String lobbyId) {
        List<User> usersList = gameService.initGame(lobbyId);
        List<UserGetDTO> initializedUsersDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : usersList) {
            initializedUsersDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }

        return initializedUsersDTOs;
    }

    @GetMapping("/game/checkUsersDoneGuessing/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkUsersDoneGuessing(@PathVariable String lobbyId){
        // returns true if all user's are done guessing
        return gameService.checkUsersDoneGuessing(lobbyId);
    }

    /**
     * used to reset temporary fields such as pictures for the grid
     * @param lobbyId
     */
    @PutMapping("/board/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setupNextRound(@PathVariable String lobbyId){
        gameService.prepareNewRound(lobbyId);
    }

    @GetMapping("/screenshots/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<ArrayList<String>> getScreenshots(@PathVariable String lobbyId) {
        ArrayList<ArrayList<String>> response = gameService.getUsersScreenshots(lobbyId);
        return response;
    }

    @PutMapping("/screenshot/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveScreenshots(@RequestBody String screenshotURL, @PathVariable String username) {
        gameService.saveScreenshotURL(screenshotURL, username);
    }


    /**
     * @return Return a List of Screenshots for the guessing screen
     */
    @GetMapping("/screenshot/{lobbyID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ScreenshotGetDTO> showScreenshots(@PathVariable String lobbyID) {
        List<Screenshot> screenshots = gameService.getScreenshots(lobbyID);
        List<ScreenshotGetDTO> screenshotGetDTOs = new ArrayList<>();
        for (Screenshot shot : screenshots) {
            screenshotGetDTOs.add(DTOMapper.INSTANCE.convertEntityToScreenshotGetDTO(shot));
        }
        return screenshotGetDTOs;
    }

    @PostMapping("/guesses/{lobbyid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String submitGuesses(@RequestBody UserPostDTO userPostDTO, @PathVariable String lobbyid) {
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        return gameService.handleGuesses(lobbyid, user);
    }

    @GetMapping("/score/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Map<String, String>> returnScore(@PathVariable String lobbyId) {
        return gameService.returnScore(lobbyId);
    }

    /**
     * Used to send a List of picture Elements to frontend
     * Pictures are already mapped to a coordinate.
     */
    @GetMapping("/pictures/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PicturesGetDTO> getPictures(@PathVariable String lobbyId) {

        Picture[] pictures = gameService.getListOfPictures(lobbyId);  // is changed to take from gameplay Entity
        List<PicturesGetDTO> picturesGetDTOs = new ArrayList();
        for (Picture picture : pictures) {
            picturesGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPicturesGetDTO(picture));
        }

        return picturesGetDTOs;
    }

    /**
     * gets the picture corresponding to the correct user id
     */
    @GetMapping("/picture/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PicturesGetDTO getCorrespondingPicture(@PathVariable String id) {
        Picture correspondingPicture = gameService.getCorrespondingToUser(Long.valueOf(id));
        PicturesGetDTO pictureResult = DTOMapper.INSTANCE.convertEntityToPicturesGetDTO(correspondingPicture);
        return pictureResult;
    }

    /**
     * used to return the current Round of a game
     * @param lobbyId
     * @return gamePlayGetDTO containing the current round
     */
    @GetMapping("/rounds/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GamePlayGetDTO getCurrentRound(@PathVariable String lobbyId){
       GamePlayGetDTO gamePlayGetDTO = DTOMapper.INSTANCE.convertEntityToGamePlayGetDTO(gameService.getGamePlay(lobbyId));
       return gamePlayGetDTO;
    }

    /**
     * will be used in the client to reset the counters for round handling
     * (use a later Component, rather than MainBoard in Fe)
     */
    @PutMapping("/rounds/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetCounterForNextRound(@PathVariable String lobbyId){
        gameService.resetCounterForRoundHandling(lobbyId);
    }

}

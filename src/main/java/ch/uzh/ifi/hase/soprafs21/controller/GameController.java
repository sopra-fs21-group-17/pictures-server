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

    @GetMapping("/board/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> initGame(@PathVariable String lobbyId) {
        List<User> usersList = gameService.initGame(lobbyId);
        List<UserGetDTO> initedUsersDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : usersList) {
            initedUsersDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }

        return initedUsersDTOs;
    }

    @GetMapping("/screenshots/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<ArrayList<String>> getScreenshots(@PathVariable String lobbyId) {
        ArrayList<ArrayList<String>> response = gameService.getUsersScreenshots(lobbyId);
        return response;
    }

    /**
     * Used to save screenshot URLs to the Back end
     *
     * @param screenshotPutDTO
     */
//    @PutMapping("/screenshot/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void saveScreenshots(@RequestBody ScreenshotPutDTO screenshotPutDTO, @PathVariable String userId){
//        Screenshot submittedShot = DTOMapper.INSTANCE.convertScreenshotPutDTOtoEntity(screenshotPutDTO);
//        gameService.saveScreenshot(submittedShot, Long.valueOf(userId));
//    }

    @PutMapping("/screenshot/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveScreenshots(@RequestBody ScreenshotPutDTO screenshotPutDTO, @PathVariable String username) {
        Screenshot submittedShot = DTOMapper.INSTANCE.convertScreenshotPutDTOtoEntity(screenshotPutDTO);
        gameService.saveScreenshot(submittedShot, username);
    }

    /**
     * @return Return a List of Screenshots for the guessing screen
     */
    @GetMapping("/screenshot")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ScreenshotGetDTO> showScreenshots() {
        List<Screenshot> screenshots = gameService.getScreenshots();
        List<ScreenshotGetDTO> screenshotGetDTOs = new ArrayList<>();
        for (Screenshot shot : screenshots) {
            screenshotGetDTOs.add(DTOMapper.INSTANCE.convertEntityToScreenshotGetDTO(shot));
        }
        return screenshotGetDTOs;
    }

//    @PutMapping("/guesses/{lobbyid}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void submitGuesses(@RequestBody UserPutDTO userPutDTO, @PathVariable String lobbyid) {
//        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
//        gameService.handleGuesses(lobbyid, user);
//    }

    @PostMapping("/guesses/{lobbyid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String submitGuesses(@RequestBody UserPostDTO userPostDTO, @PathVariable String lobbyid) {
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        System.out.println("username for guesses: "+user.getUsername());
        return gameService.handleGuesses(lobbyid, user);
    }

    @GetMapping("/score/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Map<String, String>> returnCorrectedGuesses(@PathVariable String lobbyId) {
        return gameService.returnCorrectedGuesses(lobbyId);
    }

    /**
     * Used to send a List of picture Elements to frontend
     * Pictures are already mapped to a coordinate.
     */
    @GetMapping("/pictures")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PicturesGetDTO> getPictureURL() {

        Picture[] pictures = gameService.getListOfPictures();  // is changed to take from gameplay
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

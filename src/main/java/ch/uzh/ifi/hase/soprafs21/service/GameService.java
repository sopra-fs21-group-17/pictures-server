package ch.uzh.ifi.hase.soprafs21.service;

// Name can be refactored if it is not fitting (maybe to GameLogic)

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * GameService is responsible for handling the incoming information from the Client and manipulate the
 * State of the Game according to the position in the round
 */
public class GameService {

    private List<User> gameUsers;
    private final PicturesRepository picturesRepository;

    @Autowired
    public GameService(@Qualifier("picturesRepository") PicturesRepository picturesRepository) {
        this.picturesRepository = picturesRepository;
    }

    public void initGame(){}

    public void saveScreenshots(){}

    public void handleGuesses(){}



}

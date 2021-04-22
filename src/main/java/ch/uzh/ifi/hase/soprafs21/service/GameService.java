package ch.uzh.ifi.hase.soprafs21.service;

// Name can be refactored if it is not fitting (maybe to GameLogic)

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public List<Picture> selectPictures(){
        //goes from 0 to 15 for easier mapping
        int maxPictures = 16;
        int randomLimit = 51; //limit will be strictly smaller than
        //TODO depending on storage will may need different implementation for the maximum limit.

        ArrayList<Picture> pictures = new ArrayList();
        ArrayList<Integer> checkID = new ArrayList();

        Random random = new Random();
        int idx = 0;
        while(idx < maxPictures){
            int randomizedID =random.nextInt(randomLimit);
            if(!checkID.contains(randomizedID)){
                checkID.add(randomizedID);

                Picture current = picturesRepository.findByID((long)randomizedID); //random has problems with long so to avoid, used int and parsed
                current.setCoordinate(idx); // sets the coordinate for the picture       //TODO discuss implementation maybe store this differently because of multiple possible games in web

                picturesRepository.flush();
                pictures.add(current);
                idx++;
            }

        }
        return pictures;
    }

    public void initGame(){}

    public void saveScreenshots(){}

    public void handleGuesses(){}



}

package ch.uzh.ifi.hase.soprafs21.service;

// Name can be refactored if it is not fitting (maybe to GameLogic)

import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

/**
 * GameService is responsible for handling the incoming information from the Client and manipulate the
 * State of the Game according to the position in the round
 */
public class GameService {

    private List<User> gameUsers;
    private final PicturesRepository picturesRepository;
    private final UserRepository userRepository;

    // game variables
    private final int NR_OF_PLAYERS = 5;    // TODO what is the range of min-max nr of players? How to get this?
    private final String[] SET_NAMES = new String[]{"blocks", "icon cards", "stringy", "cubes"};
    private final int NR_OF_SETS = SET_NAMES.length;
    public User[] playingUsers = null;  // index of playing users will remain the same for the entire game
    public User currentUser = null;

    @Autowired
    public GameService(@Qualifier("picturesRepository") PicturesRepository picturesRepository, UserRepository userRepository) {
        this.picturesRepository = picturesRepository;
        this.userRepository = userRepository;
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
//                checkID.add(randomizedID);
//
//                Picture current = picturesRepository.findByID((long)randomizedID); //random has problems with long so to avoid, used int and parsed
//                current.setCoordinate(idx); // sets the coordinate for the picture       //TODO discuss implementation maybe store this differently because of multiple possible games in web
//
//                picturesRepository.flush();
//                pictures.add(current);
//                idx++;
            }

        }
        return pictures;
    }

    /**
     * Initializes the game:
     *  - Assign random coordinates to each user
     *  - Assign random sets to each user
     *  - Initialize and select pictures for game
     * */
    public void initGame(String[] userNames){
        User[] playingUsers = getPlayingUsers(userNames);
        assignCoordinates(playingUsers);
        assignSets(playingUsers);

        this.playingUsers = getPlayingUsers(userNames); // for dev use only
    }

    public User[] getPlayingUsers(String[] userNames){

        User[] usersList = new User[NR_OF_PLAYERS];

        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i] = userRepository.findByUsername(userNames[i]);
        }

        return usersList;
    }

    public void saveScreenshots(){}

    public void saveGuesses(User userGuess){
        userRepository.findById(userGuess.getId())
    }

    public void setCurrentUser(String userName){
        for(User user : playingUsers){
            if(user.getUsername().equals(userName)){
                this.currentUser = user;
            }
        }

        System.out.println("ERROR - COULDN'T FIND THAT USER!");
    }

    public void handleGuesses(Map<User,String> userGuesses, Long userID){
       Optional<User> current = userRepository.findById(userID);
        String[] correctGuesses = {"n", "n", "n", "n"}; // TODO make better list
//        for(int i = 0; i < NR_OF_PLAYERS; i++){
//            if (this.playingUsers[i].getAssignedCoordinates() == userGuesses[i]){
//                correctGuesses[i] = "y";
//            }
//        }

   //     currentUser.setCorrectGuesses(correctGuesses);

        for(User user : userGuesses)

    }

    public Integer[] getShuffledIdxList(int listLength){
        // make array with indices to randomly assign sets
        Integer[] idxList = new Integer[listLength];
        for(int i = 0; i < listLength; i++){ idxList[i] = i; }

        // shuffle array/list to make random
        List<Integer> tempList = Arrays.asList(idxList);
        Collections.shuffle(tempList);
        tempList.toArray(idxList);

        return idxList;
    }

    public void assignSets(User[] usersList) {
        // make shuffled array with indices to randomly assign sets
        Integer[] idxList = getShuffledIdxList(NR_OF_SETS);

        // assign random sets
        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i].setAssignedSet(SET_NAMES[i]);
        }
    }

    // coordinates represented in code like this:
    // A1 = 0, A2 = 1, D4 = 15 ...
    // so just pick random nr between 0-15

    public void assignCoordinates(User[] usersList) {
        int nrOfCoordinates = 15;
        Integer[] idxList = getShuffledIdxList(nrOfCoordinates);

        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i].setAssignedCoordinates(idxList[i]);
        }
    }

}

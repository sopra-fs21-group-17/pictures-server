package ch.uzh.ifi.hase.soprafs21.service;

// Name can be refactored if it is not fitting (maybe to GameLogic)

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.Screenshot;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * GameService is responsible for handling the incoming information from the Client and manipulate the
 * State of the Game according to the position in the round
 */
@Service
@Transactional
public class GameService {


    private final PicturesRepository picturesRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;



    // game variables
    private final int NR_OF_PLAYERS = 5;    // TODO what is the range of min-max nr of players? How to get this?
    private final String[] SET_NAMES = new String[]{"blocks", "icon cards", "stringy", "cubes"};
    private final int NR_OF_SETS = SET_NAMES.length;
    public User[] playingUsers = null;  // index of playing users will remain the same for the entire game
    public User currentUser = null;

    @Autowired
    public GameService(@Qualifier("picturesRepository") PicturesRepository picturesRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("gameSessionRepository") GameSessionRepository gameSessionRepository) {
        this.picturesRepository = picturesRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    /**
     * selects Pictures from the external API according to their ID randomly
     * So there are 16 chosen and saved into the corresponding GamePlay entity.
     */
    public void selectPictures(){
        //goes from 0 to 15 for easier mapping
        int maxPictures = 16;
        int randomLimit = 51; //limit will be strictly smaller than
        //TODO depending on storage will may need different implementation for the maximum limit.
        GamePlay currentGame = gameSessionRepository.findByGameID(1L);  //TODO for M4 implement for mulitple lobbies


        ArrayList<Integer> checkID = new ArrayList();

        Random random = new Random();
        int idx = 0;
        while(idx < maxPictures){
            int randomizedID =random.nextInt(randomLimit);
            if(!checkID.contains(randomizedID)){
                checkID.add(randomizedID);
                Picture current = picturesRepository.findByid((long)randomizedID);
                //random has problems with long so to avoid, used int and parsed
                currentGame.addPicture(current,idx);  // adds the picture to the entity
                idx++;
            }

        }
    }

    /**
     * gets Pictures that are Saved for the Current GameRound in the Gameplay entity
     * @return returns all Pictures for the current Round
     */
    public List<Picture> getListOfPictures(){
        GamePlay currentGame = gameSessionRepository.findByGameID(1L);
        return currentGame.getSelectedPictures();
    }

    /**
     * Takes on token from user and gets the picture that has that coordinate
     * @param token
     * @return returns Picture that has the corresponding token of the User
     */
    public Picture getCorrespondingToUser(int token){
        GamePlay currentGame = gameSessionRepository.findByGameID(1L);
        return currentGame.getPictureWithToken(token);
    }

    /**
     * Saves screenshot from Controller to the corresponding Gameplay Entity
     * @param submittedShot
     */
    public void saveScreenshot(Screenshot submittedShot){
        GamePlay currentGame = gameSessionRepository.findByGameID(1L);
        currentGame.addScreenshot(submittedShot);
    }

    /**
     *
     */
    public List<Screenshot> getScreenshots(){
        GamePlay currentGame = gameSessionRepository.findByGameID(1L);
        return currentGame.getListOfScreenshots();
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
        this.gameSessionRepository.save(new GamePlay());   // needed for management fo Pictures
        gameSessionRepository.flush();
        this.playingUsers = getPlayingUsers(userNames); // for dev use only

    }
//TODO please check if javadoc is correct like this
    /**
     * used to get the playing users from the Lobby
     * @param userNames
     * @return returns a list of the playing users
     */
    public User[] getPlayingUsers(String[] userNames){

        User[] usersList = new User[NR_OF_PLAYERS];

        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i] = userRepository.findByUsername(userNames[i]);
        }

        return usersList;
    }

    /**
     * identifies and stores the current user in the method
     * @param userName
     */
    public void setCurrentUser(String userName){
        Boolean userFound = false;
        for(User user : playingUsers){
            if(user.getUsername().equals(userName)){
                this.currentUser = user;
                userFound = true;
            }
        }
        if(userFound==false){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not be found!");
        }

    }

    /**
     * used to update the score of a user depending on if they macht to the other users assigned token
     * @param currentUser
     */
    public void handleGuesses(User currentUser){
        ArrayList<ArrayList<String>> correctedGuesses = new ArrayList<ArrayList<String>>() ; // TODO make better list
        String userGuesses = currentUser.getGuesses();
        User tempUser;
        String isCorrect;
        ArrayList<String> answer;

//        for(ArrayList<String> tuple : userGuesses){
//            tempUser = userRepository.findByUsername(tuple.get(0));
//            if(tempUser != null){
//                isCorrect = "n";
//                if(tempUser.getAssignedCoordinates() == Integer.parseInt(tuple.get(1))){
//                    // TODO +1 point
//                    isCorrect = "y";
//                }
//                answer = new ArrayList<String>( Arrays.asList(tempUser.getUsername(), isCorrect) );
//                correctedGuesses.add(answer);
//            }
//            else{
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not be found!");
//            }
//        }

        // für Testzwecke guesses in console geschrieben
        System.out.println(correctedGuesses);
        // TODO update scoreboard
    }

    /**
     * Helper method used to shuffle lists for random assignment
     * @param listLength
     * @return returns an Array of shuffled indices
     */
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

    /**
     * Method is used to assigned a random set to every User
     * @param usersList
     */
    public void assignSets(User[] usersList) {
        // make shuffled array with indices to randomly assign sets
        Integer[] idxList = getShuffledIdxList(NR_OF_SETS);

        // assign random sets
        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i].setAssignedSet(SET_NAMES[i]);
        }
    }

    /**
     * Method is used to assign a random Token coordinate for every User
     * @param usersList
     */
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

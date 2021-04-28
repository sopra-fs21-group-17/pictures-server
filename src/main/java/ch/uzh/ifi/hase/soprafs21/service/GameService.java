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
import ch.uzh.ifi.hase.soprafs21.game.SetNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * GameService is responsible for handling the incoming information from the Client and manipulate the
 * State of the Game according to the position in the round
 */
@Service
@Transactional
public class GameService {

    // user list for test purposes
    User a = new User();
    User b = new User();
    User c = new User();
    User d = new User();
    User[] playingUsers = {a, b, c, d};

    //private List<User> gameUsers;
    private final PicturesRepository picturesRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;



    // game variables
    private final int NR_OF_PLAYERS = 3;    // TODO what is the range of min-max nr of players? How to get this?
    private final String[] SET_NAMES = new String[]{"CUBES", "BLOCKS", "STICKS", "ICONS", "LACE"};
    private final int NR_OF_SETS = SET_NAMES.length;

    // user references
    //public User[] playingUsers = null;  // index of playing users will remain the same for the entire game
    //public User currentUser = null;

    @Autowired
    public GameService(@Qualifier("picturesRepository") PicturesRepository picturesRepository, UserRepository userRepository, GameSessionRepository gameSessionRepository) {
        this.picturesRepository = picturesRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    public void selectPictures(){
        //goes from 0 to 15 for easier mapping
        int maxPictures = 16;
        int randomLimit = 51; //limit will be strictly smaller than
        //TODO depending on storage will may need different implementation for the maximum limit.
        GamePlay currentGame = gameSessionRepository.findbyGameID(1L);  //TODO for M4 implement for mulitple lobbies


        ArrayList<Integer> checkID = new ArrayList();

        Random random = new Random();
        int idx = 0;
        while(idx < maxPictures){
            int randomizedID =random.nextInt(randomLimit);
            if(!checkID.contains(randomizedID)){
                checkID.add(randomizedID);
                Picture current = picturesRepository.findByid((long)randomizedID); //random has problems with long so to avoid, used int and parsed
                currentGame.addPicture(current,idx);  // adds the picture to the entity
                idx++;
            }

        }
    }

    /**
     * Pictures that are Saved for the Current GameRound
     * @return returns all Pictures for the current Round
     */
    public List<Picture> getListOfPictures(){
        GamePlay currentGame = gameSessionRepository.findbyGameID(1L);
        return currentGame.getSelectedPictures();
    }

    /**
     * Takes on token from user and gets the picture that has that coordinate
     * @param token
     * @return returns Picture that has the corresponding token of the User
     */
    public Picture getCorrespondingToUser(int token){
        GamePlay currentGame = gameSessionRepository.findbyGameID(1L);
        return currentGame.getPictureWithToken(token);
    }

    /**
     * Saves screenshot from Controller to the corresponding Gameplay Entity
     * @param submittedShot
     */
    public void saveScreenshot(Screenshot submittedShot){
        GamePlay currentGame = gameSessionRepository.findbyGameID(1L);
        currentGame.addScreenshot(submittedShot);
    }

    /**
     *
     */
    public List<Screenshot> getScreenshots(){
        GamePlay currentGame = gameSessionRepository.findbyGameID(1L);
        return currentGame.getListOfScreenshots();
    }

    /**
     * Initializes the game:
     *  - Assign random coordinates to each user
     *  - Assign random sets to each user
     *  - Initialize and select pictures for game
     * */
    public void initGame(String[] userNames){
        //this.playingUsers = getPlayingUsers(userNames); // for dev use only

        // for test purposes
        for(int i = 0; i < 4; i++){
            playingUsers[i].setUsername(String.valueOf(i));
            //playingUsers[i].setId(Long.valueOf(i));
            userRepository.save(playingUsers[i]);
            userRepository.flush();
        }
        ////////////////////

        assignCoordinates(playingUsers);
        assignSets(playingUsers);
        this.gameSessionRepository.save(new GamePlay());   // needed for management fo Pictures
        gameSessionRepository.flush();
        this.playingUsers = getPlayingUsers(userNames); // for dev use only

    }

    public User[] getPlayingUsers(String[] userNames){

        User[] usersList = new User[NR_OF_PLAYERS];

        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i] = userRepository.findByUsername(String.valueOf(i));
        }

        return usersList;
    }


//    public void setCurrentUser(String userName){
//        for(User user : playingUsers){
//            if(user.getUsername().equals(userName)){
//                this.currentUser = user;
//            }
//        }
//
//        System.out.println("ERROR - COULDN'T FIND THAT USER!");
//    }

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
//                System.out.println("ERROR couldn't find that user!"); // TOOD make exception
//                return;
//            }
//        }

        // für Testzwecke guesses in console geschrieben
        System.out.println(correctedGuesses);
        // TODO update scoreboard
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
            usersList[i].setAssignedSet(SET_NAMES[idxList[i]]);
        }
    }

    // coordinates represented in code like this:
    // A1 = 0, A2 = 1, D4 = 15 ...
    // so just pick random nr between 0-15x3
    public void assignCoordinates(User[] usersList) {
        int repetitions = 3;
        int nrOfCoordinates = 16;
        int totalCoordinates = repetitions * nrOfCoordinates; // 16 cards on board, 3x same coordinate

        // make array with indices to randomly assign sets
        Integer[] idxList = new Integer[totalCoordinates];
        int idx = 0;
        for(int i = 0; i < repetitions; i++){
            for(int j = 0; j < nrOfCoordinates; j++){
                idxList[idx] = j;
                idx++;
            }
        }

        // shuffle array/list to make random
        List<Integer> tempList = Arrays.asList(idxList);
        Collections.shuffle(tempList);
        tempList.toArray(idxList);

        // assign coordinates to players
        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList[i].setAssignedCoordinates(idxList[i]);
        }
    }

}

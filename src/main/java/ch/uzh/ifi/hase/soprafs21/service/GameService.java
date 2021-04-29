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
    private final int NR_OF_PLAYERS = 4;    // TODO what is the range of min-max nr of players? How to get this?
    private final String[] SET_NAMES = new String[]{"CUBES", "BLOCKS", "STICKS", "ICONS", "LACE"};
    private final int NR_OF_SETS = SET_NAMES.length;

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
        GamePlay currentGame = gameSessionRepository.findByGameID(1L);  //TODO for M4 implement for mulitple lobbies


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

    public ArrayList<ArrayList<String>> getUsersScreenshots(){
        ArrayList<ArrayList<String>> response = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

        // for test purposes
        for(int i = 0; i < NR_OF_PLAYERS; i++){
            playingUsers[i].setUsername("USER " + String.valueOf(i));
            playingUsers[i].setAssignedCoordinates(i);
            playingUsers[i].setPoints(0); // init all points to 0
            playingUsers[i].setScreenshotURL("https://i.insider.com/5484d9d1eab8ea3017b17e29?width=600&format=jpeg&auto=webp");
            userRepository.save(playingUsers[i]);
            userRepository.flush();
        }
        ////////////////////

        for(User u : playingUsers){
            temp.add(u.getUsername());
            temp.add(u.getScreenshotURL());
            response.add(temp);
            temp = new ArrayList<>();
        }

        return response;
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
        for(int i = 0; i < 3; i++){
            playingUsers[i].setUsername(String.valueOf(i));
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

    public Map<String, String> getGuessesHashMap(String guesses){
        // convert string and save values into hashmap ////////
        String tempUsername = "";
        String tempCoordinates = "";
        Map<String, String> result = new HashMap<String, String>();

        for(int i = 0; i < guesses.length(); i++){
            for(int j = 0; j < 2; j++){ // first 2 letters are coordinates
                tempCoordinates += guesses.charAt(i+j);
            }
            i+=2; // skip coordinates, goto username

            while(i < guesses.length()-1 && guesses.charAt(i) != '-'){
                tempUsername += guesses.charAt(i);
                i++;
            }
            result.put(tempUsername, tempCoordinates);
            tempUsername = tempCoordinates = "";
        }

        return result;
    }

    public void handleGuesses(User user){
        User player = userRepository.findByUsername(user.getUsername());

        // convert String(guesses) to hashmap with username and coordinate
        Map<String, String> guesses = getGuessesHashMap(user.getGuesses());

        // correct guesses
        String[] coordinateNames = { "A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4" };
        User tempUsr;
        Map<String, String> correctedGuesses = new HashMap<String, String>();
        String result = "";
        for( Map.Entry<String, String> entry : guesses.entrySet() ){
            tempUsr = userRepository.findByUsername(entry.getKey());
            // check if coordinates match
            if( coordinateNames[tempUsr.getAssignedCoordinates()].equals( entry.getValue().toUpperCase() ) ){
                tempUsr.setPoints(tempUsr.getPoints()+1); // give user a point
                result += "y" + entry.getKey();
            }
            else{
                result += "n" + entry.getKey();
            }
            result += "-";
        }

        String x;
        if(player.getCorrectedGuesses() == null){ x = result; }
        else{ x = player.getCorrectedGuesses() + result; }

        player.setCorrectedGuesses(x);

        userRepository.save(player);
        userRepository.flush();
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

    public Map<String, Map<String, String>> returnCorrectedGuesses() {
        String correctedGuesses = "";
        Map<String, String> temp = new HashMap<>();
        String username = "";
        String answer = "";
        Map<String, Map<String, String>> result = new HashMap<>(); // { username:{max:y,eva:n}, username:{max:y,eva:n}}

        for(User usr : playingUsers){
            correctedGuesses = usr.getCorrectedGuesses();
            if(correctedGuesses != null){
                // convert
                for(int i = 0; i < correctedGuesses.length(); i++){
                    answer += correctedGuesses.charAt(i);
                    i++; // skip answer "y"/"n"
                    // parse username
                    while(i < correctedGuesses.length()-1 && correctedGuesses.charAt(i) != '-'){
                        username += correctedGuesses.charAt(i);
                        i++;
                    }
                    temp.put(username, answer);
                    username = answer = ""; // reset
                }
                System.out.println("username: "+usr.getUsername());
                System.out.println(temp.values());
                result.put(usr.getUsername(), temp);
            }
        }
        System.out.println(result.values());
        return result;
    }
}

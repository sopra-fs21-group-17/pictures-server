package ch.uzh.ifi.hase.soprafs21.service;

// Name can be refactored if it is not fitting (maybe to GameLogic)

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

// TEST MESSAGE

/**
 * GameService is responsible for handling the incoming information from the Client and manipulate the
 * State of the Game according to the position in the round
 */
@Service
@Transactional
public class GameService {

    // user list for test purposes
    ArrayList<User> playingUsers = new ArrayList<>();

    private final PicturesRepository picturesRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final LobbyRepository lobbyRepository;

    private GamePlay gamePlay = new GamePlay();
    private Long gameID = 1L;

    // game variables
    private final int NR_OF_PLAYERS = 4;
    private final String[] SET_NAMES = new String[]{"CUBES", "BLOCKS", "STICKS"};// "ICONS", "LACE"};
    private final int NR_OF_SETS = SET_NAMES.length;

    @Autowired
    public GameService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("picturesRepository") PicturesRepository picturesRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("gameSessionRepository") GameSessionRepository gameSessionRepository) {
        this.picturesRepository = picturesRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public void createTestUsers(){

        for(int i = 0; i < NR_OF_PLAYERS; i++){
            User user = new User();
            user.setUsername("USER " + String.valueOf(i));
            user.setAssignedCoordinates(i);
            user.setPoints(0); // init all points to 0
            user.setScreenshotURL("https://i.insider.com/5484d9d1eab8ea3017b17e29?width=600&format=jpeg&auto=webp");
            
            userRepository.save(user);
            userRepository.flush();

            playingUsers.add(user);
        }
        
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
        gamePlay.setGameID(1L);

        //TODO for M4 implement for mulitple lobbies


        ArrayList<Integer> checkID = new ArrayList();

        Random random = new Random();
        int idx = 0;
        while(idx < maxPictures){
            int randomizedID =random.nextInt(randomLimit);
            if(!checkID.contains(randomizedID)){
                checkID.add(randomizedID);
                Picture current = picturesRepository.findByid((long)randomizedID); //random has problems with long so to avoid, used int and parsed
                gamePlay.addPicture(current,idx);  // adds the picture to the entity
                idx++;
            }

        }
    }

    /**
     * gets Pictures that are Saved for the Current GameRound in the Gameplay entity
     * @return returns all Pictures for the current Round
     */
    public List<Picture> getListOfPictures(){
        return gamePlay.getSelectedPictures();
    }

    /**
     * Takes on token from user and gets the picture that has that coordinate
     * @param userId
     * @return returns Picture that has the corresponding token of the User
     */
    public Picture getCorrespondingToUser(Long userId){

        GamePlay currentGame = gamePlay;
        User corresponding = userRepository.findByid(userId);
        return currentGame.getPictureWithCoordinates(corresponding.getAssignedCoordinates());
    }

    /**
     * Saves screenshot from Controller to the corresponding Gameplay Entity
     * @param submittedShot
     * @param userId
     */
//    public void saveScreenshot(Screenshot submittedShot, Long userId){
//        User user = userRepository.findByid(userId);
//
//
//        user.setScreenshotURL(submittedShot.getURL());
//        userRepository.save(user);
//        userRepository.flush();
//    } // TODO auskommentiert zum testen


    public void saveScreenshot(Screenshot submittedShot, String username){
        User user = userRepository.findByUsername(username);
        user.setScreenshotURL(submittedShot.getURL());
        userRepository.save(user);
        userRepository.flush();
    }
    /**
     *
     */
    public List<Screenshot> getScreenshots(){

        return gamePlay.getListOfScreenshots();
    }

    public ArrayList<ArrayList<String>> getUsersScreenshots(String lobbyId){

        ArrayList<ArrayList<String>> response = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Set<User> usersList = lobby.getUsersList();

        for(User u : usersList){
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
     *
     * @return*/
    public Set<User> initGame(String lobbyId) {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Set<User> usersList = lobby.getUsersList();

        assignCoordinates(usersList);
        assignSets(usersList);

        for (User u : usersList) {
            userRepository.save(u);
            userRepository.flush();
        }

        if (gamePlay == null) {
            GamePlay game = new GamePlay();
            this.gameSessionRepository.save(game);   // needed for management fo Pictures
            gameSessionRepository.flush();
            gamePlay = game;

        }
        return usersList;
    }
//TODO please check if javadoc is correct like this
    /**
     * used to get the playing users from the Lobby
     * @param userNames
     * @return returns a list of the playing users
     */
    public ArrayList<User> getPlayingUsers(String[] userNames){

        ArrayList<User> usersList = new ArrayList<>();

        for(int i = 0; i < NR_OF_PLAYERS; i++){
            usersList.add(userRepository.findByUsername("USER " + String.valueOf(i)));
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

    public String handleGuesses(User user){
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

        player.setCorrectedGuesses(result);
        userRepository.save(player);
        userRepository.flush();

        return result;

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
    public void assignSets(Set<User> usersList) {
        // make shuffled array with indices to randomly assign sets
        Integer[] idxList = getShuffledIdxList(NR_OF_SETS);
        int i = 0;
        // assign random sets
        for(User user : usersList){
            user.setAssignedSet(SET_NAMES[idxList[i % SET_NAMES.length]]);
            i++;
        }
    }

    // coordinates represented in code like this:
    // A1 = 0, A2 = 1, D4 = 15 ...
    // so just pick random nr between 0-15x3
    public void assignCoordinates(Set<User> usersList) {
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
        int i = 0;
        for(User user : usersList){
            user.setAssignedCoordinates(idxList[i%totalCoordinates]);
            i++;
        }
    }

    public Map<String, Map<String, String>> returnCorrectedGuesses(String lobbyId) {

        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        Set<User> usersList = lobby.getUsersList();

        String correctedGuesses = "";
        Map<String, String> temp = new HashMap<>();
        String username = "";
        String answer = "";
        Map<String, Map<String, String>> result = new HashMap<>(); // { username:{max:y,eva:n}, username:{max:y,eva:n}}

        for(User usr : usersList){
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

                result.put(usr.getUsername(), temp);
            }
        }

        return result;
    }
}


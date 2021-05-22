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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    private Boolean gameInited = false;
    private Random rand = SecureRandom.getInstanceStrong();

    // game variables
    private final int NR_OF_PLAYERS = 4;
    private final String[] SET_NAMES = new String[]{"CUBES", "BLOCKS", "STICKS", "ICONS", "LACE"};
    private final int NR_OF_SETS = SET_NAMES.length;
    private final int MAX_GAME_ROUNDS = 5;

    @Autowired
    public GameService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("picturesRepository") PicturesRepository picturesRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("gameSessionRepository") GameSessionRepository gameSessionRepository) throws NoSuchAlgorithmException {
        this.picturesRepository = picturesRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.lobbyRepository = lobbyRepository;
    }

//*****START OF THE NEW ROUND/GAME - ROUND HANDLES

    /**
     * Initializes the game:
     * - Assign random coordinates to each user
     * - Assign random sets to each user
     * - Instanciate new GamePlayEntity
     * - select Pictures for the first time
     *
     * @return the list of playing users
     */
    public List<User> initGame(String lobbyId) throws ResponseStatusException {
        checkLobbyExists(lobbyId);
        LobbyService lobbyService = new LobbyService(this.lobbyRepository, this.userRepository);

        List<User> usersList = lobbyService.getUsersInLobby(lobbyId);

        if (gameSessionRepository.findByCorrespondingLobbyID(lobbyId) == null) {
            //add new GamePlay entity
            GamePlay game = new GamePlay();
            // game.setLobby(this.lobbyRepository.findByLobbyId(lobbyId));
            game.setLobbyForGamePlay(lobbyRepository.findByLobbyId(lobbyId));
            game.setNumberOfPlayers(usersList.size());  // needed for round counting
            game.setRoundInited(false);
            gameSessionRepository.save(game);
            gameSessionRepository.flush();
            game = null; //trying for constraint reasons
        }
        GamePlay game = gameSessionRepository.findByCorrespondingLobbyID(lobbyId);
        if (!game.roundInited) {
            assignCoordinates(usersList);
            assignSets(usersList);
            game.setRoundInited(true);
            gameSessionRepository.save(game);
            gameSessionRepository.flush();
        }

        //select pictures to corresponding gameplay entity
        selectPictures(lobbyId);

        for (User u : usersList) {
            userRepository.save(u);
            userRepository.flush();
        }

        return usersList;
    }

    /**
     * used to mainly delete content that is temporary and to count the rounds
     * for pictures to be newly selected again the list has to be empty first otherwise
     * the selection will fail
     *
     * @param lobbyID
     * @throws ResponseStatusException
     */
    public void prepareNewRound(String lobbyID) throws ResponseStatusException {
        checkLobbyExists(lobbyID);
        GamePlay current = gameSessionRepository.findByCorrespondingLobbyID(lobbyID);

        // if the pictures list wasn't already null
        if (current != null) {
            current.clearSelectedPictures();
        }
        //count until all players have finished the round if
        //if the all players are done increment the current round number (max should be 5)
        current.setAllUsersFinishedRound(current.getAllUsersFinishedRound() + 1);
        if (current.getAllUsersFinishedRound() == current.getNumberOfPlayers()) {
            current.setRoundsFinished(current.getRoundsFinished() + 1);
        }

        gameSessionRepository.save(current);
        gameSessionRepository.flush();
    }

    public void resetCounterForRoundHandling(String lobbyID){
        checkLobbyExists(lobbyID);
        GamePlay currentGame = gameSessionRepository.findByCorrespondingLobbyID(lobbyID);
        currentGame.setAllUsersFinishedRound(0);
        gameSessionRepository.save(currentGame);
        gameSessionRepository.flush();
    }

    /**
     * Method is used to assigned a random set to every User
     *
     * @param usersList
     */
    public void assignSets(List<User> usersList) {
        // make shuffled array with indices to randomly assign sets
        Integer[] idxList = getShuffledIdxList(NR_OF_SETS);
        int i = 0;
        // assign random sets
        for (User user : usersList) {
            user.setAssignedSet(SET_NAMES[idxList[i % SET_NAMES.length]]);
            i++;
        }
    }

    // coordinates represented in code like this:
    // A1 = 0, A2 = 1, D4 = 15 ...
    // so just pick random nr between 0-15x3
    public void assignCoordinates(List<User> usersList) {
        int repetitions = 3;
        int nrOfCoordinates = 16;
        int totalCoordinates = repetitions * nrOfCoordinates; // 16 cards on board, 3x same coordinate

        // make array with indices to randomly assign sets
        Integer[] idxList = new Integer[totalCoordinates];
        int idx = 0;
        for (int i = 0; i < repetitions; i++) {
            for (int j = 0; j < nrOfCoordinates; j++) {
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
        for (User user : usersList) {
            user.setAssignedCoordinates(idxList[i % totalCoordinates]);
            i++;
        }
    }

    public GamePlay getGamePlay(String lobbyId) throws ResponseStatusException {
        checkLobbyExists(lobbyId);
        GamePlay game = gameSessionRepository.findByCorrespondingLobbyID(lobbyId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("the current GamePlay entity was not found"));
        }
        return game;
    }

    public void createTestUsers() {

        for (int i = 0; i < NR_OF_PLAYERS; i++) {
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

//*****PICTURE HANDLING

    /**
     * selects Pictures from the external API according to their ID randomly
     * So there are 16 chosen and saved into the corresponding GamePlay entity.
     */
    public void selectPictures(String lobbyID) throws ResponseStatusException {
        //goes from 0 to 15 for easier mapping
        checkLobbyExists(lobbyID); // throws ResponseStatus Exception
        GamePlay gamePlay = gameSessionRepository.findByCorrespondingLobbyID(lobbyID);
        if (gamePlay.getSelectedPictures() == null) {
            int maxPictures = 16;
            int randomLimit = 51; //limit will be strictly smaller than

            ArrayList<Integer> checkID = new ArrayList();

            int idx = 0;
            while (idx < maxPictures) {
                int randomizedID = rand.nextInt(randomLimit);
                if (!checkID.contains(randomizedID)) {
                    Picture current = picturesRepository.findByid(Long.valueOf(randomizedID));
                    //random has problems with long so to avoid, used int and parsed
                    if (current != null && current.getPictureLink() != null) {
                        checkID.add(randomizedID);

                        // adds the picture to the entity
                        gamePlay.addPicture(current.getPictureLink(), idx);
                        gameSessionRepository.save(gamePlay);
                        gameSessionRepository.flush();
                        idx++;
                    }
                }
            }

        }
    }

    /**
     * gets Pictures that are Saved for the Current GameRound in the Gameplay entity
     *
     * @return returns all Pictures for the current Round
     */
    public Picture[] getListOfPictures(String lobbyID) throws ResponseStatusException {
        checkLobbyExists(lobbyID); // throws ResponseStatus Exception
        GamePlay gamePlay = gameSessionRepository.findByCorrespondingLobbyID(lobbyID);
        String[] pictureURLs = gamePlay.getSelectedPictures();
        if(pictureURLs != null) {
            Picture[] pictures = new Picture[16];
            for (int i = 0; i < pictureURLs.length; i++) {
                pictures[i] = new Picture();
                pictures[i].setPictureLink(pictureURLs[i]);
            }
            return gamePlay != null ? pictures : null;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The List of Pictures was empty");
        }
    }

    /**
     * Takes on token from user and gets the picture that has that coordinate
     *
     * @param userId
     * @return returns Picture that has the corresponding token of the User
     */
    public Picture getCorrespondingToUser(Long userId) throws ResponseStatusException {
        User correspondingUser = userRepository.findByid(userId);


        if (correspondingUser != null && correspondingUser.getAssignedCoordinates() >= 0) {
            checkLobbyExists(correspondingUser.getLobbyId()); // Throws responsestatus exeption
            GamePlay currentGame = gameSessionRepository.findByCorrespondingLobbyID(correspondingUser.getLobbyId());
            String pictureURL = currentGame.getPictureWithCoordinates(correspondingUser.getAssignedCoordinates());
            if (pictureURL != null) {
                Picture picture = new Picture();
                picture.setPictureLink(pictureURL);
                return picture;
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("Picture corresponding to user was not found"));
            }

        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("User to find corresponding picutures does not exist"));
        }
    }

//****SCREENSHOT HANDLING

    /**
     * Saves screenshot from Controller to the corresponding Gameplay Entity
     *
     * @param submittedShot
     * @param //userId
     */
    public void saveScreenshot(Screenshot submittedShot, String username) {
        User user = userRepository.findByUsername(username);
        user.setScreenshotURL(submittedShot.getURL());
        userRepository.save(user);
        userRepository.flush();
    }

    // Currently unused
    public List<Screenshot> getScreenshots(String lobbyID) throws ResponseStatusException {
        checkLobbyExists(lobbyID); // throws ResponseStatusException
        GamePlay gamePlay = gameSessionRepository.findByCorrespondingLobbyID(lobbyID);
        return gamePlay.getListOfScreenshots();
    }

    public ArrayList<ArrayList<String>> getUsersScreenshots(String lobbyId) throws ResponseStatusException {
        checkLobbyExists(lobbyId); //throws ResponseStatusException
        ArrayList<ArrayList<String>> response = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

        LobbyService lobbyService = new LobbyService(this.lobbyRepository, this.userRepository);
        List<User> usersList = lobbyService.getUsersInLobby(lobbyId);

        for (User u : usersList) {
            temp.add(u.getUsername());
            temp.add(u.getScreenshotURL());
            response.add(temp);
            temp = new ArrayList<>();
        }

        return response;
    }

//*****GUESSING handlers

    public Map<String, String> getGuessesHashMap(String guesses) {
        // convert string and save values into hashmap
        String tempUsername = "";
        String tempCoordinates = "";
        Map<String, String> result = new HashMap<String, String>();

        for (int i = 0; i < guesses.length(); i++) {
            for (int j = 0; j < 2; j++) { // first 2 letters are coordinates
                tempCoordinates += guesses.charAt(i + j);
            }
            i += 2; // skip coordinates, goto username

            while (i < guesses.length() - 1 && guesses.charAt(i) != '-') {
                tempUsername += guesses.charAt(i);
                i++;
            }
            result.put(tempUsername, tempCoordinates);
            tempUsername = tempCoordinates = "";
        }

        return result;
    }

    public String handleGuesses(String lobbyId, User user) {

        LobbyService lobbyService = new LobbyService(this.lobbyRepository, this.userRepository);
        List<User> usersList = lobbyService.getUsersInLobby(lobbyId);

        String username = user.getUsername();
        User player = null;
        if(username != null){
            player = userRepository.findByUsername(user.getUsername());
        }
        else{
            System.out.println("Sorry, the username I got was null!");
        }

        // convert String(guesses) to hashmap with username and coordinate
        String result = "";
        if (player != null) {
            Map<String, String> guesses = getGuessesHashMap(user.getGuesses());

            // correct guesses
            String[] coordinateNames = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4"};
            for (Map.Entry<String, String> entry : guesses.entrySet()) {
                User tempUsr = userRepository.findByUsername(entry.getKey());
                // check if coordinates match
                if (tempUsr != null) {
                    System.out.println("--------");
                    System.out.println("USERNAME: "+player.getUsername());
                    System.out.println("coord: "+coordinateNames[tempUsr.getAssignedCoordinates()]+" GUESS: "+entry.getValue().toUpperCase());
                    if (coordinateNames[tempUsr.getAssignedCoordinates()].equals(entry.getValue().toUpperCase())) {
                        player.setPoints(player.getPoints() + 1);   // give player a point
                        tempUsr.setPoints(tempUsr.getPoints() + 1); // also give to other player a point
                        result += "y" + entry.getKey();
                        System.out.println("--------");
                        System.out.println("user: "+player.getPoints());
                        System.out.println("other: "+tempUsr.getPoints());
                        System.out.println("--------");

                        // update user repo
                        userRepository.save(player);
                        userRepository.save(tempUsr);
                        userRepository.flush();
                    }
                    else {
                        result += "n" + entry.getKey();
                    }
                    result += "-";
                }
            }
            player.setCorrectedGuesses(result);
            userRepository.save(player);
            userRepository.flush();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guesses could not be assigned to users");
        }

        User test = userRepository.findByUsername(username);
        System.out.println("updated points?: "+test.getPoints());

        return result;
    }

    public Map<String, Map<String, String>> returnScore(String lobbyId) throws ResponseStatusException {
        checkLobbyExists(lobbyId); //throws Runtime Exception
        LobbyService lobbyService = new LobbyService(this.lobbyRepository, this.userRepository);
        List<User> usersList = lobbyService.getUsersInLobby(lobbyId);

        String correctedGuesses;
        Map<String, String> temp = new HashMap<>();
        String username = "";
        String answer = "";
        Map<String, Map<String, String>> result = new HashMap<>(); // { username:{max:y,eva:n}, username:{max:y,eva:n}}

        for (User usr : usersList) {
            correctedGuesses = usr.getCorrectedGuesses();
            System.out.println(usr.getUsername()+" "+usr.getCorrectedGuesses());
            if (correctedGuesses != null) {
                // convert
                for (int i = 0; i < correctedGuesses.length(); i++) {
                    answer += correctedGuesses.charAt(i);
                    i++; // skip answer "y"/"n"
                    // parse username
                    while (i < correctedGuesses.length() - 1 && correctedGuesses.charAt(i) != '-') {
                        username += correctedGuesses.charAt(i);
                        i++;
                    }
                    temp.put(username, answer);
                    username = answer = ""; // reset
                }
                temp.put("points", String.valueOf(usr.getPoints()));
                result.put(usr.getUsername(), temp);
            }
            //System.out.println(usr.getUsername()+" "+result.get(usr.getUsername()));
        }

        // set to false for next round
        gameSessionRepository.findByCorrespondingLobbyID(lobbyId).setRoundInited(false);

        return result;
    }

//*****INPUT CHECK METHODS

    /**
     * Used to check that Lobby ID received is in the Repository
     *
     * @param lobbyID
     * @throws ResponseStatusException
     */
    private void checkLobbyExists(String lobbyID) throws ResponseStatusException {
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyID);
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with " + lobbyID + " does not exist in repository");
        }
    }

//*****HELPER METHODS

    /**
     * Helper method used to shuffle lists for random assignment
     *
     * @param listLength
     * @return returns an Array of shuffled indices
     */
    public Integer[] getShuffledIdxList(int listLength) {
        // make array with indices to randomly assign sets
        Integer[] idxList = new Integer[listLength];
        for (int i = 0; i < listLength; i++) {
            idxList[i] = i;
        }

        // shuffle array/list to make random
        List<Integer> tempList = Arrays.asList(idxList);
        Collections.shuffle(tempList);
        tempList.toArray(idxList);

        return idxList;
    }

    public void saveScreenshotURL(String screenshotURL, String username) {
        User user = userRepository.findByUsername(username);

        if(user != null){
            user.setScreenshotURL(screenshotURL.replace("\"", ""));
        }
        else{
            // TODO throw exception here
            System.out.println("ERROR: can't save screenshot URL because user was not found.");
        }
    }
}


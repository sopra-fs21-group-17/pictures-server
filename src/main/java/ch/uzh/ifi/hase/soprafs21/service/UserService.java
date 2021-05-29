package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @return returns a List of all users.
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUser(String username){
        User user = userRepository.findByUsername(username);
        return user;
    }

    public List<User> getUsersInLobby(String lobbyId) {

        List<User> usersInLobby = new ArrayList<>();
        List<User> allUsers = getUsers();

        for (User user : allUsers) {
            if(user.getLobbyId() != null){
                if (user.getLobbyId().equals(lobbyId)) {
                    usersInLobby.add(user);
                }
            }
        }

        userRepository.flush();

        return usersInLobby;
    }

    /**
     * Gets user from Repository if User exists
     * @param userInput
     * @return returns user from userRepository
     */
    public User getUserLogin(User userInput){

        //checks if user exists and if the password is correct
        checkUserLogin(userInput);

        return userRepository.findByUsername(userInput.getUsername());
    }

    /**
     * Saves a new User to the UserRepository
     * @param newUser
     * @return newUser
     */
    public User createUser(User newUser) throws ParseException {
        newUser.setToken(UUID.randomUUID().toString());

        checkIfUserExists(newUser);

        newUser.setIsReady(false);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        //log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    //updates isReady of the User
    public void updateIsReady(String username, User user){
        User foundByName = userRepository.findByUsername(username);
        checkUserLogin(foundByName);
        if (foundByName.getIsReady() == false){
            foundByName.setIsReady(true);
            userRepository.flush();
        }else{
            foundByName.setIsReady(false);
            userRepository.flush();
        }

    }

    //updates isReadyBuildScreen of the User
    public void updateIsReadyBuildScreen(String username, User user){
        User foundByName = userRepository.findByUsername(username);
        checkUserLogin(foundByName);
        //if (foundByName.isReadyBuildScreen() == false){
        foundByName.setReadyBuildScreen(true);
        userRepository.flush();
        //}else{
        //  foundByName.setReadyBuildScreen(false);
          //  userRepository.flush();
        //}

    }
    public void setReadyFalseForBuildScreen(String username, User user){
        User foundByName = userRepository.findByUsername(username);
        checkUserLogin(foundByName);
        //if (foundByName.isReadyBuildScreen() == false){
        foundByName.setReadyBuildScreen(false);
        userRepository.flush();
    }

    public void doneGuessingTrue(String username){
        User foundByName = userRepository.findByUsername(username);

        if(foundByName != null) {
            foundByName.setDoneGuessing(true);
            userRepository.flush();
        }
        else{ System.out.println("Sorry, username not found!"); }

    }


    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());


        String baseErrorMessage = "username already taken, therefore the User could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, (baseErrorMessage));
        }

    }

    /**checks if the username and password are correct and if the user is registered
     * @throws ResponseStatusException
     * @param userToBeFound
     */
    private void checkUserLogin(User userToBeFound){
        User userByUsername = userRepository.findByUsername(userToBeFound.getUsername());

        if(userByUsername != null){
            if(!userByUsername.getUsername().equalsIgnoreCase(userToBeFound.getUsername()) || !userByUsername.getPassword().equalsIgnoreCase(userToBeFound.getPassword())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password!");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not registered!");
        }
    }
}

package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class LobbyService {

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier ("lobbyRepository")LobbyRepository lobbyRepository, UserRepository userRepository){

        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
    }

    public List<User> getUsersInLobby(String lobbyId) {
        List<User> usersInLobby= userRepository.findDistinctUserBy(lobbyId);

//        List<User> allUsers = this.userRepository.findAll();
//        for(User user: allUsers){
//            Lobby userLobby = user.getLobby();
//            if (userLobby.getLobbyId() == lobbyId){
//                usersInLobby.add(user);
//            }
//        }
//        Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);
//        List<User> usersInLobby = currentLobby.getUserList();
        return usersInLobby;
    }


//    public List<User> getUsers(String lobbyId) {
//        Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);
//        //List<User> usersInLobby = userRepository.findAllBy(currentLobby);
//        List<User> usersInLobby = currentLobby.getUserList();
//        return usersInLobby ;
//    }

    public Lobby createLobby(Lobby newLobby)  {

        checkIfLobbyExists(newLobby);
        //newUser.setIsReady(false);

        // saves the given entity but data is only persisted in the database once flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        //log.debug("Created Information for User: {}", newUser);
        return newLobby;
    }

    public void addUserToLobby(User user, String lobbyId){
        String username = user.getUsername();
        User userToAdd = userRepository.findByUsername(username);
        //Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);
        userToAdd.setLobbyId(lobbyId);

        userRepository.flush();
        lobbyRepository.flush();
    }

    public Lobby checkReadyAndGetCount(String lobbyId){
        int countReady = 0;
        int countUsers = 0;

        Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);
        List<User>usersInLobby = userRepository.findDistinctUserBy(lobbyId);
        for(User user : usersInLobby){
            countUsers += 1;
            if (user.getIsReady()){
                countReady += 1;
            }
        }
        currentLobby.setPlayersCount(countUsers);
        if (countReady >= 3 && countReady == countUsers){
            currentLobby.setLobbyReady(true);
        }else{
            currentLobby.setLobbyReady(false);
        }
        return currentLobby;
    }

    public int countDown(){
        int count = 100;
        return count;

    }

    private void checkIfLobbyExists(Lobby lobbyToBeCreated) {
        Lobby lobbyByLobbyId = lobbyRepository.findByLobbyId(lobbyToBeCreated.getLobbyId());


        String baseErrorMessage = "lobby already taken, therefore the User could not be created!";
        if (lobbyByLobbyId != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, (baseErrorMessage));
        }

    }
    //checks if the lobbyId exists and is valid
    public void checkLobbyId(String lobbyId){
        Lobby lobbyById = lobbyRepository.findByLobbyId(lobbyId);

        if(lobbyById != null){
            if(lobbyById.getLobbyId() != lobbyId){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Game code!");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Game Code!");
        }
    }
}

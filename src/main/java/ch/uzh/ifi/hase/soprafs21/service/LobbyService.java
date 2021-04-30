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

    //returns the list of Users in the Lobby
    public List<User> getUsersInLobby(String lobbyId) {
        List<User> usersInLobby= new ArrayList<>();
        List<User> allUsers = this.userRepository.findAll();
        System.out.println(allUsers);

        for(User user: allUsers){
            if (user.getLobbyId().equals(lobbyId)){
                usersInLobby.add(user);
            }
        }

        return usersInLobby;
    }

    //creates a new Lobby
    public Lobby createLobby(Lobby newLobby)  {

        checkIfLobbyExists(newLobby);
        newLobby.setCreationTime(System.nanoTime());

        // saves the given entity but data is only persisted in the database once flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        //log.debug("Created Information for User: {}", newUser);
        return newLobby;
    }

    //calculates and sets the timeDifference in order to after update the count
    public void updateCount(String lobbyId){
        Lobby foundByLobbyId = lobbyRepository.findByLobbyId(lobbyId);

        long currentTime = System.nanoTime();

        long timeDifference = foundByLobbyId.getCreationTime() - currentTime;

        foundByLobbyId.setTimeDifference((double) timeDifference /1_000_000_000);

        lobbyRepository.flush();

    }

    //adds  User to the Lobby
    public void addUserToLobby(User user, String lobbyId){
        String username = user.getUsername();


        User userToAdd = userRepository.findByUsername(username);
        userToAdd.setLobbyId(lobbyId);
        userToAdd.setIsReady(false);



        userRepository.flush();
        lobbyRepository.flush();
    }


    //checks if the User are ready and gets the Players count in the lobby
    public Lobby checkReadyAndGetCount(String lobbyId){
        int countReady = 0;
        int countUsers = 0;

        Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);
        List<User> usersInLobby = getUsersInLobby(lobbyId);

        for(User user : usersInLobby){
            countUsers += 1;
            if (user.getIsReady()){
                countReady += 1;
            }
        }
        currentLobby.setPlayersCount(countUsers);
        if (countReady >= 3 && countReady == countUsers){
            currentLobby.setLobbyReady(true);
        }else if (countReady == 5){
            currentLobby.setLobbyReady(true);

        }else{
            currentLobby.setLobbyReady(false);
        }
        lobbyRepository.flush();

        return currentLobby;
    }


    //checks if the lobby exists and is full
    private void checkIfLobbyExists(Lobby lobbyToBeCreated) {
        Lobby lobbyByLobbyId = lobbyRepository.findByLobbyId(lobbyToBeCreated.getLobbyId());

        String baseErrorMessage = "lobby already taken, therefore the User could not be added to the lobby!";
        if (lobbyByLobbyId != null && lobbyByLobbyId.getPlayersCount() >= 5) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, (baseErrorMessage));
        }

    }
    //checks if the lobbyId exists and is valid
    public void checkLobbyId(String lobbyId){
        Lobby lobbyById = lobbyRepository.findByLobbyId(lobbyId);

        if(lobbyById != null){
            if(!lobbyById.getLobbyId().equals(lobbyId)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Game Code!");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Game Code1!");
        }
    }
}

package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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

import java.util.*;

@Service
@Transactional
public class LobbyService {

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository){
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

        // neu hinzugefügt von Julia & Oli ...
        lobbyRepository.findByLobbyId(lobbyId).getUsersList().add(userToAdd);

        userRepository.flush();
        lobbyRepository.flush();
    }

    public void removeUserFromLobby(String username, String lobbyId){
        User userToRemove = userRepository.findByUsername(username);
        lobbyRepository.findByLobbyId(lobbyId).getUsersList().remove(userToRemove);

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

    public Set<User> lobbyIsReady(String lobbyId){

//        Lobby createdLobby;
//        // für testzwecke lobby erzeugen
//        if(lobbyRepository.findByLobbyId(lobbyId) == null) {
//            LobbyPostDTO testInput = new LobbyPostDTO();
//            testInput.setLobbyId(lobbyId);
//            Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(testInput);
//            createdLobby = createLobby(lobbyInput);
//            lobbyRepository.save(createdLobby);
//            lobbyRepository.flush();
//
//            // Users manuell erzeugen für Testzwecke
//            String[] userNames = {"JULIA", "DOMINIK", "OLIVER", "SHINO", "VIKTOR"};
//            Set<User> testUsersList =   new HashSet<>();
//            int NR_OF_PLAYERS = 4;
//            for(int i = 0; i < NR_OF_PLAYERS; i++){
//                User user = new User();
//                user.setUsername(userNames[i]);
//                user.setAssignedCoordinates(i);
//                user.setPoints(0); // init all points to 0
//                user.setScreenshotURL("https://i.insider.com/5484d9d1eab8ea3017b17e29?width=600&format=jpeg&auto=webp");
//
//                userRepository.save(user);
//                userRepository.flush();
//
//                testUsersList.add(user);
//            }
//            createdLobby.setUsersList(testUsersList);
//            lobbyRepository.flush();
//        }


        // uncomment to use real repo
        Lobby lobby = lobbyRepository.findByLobbyId(lobbyId);
        return lobby.getUsersList();
    }


    public Lobby getTestLobby(String lobbyId) {
        Lobby createdLobby;
        // für testzwecke lobby erzeugen
        if(lobbyRepository.findByLobbyId(lobbyId) == null) {
            LobbyPostDTO testInput = new LobbyPostDTO();
            testInput.setLobbyId(lobbyId);
            Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(testInput);
            createdLobby = createLobby(lobbyInput);
            lobbyRepository.save(createdLobby);
            lobbyRepository.flush();

            // Users manuell erzeugen für Testzwecke
            String[] userNames = {"JULIA", "DOMINIK", "OLIVER", "SHINO", "VIKTOR"};
            Set<User> testUsersList =   new HashSet<>();
            int NR_OF_PLAYERS = 4;
            for(int i = 0; i < NR_OF_PLAYERS; i++){
                User user = new User();
                user.setUsername(userNames[i]);
                user.setAssignedCoordinates(i);
                user.setPoints(0); // init all points to 0
                user.setScreenshotURL("https://i.insider.com/5484d9d1eab8ea3017b17e29?width=600&format=jpeg&auto=webp");

                userRepository.save(user);
                userRepository.flush();

                testUsersList.add(user);
            }
            createdLobby.setUsersList(testUsersList);
            lobbyRepository.flush();
        }



        return lobbyRepository.findByLobbyId(lobbyId);
    }
}

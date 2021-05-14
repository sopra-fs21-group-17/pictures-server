package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//TODO test GameService corresponding to GameSessionRepository --> mulitple  Lobbies correctly fetched?
//TODO test that an instance has not been entered at the wrong time when initializing game

@WebAppConfiguration
@SpringBootTest
@Transactional
public class GameServiceIntegrationTest {
    @Qualifier("picturesRepository")
    @Autowired
    private PicturesRepository picturesRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Qualifier("gameSessionRepository")
    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private GameService gameService;



    @BeforeEach
    public void setup(){
        picturesRepository.deleteAll();
        userRepository.deleteAll();
        lobbyRepository.deleteAll();
        gameSessionRepository.deleteAll();

    }
    @Test
    public void testGetPictureUsingUserIDSuccess(){
        //initialize Lobby add to LobbyRepository
        Lobby lobby = new Lobby();
        lobby.setLobbyId("testLobby_1");
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

         //initialize testUser add to UserRepository
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setAssignedCoordinates(1);
        testUser.setLobbyId("testLobby_1");
        testUser.setPassword("Test");
        userRepository.save(testUser);
        userRepository.flush();

        //initialize Gameplay
        GamePlay testGameplay = new GamePlay();
        testGameplay.setCorrespondingLobbyID("testLobby_1");

        //add GamePlay to gameSession repository
        gameSessionRepository.save(testGameplay);
        gameSessionRepository.flush();

        //initialize Picture add to GamePlay entity
        Picture testPicture = new Picture();
        testPicture.setPictureLink("testLink");
        testPicture.setId(1L);
        gameSessionRepository.findByCorrespondingLobbyID("testLobby_1").addPicture(testPicture,1);



        //when
        Picture result = gameService.getCorrespondingToUser(userRepository.findByUsername("TestUser").getId());

        //then
        assertEquals(testUser.getLobbyId(),lobby.getLobbyId());
        assertEquals(testPicture.getCoordinates(),result.getCoordinates());
        assertEquals(testPicture.getPictureLink(),result.getPictureLink());


    }
    @Test
    public void testGetPictureUsingUserIDFail(){
        //initialize Lobby add to LobbyRepository
        Lobby lobby = new Lobby();
        lobby.setLobbyId("testLobby_1");
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        //initialize User for failure intentionally missing userRepository.save and flush()
        User testUser1 = new User();
        testUser1.setUsername("TestUser1");
        testUser1.setPassword("Test");
        testUser1.setAssignedCoordinates(1);
        testUser1.setLobbyId("testLobby_1");
        testUser1.setId(3L);

        //initialize User for successful user find
        User testUser2 = new User();
        testUser2.setUsername("TestUser2");
        testUser2.setPassword("Test2");
        testUser2.setAssignedCoordinates(2);
        testUser2.setLobbyId("testLobby_1");

        userRepository.save(testUser2);
        userRepository.flush();

        //initialize Gameplay
        GamePlay testGameplay = new GamePlay();
        testGameplay.setCorrespondingLobbyID("testLobby_1");

        //initialize Picture add to GamePlay entity
        Picture testPicture = new Picture();
        testPicture.setPictureLink("testLink");
        testPicture.setId(1L);
        testGameplay.addPicture(testPicture,1);

        //add GamePlay to gameSession repository
        gameSessionRepository.save(testGameplay);
        gameSessionRepository.flush();

        assertThrows(ResponseStatusException.class,() ->gameService.getCorrespondingToUser(3L));
        assertThrows(ResponseStatusException.class,() ->gameService.getCorrespondingToUser(userRepository.findByUsername("TestUser2").getId()));
    }

    @Test
    public void testInitGameWithLobbyID(){

    }

}

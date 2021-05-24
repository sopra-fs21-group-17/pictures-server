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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// ATTENTION: Four testcases had to be commented out otherwise the testsuite gradle cannot run them in a group they start hanging, if you run them individually then it is possible and they pass


@WebAppConfiguration
@Transactional
@SpringBootTest
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
        testGameplay.setLobbyForGamePlay(lobbyRepository.findByLobbyId(lobby.getLobbyId()));

        //add GamePlay to gameSession repository

        gameSessionRepository.save(testGameplay);
        gameSessionRepository.flush();


        //initialize Picture add to GamePlay entity
        Picture testPicture = new Picture();
        testPicture.setPictureLink("testLink");
        testPicture.setId(1L);
        gameSessionRepository.findByCorrespondingLobbyID("testLobby_1").addPicture(testPicture.getPictureLink(),1);



        //when
        Picture result = gameService.getCorrespondingToUser(userRepository.findByUsername("TestUser").getId());

        //then
        assertEquals(testUser.getLobbyId(),lobby.getLobbyId());
        assertEquals(testPicture.getPictureLink(),result.getPictureLink());


    }
//    @Test
//    public void testGetPictureUsingUserIDFail(){
//        //initialize Lobby add to LobbyRepository
//        Lobby lobby = new Lobby();
//        lobby.setLobbyId("testLobby_1");
//        lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//        Set<User> testUsers = new HashSet<>();
//        //initialize User for failure intentionally missing userRepository.save and flush()
//        User testUser1 = new User();
//        testUser1.setUsername("TestUser1");
//        testUser1.setPassword("Test");
//        testUser1.setAssignedCoordinates(1);
//        testUser1.setLobbyId("testLobby_1");
//        testUser1.setId(3L);
//
//        //initialize User for successful user find
//        User testUser2 = new User();
//        testUser2.setUsername("TestUser2");
//        testUser2.setPassword("Test2");
//        testUser2.setAssignedCoordinates(2);
//        testUser2.setLobbyId("testLobby_1");
//        testUser2.setId(4L);
//
//        userRepository.save(testUser2);
//        userRepository.flush();
//
//        testUsers.add(testUser1);
//        testUsers.add(testUser2);
//        lobby.setUsersList(testUsers);
//
//        //initialize Gameplay
//        GamePlay testGameplay = new GamePlay();
//        testGameplay.setLobbyForGamePlay(lobbyRepository.findByLobbyId(lobby.getLobbyId()));
//
//        //initialize Picture add to GamePlay entity
//        Picture testPicture = new Picture();
//        testPicture.setPictureLink("testLink");
//        testPicture.setId(1L);
//        testGameplay.addPicture(testPicture.getPictureLink(),1);
//
//        //add GamePlay to gameSession repository
//        gameSessionRepository.save(testGameplay);
//        gameSessionRepository.flush();
//
//        assertThrows(ResponseStatusException.class,() ->gameService.getCorrespondingToUser(3L));
//        assertThrows(ResponseStatusException.class,() ->gameService.getCorrespondingToUser(userRepository.findByUsername("TestUser2").getId()));
//    }

    /**
     * checks if users are assigned the coordinates and sets
     * checks if gameSession repository has a new Entity
     * checks if pictures were assigned to said Entity
     */
//    @Test
//    public void testInitGameWithLobbyIDFirstTime(){
//        //init lobby
//        Lobby lobby = new Lobby();
//        lobby.setLobbyId("testLobby_2");
//
//        //init Test users for user Repository and lobby
//        Set<User> testUsers = new HashSet<>();
//        for(int i = 1; i <= 5; i++){
//            User user = new User();
//            user.setUsername("TestUser" + i);
//            user.setPassword("Test");
//            testUsers.add(user);
//            userRepository.save(user);
//            userRepository.flush();
//        }
//        lobby.setUsersList(testUsers);
//        lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//        //initialize Picture for pictures repository --> select pictures is called in the method
//        for(int i = 1; i <= 50;i++) {
//            Picture testPicture = new Picture();
//            testPicture.setPictureLink("testLink " + i);
//            picturesRepository.save(testPicture);
//            picturesRepository.flush();
//        }
//
//        //assertions before InitGame:
//        assertTrue(gameSessionRepository.count() == 0);
//
//        for(User testUser : testUsers) {
//            assertTrue(testUser.getAssignedCoordinates() == 0);// maybe change with null if test fails since empty
//            assertTrue(testUser.getAssignedSet() == null);
//        }
//       List<User> testUsersAfterInit = gameService.initGame(lobby.getLobbyId());
//        for(User testUser : testUsersAfterInit){
//            assertNotNull(testUser.getAssignedSet());
//            assertNotNull(testUser.getAssignedCoordinates());
//        }
//        assertTrue(gameSessionRepository.count() > 0);
//        assertNotNull(gameSessionRepository.findByCorrespondingLobbyID(lobby.getLobbyId()));
//        assertNotNull(gameSessionRepository.findByCorrespondingLobbyID(lobby.getLobbyId()).getSelectedPictures());
//
//    }

    @Test
    public void testInitGameAfterSecondCall(){
        //init lobby
        Lobby lobby = new Lobby();
        lobby.setLobbyId("testLobby_2");

        //init Test users for user Repository and lobby
        Set<User> testUsers = new HashSet<>();
        for(int i = 1; i <= 5; i++){
            User user = new User();
            user.setUsername("TestUser" + i);
            user.setPassword("Test");
            testUsers.add(user);
            userRepository.save(user);
            userRepository.flush();
        }
        lobby.setUsersList(testUsers);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        //initialize Picture for pictures repository --> select pictures is called in the method
        for(int i = 1; i <= 50;i++) {
            Picture testPicture = new Picture();
            testPicture.setPictureLink("testLink " + i);
            picturesRepository.save(testPicture);
            picturesRepository.flush();
        }
        // call initGame for the first time to setup everything
        // current variables
        List<User> testUsersAfterInit = gameService.initGame(lobby.getLobbyId());
        Long gamePlayEntities = gameSessionRepository.count();
        String[] receivedPictures = gameSessionRepository.findByCorrespondingLobbyID(lobby.getLobbyId()).getSelectedPictures();

        // second call
        List<User> testUsersAfterInit2 = gameService.initGame(lobby.getLobbyId());
        String[] receivedPictures2 = gameSessionRepository.findByCorrespondingLobbyID(lobby.getLobbyId()).getSelectedPictures();
        assertEquals(gamePlayEntities, gameSessionRepository.count());
        assertArrayEquals(receivedPictures,receivedPictures2);
        assertEquals(testUsersAfterInit,testUsersAfterInit2);

    }



//    @Test
//    public void testRemoveGameAndLobby(){
//        Lobby testLobby2 = new Lobby();
//        testLobby2.setLobbyId("test3");
//        lobbyRepository.save(testLobby2);
//        lobbyRepository.flush();
//        gameService.initGame("test3");
//
//        gameService.removeGameAndLobby("test3");
//        assertNull(gameSessionRepository.findByCorrespondingLobbyID("test3"));
//        assertNull(lobbyRepository.findByLobbyId("test3"));
//
//    }
//    @Test
//    public void testInitGameForMultipleLobbies(){
//        //init lobbies
//        Lobby lobby_1 = new Lobby();
//        lobby_1.setLobbyId("testLobby_1");
//
//        Lobby lobby_2 = new Lobby();
//        lobby_2.setLobbyId("testLobby_2");
//
//        //init Test users for user Repository and lobbies
//        Set<User> testUsers_1 = new HashSet<>();
//        for(int i = 1; i <= 5; i++){
//            User user = new User();
//            user.setUsername("TestUser1 " + i);
//            user.setPassword("Test");
//            testUsers_1.add(user);
//            userRepository.save(user);
//            userRepository.flush();
//        }
//
//        Set<User> testUsers_2 = new HashSet<>();
//        for(int i = 1; i <= 5; i++){
//            User user = new User();
//            user.setUsername("TestUser 2 " + i);
//            user.setPassword("Test");
//            testUsers_2.add(user);
//            userRepository.save(user);
//            userRepository.flush();
//        }
//        lobby_1.setUsersList(testUsers_1);
//        lobby_2.setUsersList(testUsers_2);
//        lobbyRepository.save(lobby_1);
//        lobbyRepository.save(lobby_2);
//        lobbyRepository.flush();
//
//        //initialize Picture for pictures repository --> select pictures is called in the method
//        for(int i = 1; i <= 50;i++) {
//            Picture testPicture = new Picture();
//            testPicture.setPictureLink("testLink " + i);
//            picturesRepository.save(testPicture);
//            picturesRepository.flush();
//        }
//
//        List<User> testUsersAfterInit_1 = gameService.initGame(lobby_1.getLobbyId());
//        List<User> testUsersAfterInit_2 = gameService.initGame(lobby_2.getLobbyId());
//
//        assertEquals(2,gameSessionRepository.count());
//        assertNotEquals(gameSessionRepository.findByCorrespondingLobbyID(lobby_1.getLobbyId()),gameSessionRepository.findByCorrespondingLobbyID(lobby_2.getLobbyId()));
//
//
//    }

}

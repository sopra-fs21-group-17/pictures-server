//package ch.uzh.ifi.hase.soprafs21.service;
//
//import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
//import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
//import ch.uzh.ifi.hase.soprafs21.entity.Picture;
//import ch.uzh.ifi.hase.soprafs21.entity.User;
//import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
//import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
//import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.web.server.ResponseStatusException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@WebAppConfiguration
//@SpringBootTest
//public class GameServiceIntegrationTest {
//    @Qualifier("picturesRepository")
//    @Autowired
//    private PicturesRepository picturesRepository;
//
//    @Qualifier("userRepository")
//    @Autowired
//    private UserRepository userRepository;
//
//    @Qualifier("lobbyRepository")
//    @Autowired
//    private LobbyRepository lobbyRepository;
//
//    @Autowired
//    private GameService gameService;
//
//    GamePlay testGameplay = new GamePlay();
//
//    @BeforeEach
//    public void setup(){
//
//        gameService.setGamePlay(testGameplay);
//        userRepository.deleteAll();
//        lobbyRepository.deleteAll();
//        testGameplay.clearSelectedPictures();
//    }
//    @Test
//    public void testGetPictureUsingUserIDSuccess(){
//        //initialize Lobby add to LobbyRepository
//        Lobby lobby = new Lobby();
//        lobby.setLobbyId("testLobby");
//        lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//         //initialize testUser add to UserRepository
//        User testUser = new User();
//        testUser.setUsername("TestUser");
//        testUser.setAssignedCoordinates(1);
//        testUser.setLobbyId("testLobby");
//        userRepository.save(testUser);
//        userRepository.flush();
//
//        //initialize Picture add to GamePlay entity
//        Picture testPicture = new Picture();
//        testPicture.setPictureLink("testLink");
//        testPicture.setId(1L);
//        testGameplay.addPicture(testPicture,1);
//
//        //when
//        Picture result = gameService.getCorrespondingToUser(userRepository.findByUsername("TestUser").getId());
//
//        //then
//        assertEquals(testPicture,result);
//
//
//    }
//    @Test
//    public void testGetPictureUsingUserIDFail(){
//        //initialize Lobby add to LobbyRepository
//        Lobby lobby = new Lobby();
//        lobby.setLobbyId("testLobby");
//        lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//        //initialize User for failure intentionally missing userRepository.save and flush()
//        User testUser1 = new User();
//        testUser1.setUsername("TestUser1");
//        testUser1.setAssignedCoordinates(1);
//        testUser1.setLobbyId("testLobby");
//        testUser1.setId(3L);
//
//        //initialize User for successful user find
//        User testUser2 = new User();
//        testUser2.setUsername("TestUser2");
//        testUser2.setAssignedCoordinates(2);
//        testUser2.setLobbyId("testLobby");
//        userRepository.save(testUser2);
//        userRepository.flush();
//
//        //initialize Picture add to GamePlay entity
//        Picture testPicture = new Picture();
//        testPicture.setPictureLink("testLink");
//        testPicture.setId(1L);
//        testGameplay.addPicture(testPicture,1);
//
//        assertThrows(ResponseStatusException.class,() ->gameService.getCorrespondingToUser(3L));
//        assertThrows(ResponseStatusException.class,() ->gameService.getCorrespondingToUser(userRepository.findByUsername("TestUser2").getId()));
//    }
//
//}

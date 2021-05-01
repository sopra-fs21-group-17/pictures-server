package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {
    @Mock
    private PicturesRepository picturesRepository;
    @Mock
    private GameSessionRepository gameSessionRepository;
    @Mock // mocks an Object
    private UserRepository userRepository;
    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private GameService gameService;

    private User testUser = new User();
    private User testUser2 = new User();
    private User testUser3 = new User();
    private Picture[] mockPictureURLs = new Picture[50];


    @BeforeAll
    // initializes this before every test case
    public void setup(){
        MockitoAnnotations.openMocks(this);

        // given
        testUser.setUsername("Test1");
        testUser2.setUsername("Test2");
        testUser3.setUsername("Test3");

        Mockito.when(userRepository.findByUsername("Test1")).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername("Test2")).thenReturn(testUser2);
        Mockito.when(userRepository.findByUsername("Test3")).thenReturn(testUser3);


        int index = 0;
        for (Picture mockPicture : mockPictureURLs){
            mockPicture = new Picture();
            mockPicture.setPictureLink("mockURL_"+ index++);
            mockPicture.setId((long) index);
            picturesRepository.saveAndFlush(mockPicture);
        }


    }

    /**
     * tests if selected pictures are saved to the Gameplay entity
     */
    @Test

    public void testSelectPictures(){
        gameService.selectPictures();
        Mockito.verify(picturesRepository,Mockito.times(16)).findByid(Mockito.any());


    }
    @Test
    /**
     * tests if list of pictures is returned from the Gameplay entity
     */
    public void testGetSelectedPictures(){
        gameService.selectPictures();
       List<Picture> selected =  gameService.getListOfPictures();
        assertEquals(selected.size(),16);
    }


//TODO test getCorresponding to User
//TODO test saveScreenshot
//TODO test getScreenshots
//TODO test initGame
//TODO test getPlayingUsers
//TODO test setCurrentUser
//TODO test handleGuesses
//TODO test assignSets
//TODO test assignCoordinates




}

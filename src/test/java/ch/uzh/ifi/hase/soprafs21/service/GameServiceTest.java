package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    @Mock
    private PicturesRepository picturesRepository;
    @Mock
    private GameSessionRepository gameSessionRepository;
    @Mock // mocks an Object
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    private User testUser = new User();
    private Picture[] mockPictureURLs = new Picture[50];

    @Spy    // actual testGamePlay is executed with content but is watched
    private GamePlay testGamePlay = new GamePlay();

    @BeforeAll// initializes this before every test case
    public void setup(){
        MockitoAnnotations.openMocks(this);

        // given
        testUser.setUsername("TestUser");
        testUser.setId(1L);
        testUser.setAssignedCoordinates(5);  // is actually Token  //TODO diskutiere wege bereinigung
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));



        int index = 0;
        for (Picture mockPicture : mockPictureURLs){
            mockPicture = new Picture();
            mockPicture.setPictureLink("mockURL_"+ index++);
            mockPicture.setId((long) index);
            picturesRepository.saveAndFlush(mockPicture);
        }

        testGamePlay.setGameID(1L);
        Mockito.when(gameSessionRepository.save(Mockito.any())).thenReturn(testGamePlay);
        Mockito.when(gameSessionRepository.findByGameID(Mockito.any())).thenReturn(testGamePlay);

    }

    /**
     * tests if selected pictures are saved to the Gameplay entity
     */
    @Test
    public void testSelectPictures(){
        gameService.selectPictures();
        Mockito.verify(gameSessionRepository,Mockito.times(1)).findByGameID(Mockito.any());
        Mockito.verify(picturesRepository,Mockito.times(16)).findByid(Mockito.any());
        Mockito.verify(testGamePlay,Mockito.times(16)).addPicture(Mockito.any(),Mockito.anyInt());


    }
    @Test
    /**
     * tests if list of pictures is returned from the Gameplay entity
     */
    public void testGetSelectedPictures(){
        gameService.selectPictures();
       List<Picture> selected =  gameService.getListOfPictures();
       Mockito.verify(testGamePlay,Mockito.times(1)).getSelectedPictures();
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

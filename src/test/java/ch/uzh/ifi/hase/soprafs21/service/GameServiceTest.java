package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {
    @Mock
    private PicturesRepository picturesRepository;

    @Mock
    private GameSessionRepository gameSessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private GameService gameService;

    private final User testUser = new User();

    Picture mockPicture = new Picture();
    private final Lobby testLobby = new Lobby();

    @Spy
    private final GamePlay testGameplay = new GamePlay();

    @Spy
    private final LobbyService testLobbyService = new LobbyService(lobbyRepository,userRepository);

    Screenshot testScreenshot = new Screenshot();

    /** Define test help information */
    private final ArrayList<User> testUsersList = new ArrayList<User>();
    private final String testLobbyID = "test";
    private final int NR_OF_PLAYERS = 3;    // at least 3 players needed to play the game
    private final int testCoordinates = 5;  // nr from 0-15 possible, will be used for multiple tests
    private final String[] coordinateNames = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4"};
    private final String testUsername = "Test1";
    /***/

    @BeforeAll
    // initializes this before every test case
    public void setup(){
        MockitoAnnotations.openMocks(this);

        // given test user
        testUser.setUsername("Test1");
        testUser.setId(1L);
        String testGuess = "";
        // create test guess
        testGuess += coordinateNames[testCoordinates];
        testGuess += testUsername;
        testGuess += "-";
        testUser.setGuesses(testGuess);
        testUser.setAssignedCoordinates(testCoordinates);

        Mockito.when(userRepository.findByUsername("Test1")).thenReturn(testUser);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);

        // given test lobby
        testLobby.setLobbyId("test");
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);

        testGameplay.setCorrespondingLobbyID("test");
        testGameplay.setLobbyForGamePlay(testLobby);
        Mockito.when(gameSessionRepository.findByCorrespondingLobbyID(Mockito.any())).thenReturn(testGameplay);
        // Mockito.when(testGameplay.getPictureWithCoordinates(Mockito.anyInt())).thenReturn(mockPicture.getPictureLink());

        testScreenshot.setURL("ScreenShotTest");
        testScreenshot.setUserID(testUser.getId());

        mockPicture.setPictureLink("mockURL_");
        mockPicture.setId(1L);
        Mockito.when(picturesRepository.findByid(Mockito.any())).thenReturn(mockPicture);
    }

    /**
     * tests if selected pictures are saved to the Gameplay entity
     */
    @Test
    public void testSelectPictures(){

        assertThrows(ResponseStatusException.class,() -> gameService.getListOfPictures("test"));
        gameService.selectPictures("test");
        Mockito.verify(picturesRepository,Mockito.atLeast(16)).findByid(Mockito.any());
        assertNotNull(gameService.getListOfPictures("test"));
    }

    @Test
    /**
     * tests if list of pictures is returned from the Gameplay entity
     */
    public void testGetSelectedPictures(){
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
        gameService.selectPictures("test");
        Picture[] selected =  gameService.getListOfPictures("test");
        assertEquals(selected.length,16);
    }

    /**
     * is used to simulate getting a picture when the service is given a userID
     */
    @Test
    public void testGetCorrespondingToUser()
    {
        
        gameService.initGame(testLobby.getLobbyId());
        testUser.setAssignedCoordinates(1);
        Picture picture = gameService.getCorrespondingToUser(testUser.getId());


        assertEquals(picture.getPictureLink(),mockPicture.getPictureLink());

    }

    /**
     * tests if the exceptions are thrown correctly
     * included the test for private method checkLobbyExists here;
     */
    @Test
    public void testGetCorrespondingToUserThrowsExceptions(){


        gameService.initGame(testLobby.getLobbyId());

        Mockito.when(testGameplay.getPictureWithCoordinates(Mockito.anyInt())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.getCorrespondingToUser(1L));
        Mockito.when(testGameplay.getPictureWithCoordinates(Mockito.anyInt())).thenReturn(mockPicture.getPictureLink());

        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(null);
        assertThrows(ResponseStatusException.class, ()-> gameService.getCorrespondingToUser(1L));
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.getCorrespondingToUser(2L) );
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);

    }

    /**
     * tests before and after saving screenshot to user
     */
    @Test
    public void testSaveScreenshot()
    {
        assertNull(testUser.getScreenshotURL());
        gameService.saveScreenshot(testScreenshot,testUser.getUsername());
        assertEquals(testUser.getScreenshotURL(), testScreenshot.getURL());


    }

    //Matches style of integration test more may move there
    @Test
    public void testInitGameHandlesCoordinatesHandlesSetsAssignment()
    {
        // initialize the list of testusers to simulate lobby content
        Set<User> testUsers = new HashSet<>();

        User testUser2 = new User();
        User testUser3 = new User();
        User testUser4 = new User();
        ArrayList<User> users= new ArrayList<>();
        users.add(testUser2);
        users.add(testUser3);
        users.add(testUser4);

        for(int i = 0; i < 3; i++){

            users.get(i).setUsername("Test" + (i+2));
            users.get(i).setId((long) (i+2));
            users.get(i).setLobbyId(testLobby.getLobbyId());
            assertTrue(users.get(i).getAssignedCoordinates() == 0);
            assertNull(users.get(i).getAssignedSet());
            testUsers.add(users.get(i));
        }
       testLobby.setUsersList(testUsers);
       List<User> testUsersAfter = gameService.initGame(testLobby.getLobbyId());

      for(User user : testUsersAfter){
           assertTrue(user.getAssignedCoordinates()>=0);
           assertNotNull(user.getAssignedSet());
       }



    }

    @Test
    public void testGetGamePlay(){
        assertEquals(gameService.getGamePlay("test"),testGameplay);
        Mockito.when(gameSessionRepository.findByCorrespondingLobbyID("test2")).thenReturn(null);
        assertThrows(ResponseStatusException.class,()-> gameService.getGamePlay("test2"));

    }

    @Test
    public void testPrepareNewRound(){
        testGameplay.setNumberOfPlayers(3);
        gameService.prepareNewRound("test");
        assertEquals(1,testGameplay.getAllUsersFinishedRound());
        assertNull(testGameplay.getSelectedPictures());
        gameService.prepareNewRound("test");
        gameService.prepareNewRound("test");
        assertEquals(1,testGameplay.getRoundsFinished());
    }

    @Test
    public void testResetRoundHandle(){
        testGameplay.setAllUsersFinishedRound(5);
        gameService.resetCounterForRoundHandling("test");
        assertEquals(0,testGameplay.getAllUsersFinishedRound());
    }

    @Test
    public void testRemoveUserFromLobby(){
        Set<User> testUserSet = new HashSet<>();
        testUserSet.add(testUser);
        testLobby.setUsersList(testUserSet);
        gameService.removeUserFromLobby("test",1L);

        testUserSet.remove(testUser);
        testLobby.setUsersList(testUserSet);

        assertEquals(0,lobbyRepository.findByLobbyId("test").getUsersList().size());
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(null);
        assertThrows(ResponseStatusException.class,()-> gameService.removeUserFromLobby("test",2L));
    }

//    @Test
//    public void guesses_all_correct(){
//
//        String expectedResult = "";
//        String testGuess = "";
//
//        // manually create guessing information
//
//        // create test guess
//        testGuess += coordinateNames[testCoordinates];
//        testGuess += testUsername;
//        testGuess += "-";
//
//        // create expected result
//        expectedResult += "y";
//        expectedResult += "Test1";
//        expectedResult += "-";
//
//        //testUser.setGuesses(testGuess);
//        assertEquals(expectedResult, gameService.handleGuesses("test", testUser));
//
//    }







}

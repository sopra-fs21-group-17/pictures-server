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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PointsLogicUnitTest {

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

    @Spy
    private final GamePlay testGameplay = new GamePlay();

    @Spy
    private final LobbyService testLobbyService = new LobbyService(lobbyRepository,userRepository);

    /** Define test help information */
    private final ArrayList<User> testUsersList = new ArrayList<User>();

    private final String testLobbyID = "test";

    private final int NR_OF_PLAYERS = 3;    // at least 3 players needed to play the game

    private final int testCoordinates = 5;  // nr from 0-15 possible, will be used for multiple tests

    private final String[] coordinateNames = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4"};

    private final String testUsername = "Test1";

    private final User testUser = new User();

    private final Lobby testLobby = new Lobby();
    /***/

    @BeforeAll
    // initializes this before every test case
    public void setup(){

        MockitoAnnotations.openMocks(this);

        // given test user
        testUser.setUsername("Test1");
        testUser.setId(1L);
        testUser.setAssignedCoordinates(testCoordinates);

        Mockito.when(userRepository.findByUsername("Test1")).thenReturn(testUser);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);

        // given test lobby
        testLobby.setLobbyId("test");
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);

        testGameplay.setCorrespondingLobbyID("test");
        testGameplay.setLobbyForGamePlay(testLobby);
        Mockito.when(gameSessionRepository.findByCorrespondingLobbyID(Mockito.any())).thenReturn(testGameplay);

    }

    /** Every player's point are initialized to 0 by default when they are created and added to re repository. */
    @Test
    public void inital_points(){
        int expectedResult = 0;
        assertEquals(expectedResult, testUser.getPoints());
    }

    @Test
    public void add_points(){
        // only 1 test user in repo, so 1 point for guessing correctly, 1 point for someone guessed my picture correctly
        int expectedResult = 2;

        // mock a correct guess
        String testGuess = "";
        testGuess += coordinateNames[testCoordinates];
        testGuess += testUsername;
        testGuess += "-";

        testUser.setGuesses(testGuess);
        String placeholder = gameService.handleGuesses("test", testUser); // needs to be called but result is not needed in this test

        assertEquals(expectedResult, testUser.getPoints());

    }

}

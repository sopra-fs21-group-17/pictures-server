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
public class GuessingLogicUnitTest {

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

    @Test
    public void correct_guess(){

        String expectedResult = "";
        String testGuess = "";

        // manually create guessing information

        // create test guess
        testGuess += coordinateNames[testCoordinates];
        testGuess += testUsername;
        testGuess += "-";

        // create expected result
        expectedResult += "y";
        expectedResult += testUsername;
        expectedResult += "-";

        testUser.setGuesses(testGuess);
        assertEquals(expectedResult, gameService.handleGuesses("test", testUser));

    }

    @Test
    public void wrong_guess(){

        String expectedResult = "";
        String testGuess = "";

        // manually create guessing information

        // create test guess
        testGuess += "A1"; // wrong coordinates
        testGuess += testUsername;
        testGuess += "-";

        // create expected result
        expectedResult += "n";
        expectedResult += testUsername;
        expectedResult += "-";

        testUser.setGuesses(testGuess);
        assertEquals(expectedResult, gameService.handleGuesses("test", testUser));

    }

}

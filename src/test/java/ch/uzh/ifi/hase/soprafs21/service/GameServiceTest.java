////package ch.uzh.ifi.hase.soprafs21.service;
////
////import ch.uzh.ifi.hase.soprafs21.entity.*;
////import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
////import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
////import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
////import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
////
////import org.aspectj.lang.annotation.Before;
////import org.junit.jupiter.api.BeforeAll;
////import org.junit.jupiter.api.TestInstance;
////import org.mockito.*;
////
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////
////import java.util.*;
////
////import static org.junit.jupiter.api.Assertions.*;
////
////@TestInstance(TestInstance.Lifecycle.PER_CLASS)
////public class GameServiceTest {
////    @Mock
////    private PicturesRepository picturesRepository;
////    @Mock
////    private GameSessionRepository gameSessionRepository;
////    @Mock // mocks an Object
////    private UserRepository userRepository;
////    @Mock
////    private LobbyRepository lobbyRepository;
////
////    @InjectMocks
////    private GameService gameService;
////
////    private User testUser = new User();
////
////    Picture mockPicture = new Picture();
////    private Lobby testLobby = new Lobby();
////    @Spy
////    private GamePlay testGameplay = new GamePlay();
////
////    Screenshot testScreenshot = new Screenshot();
////
////    @BeforeAll
////    // initializes this before every test case
////    public void setup(){
////        MockitoAnnotations.openMocks(this);
////
////        // given
////        testUser.setUsername("Test1");
////
////        Mockito.when(userRepository.findByUsername("Test1")).thenReturn(testUser);
////        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);
////
////        testLobby.setLobbyId("test");
////        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
////
////        testGameplay.setGameID(1L);
////        Mockito.when(testGameplay.getPictureWithCoordinates(Mockito.anyInt())).thenReturn(mockPicture);
////
////        testScreenshot.setURL("ScreenShotTest");
////        testScreenshot.setUserID(testUser.getId());
////
////
////            mockPicture.setPictureLink("mockURL_");
////            mockPicture.setId(1L);
////            Mockito.when(picturesRepository.findByid(Mockito.any())).thenReturn(mockPicture);
////    }
////
////    /**
////     * tests if selected pictures are saved to the Gameplay entity
////     */
////    @Test
////
////    public void testSelectPictures(){
////        assertNull(gameService.getListOfPictures());
////        gameService.initGame("test");
////        gameService.selectPictures();
////        Mockito.verify(picturesRepository,Mockito.times(16)).findByid(Mockito.any());
////        assertNotNull(gameService.getListOfPictures());
////    }
////    @Test
////    /**
////     * tests if list of pictures is returned from the Gameplay entity
////     */
////    public void testGetSelectedPictures(){
////        gameService.selectPictures();
////       Picture[] selected =  gameService.getListOfPictures();
////        assertEquals(selected.length,16);
////    }
////
////    /**
////     * is used to simulate getting a picture when the service is given a userID
////     */
////    @Test
////    public void testGetCorrespondingToUser()
////    {
////        gameService.setGamePlay(testGameplay);
////        gameService.initGame(testLobby.getLobbyId());
////        testUser.setAssignedCoordinates(1);
////        Picture picture = gameService.getCorrespondingToUser(testUser.getId());
////
////
////        assertEquals(picture.getId(),mockPicture.getId());
////        assertEquals(picture.getPictureLink(),mockPicture.getPictureLink());
////
////    }
////
////    /**
////     * tests before and after saving screenshot to user
////     */
////    @Test
////    public void testSaveScreenshot()
////    {
////        assertNull(testUser.getScreenshotURL());
////        gameService.saveScreenshot(testScreenshot,testUser.getUsername());
////        assertEquals(testUser.getScreenshotURL(), testScreenshot.getURL());
////
////
////    }
////
////    @Test
////    public void testInitGameHandlesCoordinatesHandlesSetsAssignment()
////    {
////        // initialize the list of testusers to simulate lobby content
////        Set<User> testUsers = new HashSet<>();
////
////        User testUser2 = new User();
////        User testUser3 = new User();
////        User testUser4 = new User();
////        User[] users = {testUser2,testUser3,testUser4};
////
////        for(int i = 0; i < 3; i++){
////
////            users[i].setUsername("Test" + (i+2));
////            users[i].setId((long) (i+2));
////            users[i].setLobbyId(testLobby.getLobbyId());
////            assertTrue(users[i].getAssignedCoordinates() == 0);
////            assertNull(users[i].getAssignedSet());
////            testUsers.add(users[i]);
////        }
////        testLobby.setUsersList(testUsers);
////
////       Set<User> testUserAfterCall =  gameService.initGame(testLobby.getLobbyId());
////       ArrayList<User> testUsersAfter = new ArrayList<>(testUserAfterCall);
////       for(int i = 0;i < 3; i++){
////           assertEquals(testUsersAfter.get(i).getUsername(),users[i].getUsername());
////           assertEquals(testUsersAfter.get(i).getId(),users[i].getId());
////           assertTrue(testUsersAfter.get(i).getAssignedCoordinates()>=0);
////           assertNotNull(testUsersAfter.get(i).getAssignedSet());
////       }
////
////
////
////    }
////
////    @Test
////    public void testHandleGuesses()
////    {
////        //TODO fragen
////        assertTrue(false);
////    }
////
////
////
////
////
////
////
////
////
////}
//=======
//>>>>>>> Stashed changes
//package ch.uzh.ifi.hase.soprafs21.service;
//
//import ch.uzh.ifi.hase.soprafs21.entity.*;
//import ch.uzh.ifi.hase.soprafs21.repository.GameSessionRepository;
//import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
//import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
//import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
//
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class GameServiceTest {
//    @Mock
//    private PicturesRepository picturesRepository;
//    @Mock
//    private GameSessionRepository gameSessionRepository;
//    @Mock // mocks an Object
//    private UserRepository userRepository;
//    @Mock
//    private LobbyRepository lobbyRepository;
//
//    @InjectMocks
//    private GameService gameService;
//
//    private User testUser = new User();
//
//    Picture mockPicture = new Picture();
//    private Lobby testLobby = new Lobby();
//    @Spy
//    private GamePlay testGameplay = new GamePlay();
//
//    Screenshot testScreenshot = new Screenshot();
//
//    @BeforeAll
//    // initializes this before every test case
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//
//        // given
//        testUser.setUsername("Test1");
//
//        Mockito.when(userRepository.findByUsername("Test1")).thenReturn(testUser);
//        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);
//
//        testLobby.setLobbyId("test");
//        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
//
//        testGameplay.setGameID(1L);
//        Mockito.when(testGameplay.getPictureWithCoordinates(Mockito.anyInt())).thenReturn(mockPicture);
//
//        testScreenshot.setURL("ScreenShotTest");
//        testScreenshot.setUserID(testUser.getId());
//
//
//            mockPicture.setPictureLink("mockURL_");
//            mockPicture.setId(1L);
//            Mockito.when(picturesRepository.findByid(Mockito.any())).thenReturn(mockPicture);
//    }
//
//    /**
//     * tests if selected pictures are saved to the Gameplay entity
//     */
//    @Test
//
//    public void testSelectPictures(){
//        assertNull(gameService.getListOfPictures());
//        gameService.initGame("test");
//        gameService.selectPictures();
//        Mockito.verify(picturesRepository,Mockito.times(16)).findByid(Mockito.any());
//        assertNotNull(gameService.getListOfPictures());
//    }
//    @Test
//    /**
//     * tests if list of pictures is returned from the Gameplay entity
//     */
//    public void testGetSelectedPictures(){
//        gameService.selectPictures();
//       Picture[] selected =  gameService.getListOfPictures();
//        assertEquals(selected.length,16);
//    }
//
//    /**
//     * is used to simulate getting a picture when the service is given a userID
//     */
//    @Test
//    public void testGetCorrespondingToUser()
//    {
//        gameService.setGamePlay(testGameplay);
//        gameService.initGame(testLobby.getLobbyId());
//        testUser.setAssignedCoordinates(1);
//        Picture picture = gameService.getCorrespondingToUser(testUser.getId());
//
//
//        assertEquals(picture.getId(),mockPicture.getId());
//        assertEquals(picture.getPictureLink(),mockPicture.getPictureLink());
//
//    }
//
//    /**
//     * tests before and after saving screenshot to user
//     */
//    @Test
//    public void testSaveScreenshot()
//    {
//        assertNull(testUser.getScreenshotURL());
//        gameService.saveScreenshot(testScreenshot,testUser.getUsername());
//        assertEquals(testUser.getScreenshotURL(), testScreenshot.getURL());
//
//
//    }
//
//    //Matches style of integration test more
//    @Test
//    public void testInitGameHandlesCoordinatesHandlesSetsAssignment()
//    {
//        // initialize the list of testusers to simulate lobby content
//        Set<User> testUsers = new HashSet<>();
//
//        User testUser2 = new User();
//        User testUser3 = new User();
//        User testUser4 = new User();
//        User[] users = {testUser2,testUser3,testUser4};
//
//        for(int i = 0; i < 3; i++){
//
//            users[i].setUsername("Test" + (i+2));
//            users[i].setId((long) (i+2));
//            users[i].setLobbyId(testLobby.getLobbyId());
//            assertTrue(users[i].getAssignedCoordinates() == 0);
//            assertNull(users[i].getAssignedSet());
//            testUsers.add(users[i]);
//        }
//        testLobby.setUsersList(testUsers);
//
//       Set<User> testUserAfterCall =  gameService.initGame(testLobby.getLobbyId());
//       ArrayList<User> testUsersAfter = new ArrayList<>(testUserAfterCall);
//       for(int i = 0;i < 3; i++){
//           assertTrue(testUsersAfter.get(i).getAssignedCoordinates()>=0);
//           assertNotNull(testUsersAfter.get(i).getAssignedSet());
//       }
//
//
//
//    }
//
//    @Test
//    public void testHandleGuesses()
//    {
//        //TODO fragen
//        assertTrue(false);
//    }
//
//
//
//
//
//
//
//
//
//}
//

package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        lobbyRepository.deleteAll();
    }

    @Test
    public void createLobby_validInputs_success() {
        // given
        assertNull(lobbyRepository.findByLobbyId("AbCd"));
        assertNull(userRepository.findByUsername("testUsername"));

        Lobby testLobby = new Lobby();
        testLobby.setLobbyId("AbCd");


        // when
        Lobby createdLobby = lobbyService.createLobby(testLobby);


        // then
        assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());

    }

//    @Test
//    public void createLobby_duplicateLobbyId_throwsException() {
//        assertNull(lobbyRepository.findByLobbyId("AbCd"));
//
//
//        Lobby testLobby = new Lobby();
//        testLobby.setLobbyId("AbCd");
//
//        Lobby createdLobby = lobbyService.createLobby(testLobby);
//
//
//        // attempt to create second user with same username
//        Lobby testLobby2 = new Lobby();
//
//
//
//        testLobby2.setLobbyId("AbCd");
//
//        // check that an error is thrown
//        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby2));
//    }
}

package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setLobbyId("AbCd");


        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);


    }

    @Test
    public void create_Lobby_validInputs_success() {
        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testLobby.getCreationTime(), createdLobby.getCreationTime());
        assertEquals(testLobby.getPlayersCount(), createdLobby.getPlayersCount());
        assertEquals(testLobby.getUsersList(), createdLobby.getUsersList());
        assertEquals(testLobby.getTimeDifference(), createdLobby.getTimeDifference());

    }

//    @Test
//    public void createLobby_duplicateLobby_throwsException() {
//        // given -> a first user has already been created
//        lobbyService.createLobby(testLobby);
//
//        // when -> setup additional mocks for UserRepository
//        Mockito.when(lobbyRepository.findByLobbyId(Mockito.any())).thenReturn(testLobby);
//
//
//        // then -> attempt to create second user with same user -> check that an error is thrown
//        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby));
//    }
}
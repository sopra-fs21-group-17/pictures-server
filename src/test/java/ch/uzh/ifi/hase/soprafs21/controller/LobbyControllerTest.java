package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;


import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LobbyController.class)
class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;


    @Test
    void createLobby_validInput_LobbyCreated() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());


        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyId("AbCd");

        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId())))
                .andExpect(jsonPath("$.creationTime", is(lobby.getCreationTime())));

    }


    @Test
    void givenUser_setLobbyId_whenLobbyFoundByLobbyId_addToLobby()throws Exception {
        //given
        User user = new User();
        user.setUsername("Username");
        user.setPassword("Password");
        user.setToken("efg");
        user.setId(1L);
        user.setLobbyId("AbCd");
        user.setIsReady(false);

        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        //lobby.setUsersList(lobby.getUsersList().add(user));

        UserPostDTO userInput = new UserPostDTO();
        userInput.setUsername("username");

        User convertedUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userInput);

        //this mocks the LobbyService -> we defined above what the LobbyService should return when addUserToLobby() is called
        doNothing().when(lobbyService).addUserToLobby(convertedUser, user.getLobbyId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/users/"+lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userInput));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

//    @Test
//    void givenUser_whenFoundByName_Remove_FromLobbyList() throws Exception {
//        User user = new User();
//        user.setName("Username");
//        user.setPassword("Password");
//        user.setToken("efg");
//        user.setId(1L);
//        user.setLobbyId("AbCd");
//        user.setIsReady(false);
//
//
//        Lobby lobby = new Lobby();
//        lobby.setLobbyId("AbCd");
//        lobby.setCreationTime(System.nanoTime());
//        lobby.setUsersList(new HashSet<>());
//
//        //this mocks the LobbyService -> we defined above what the LobbyService should return when addUserToLobby() is called
//        doNothing().when(lobbyService).removeUserFromLobby(user.getUsername(), user.getLobbyId());
//
//        // when/then -> do the request + validate the result
//        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+user.getUsername()+lobby.getLobbyId());
//
//
//        mockMvc.perform(putRequest)
//                .andExpect(status().isNoContent());
//    }

    @Test
    void findLobbyWithGivenString_thenCheckIfLobbyExists() throws Exception {

        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());

        //this mocks the LobbyService -> we defined above what the LobbyService should return when checkLobbyId() is called
        doNothing().when(lobbyService).checkLobbyId(lobby.getLobbyId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+lobby.getLobbyId()+"/users");


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    @Test
    void findLobbyWithGivenString_thenCalculateTimeDifference_updateTimeDifference() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        long currentTime = System.nanoTime();
        long timeDifference = lobby.getCreationTime() - currentTime;
        lobby.setTimeDifference(timeDifference);

        //this mocks the LobbyService -> we defined above what the LobbyService should return when updateCount() is called
        doNothing().when(lobbyService).updateCount(lobby.getLobbyId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/count/"+lobby.getLobbyId());


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }


    @Test
    void getsLobbyByGivenString_ChecksIfLobbyIsReady_updatesPlayerCount_updatesLobbyIsReady() throws Exception {
        //given
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        lobby.setTimeDifference(0.0);
        lobby.setLobbyReady(false);
        lobby.setPlayersCount(1);

        User user = new User();
        user.setUsername("Username");
        user.setPassword("Password");
        user.setToken("efg");
        user.setId(1L);
        user.setLobbyId("AbCd");
        user.setIsReady(false);

        given(lobbyService.checkReadyAndGetCount(Mockito.any())).willReturn(lobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/ready/"+user.getLobbyId()).contentType(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId())))
                .andExpect(jsonPath("$.creationTime", is(lobby.getCreationTime())))
                .andExpect(jsonPath("$.timeDifference", is(lobby.getTimeDifference())))
                .andExpect(jsonPath("$.lobbyReady", is(lobby.isLobbyReady())))
                .andExpect(jsonPath("$.playersCount", is(lobby.getPlayersCount())));


    }

//    @Test
//    void getsLobbyByGivenString_ChecksIfLobbyIsReadyBuildScreen_updatesLobbyIsReadyBuildScreen() throws Exception {
//        //given
//        Lobby lobby = new Lobby();
//        lobby.setLobbyId("AbCd");
//        lobby.setCreationTime(System.nanoTime());
//        lobby.setTimeDifference(0.0);
//        lobby.setLobbyReady(false);
//        lobby.setPlayersCount(1);
//        lobby.setLobbyReadyBuildScreen(false);
//
//        User user = new User();
//        user.setUsername("Username");
//        user.setPassword("Password");
//        user.setToken("efg");
//        user.setId(1L);
//        user.setLobbyId("AbCd");
//        user.setIsReady(false);
//        user.setReadyBuildScreen(false);
//
//        given(lobbyService.checkReadyAndGetCount(Mockito.any())).willReturn(lobby);
//
//        MockHttpServletRequestBuilder getRequest = get("/lobbies/buildScreens/ready/"+user.getLobbyId());
//
//        //then
//        mockMvc.perform(getRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId())))
//                .andExpect(jsonPath("$.creationTime", is(lobby.getCreationTime())))
//                .andExpect(jsonPath("$.timeDifference", is(lobby.getTimeDifference())))
//                .andExpect(jsonPath("$.lobbyReady", is(lobby.isLobbyReady())))
//                .andExpect(jsonPath("$.playersCount", is(lobby.getPlayersCount())))
//                .andExpect(jsonPath("$.lobbyReadyBuildScreen", is(lobby.isLobbyReadyBuildScreen())));
//    }

    @Test
    void findLobby_ByGivenID_setReadyBuildScreenTimeUp()throws Exception{
        //given
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        lobby.setTimeDifference(0.0);
        lobby.setLobbyReady(false);
        lobby.setPlayersCount(1);
        lobby.setLobbyReadyBuildScreen(false);

        //this mocks the LobbyService -> we defined above what the LobbyService should return when timeUpBuildScreen() is called
        doNothing().when(lobbyService).timeUpBuildScreen(lobby.getLobbyId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/buildScreens/ready/timers/"+lobby.getLobbyId());


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void findLobby_ByGivenID_setReadyBuildScreenStartScreen()throws Exception{
        //given
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        lobby.setTimeDifference(0.0);
        lobby.setLobbyReady(false);
        lobby.setPlayersCount(1);
        lobby.setLobbyReadyBuildScreen(false);

        //this mocks the LobbyService -> we defined above what the LobbyService should return when startBuildScreen() is called
        doNothing().when(lobbyService).startBuildScreen(lobby.getLobbyId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/buildScreens/ready/preparations/"+lobby.getLobbyId());


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void removeUserFromLobbyTest() throws Exception{
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        lobby.setTimeDifference(0.0);
        lobby.setLobbyReady(false);
        lobby.setPlayersCount(1);
        lobby.setLobbyReadyBuildScreen(false);

        User user = new User();
        user.setUsername("Username");
        user.setPassword("Password");
        user.setToken("efg");
        user.setId(1L);
        user.setLobbyId("AbCd");
        user.setIsReady(false);
        user.setReadyBuildScreen(false);

        doNothing().when(lobbyService).startBuildScreen(lobby.getLobbyId());
        MockHttpServletRequestBuilder putRequest = put("/lobby/"+ user.getUsername()+"/"+lobby.getLobbyId());

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());


    }

    @Test
    void checkIsLobbyReadyBuildScreenTest() throws Exception{
        Lobby lobby = new Lobby();
        lobby.setLobbyId("AbCd");
        lobby.setCreationTime(System.nanoTime());
        lobby.setTimeDifference(0.0);
        lobby.setLobbyReady(false);
        lobby.setPlayersCount(1);
        lobby.setLobbyReadyBuildScreen(false);

        doNothing().when(lobbyService).startBuildScreen(lobby.getLobbyId());
        given(lobbyService.checkReadyAndGetCountBuildScreen(lobby.getLobbyId())).willReturn(lobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/buildScreens/ready/"+lobby.getLobbyId());
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId())))
                .andExpect(jsonPath("$.lobbyReady", is(false)))
                .andExpect(jsonPath("$.playersCount", is(lobby.getPlayersCount())));

    }


    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
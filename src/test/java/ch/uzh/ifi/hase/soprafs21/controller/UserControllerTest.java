package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setPassword("Firstname");


        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())));



    }
    @Test
    public void givenUsers_whenGetUsersLobby_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setPassword("Firstname");
        user.setLobbyId("AbCd");


        List<User> allUsersInLobby = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsersInLobby(user.getLobbyId())).willReturn(allUsersInLobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/users/"+ user.getLobbyId()).contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())));



    }


    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setPassword("testPassword");




        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        user.setPassword("testPassword");

        given(userService.createUser(Mockito.any())).willReturn(user);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));


    }
    @Test
    public void updateUser_validInput_validUsername_updateUser_userUpdated() throws Exception{
        //given
        User user = new User();
        user.setId(1L);
        user.setUsername("firstname@lastname");
        user.setPassword("Firstname Lastname");
        user.setToken("1");
        user.setIsReady(false);


        UserPutDTO userInput = new UserPutDTO();
        userInput.setUsername("username");



        User convertedUser = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userInput);

        // this mocks the UserService -> we define above what the userService should return when updateUser() is called
        doNothing().when(userService).updateIsReady(user.getUsername(), convertedUser);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userInput));

        //then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenUser_whenFindByUserName_thenReturnUser() throws Exception{
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("firstname@lastname");
        user.setPassword("Firstname Lastname");
        user.setToken("1");



        // this mocks the UserService -> we define above what the userService should return when getUserByUserID() is called
        given(userService.getUser(user.getUsername())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getUsername()).contentType(MediaType.APPLICATION_JSON);

        //then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));


    }
    @Test
    public void returnUser_validInput_userReturned() throws Exception {
        // given
        UserPostDTO userLogin = new UserPostDTO();
        userLogin.setPassword("Firstname Lastname");
        userLogin.setUsername("firstname@lastname");

        User user = new User();
        user.setId(1l);
        user.setUsername("firstname@lastname");
        user.setPassword("Firstname Lastname");
        user.setToken("1");
        user.setIsReady(false);

        given(userService.getUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = get("/users/"+user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLogin));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.isReady", is(user.getIsReady())));
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
package ch.uzh.ifi.hase.soprafs21.controller;

import static org.junit.jupiter.api.Assertions.*;


import ch.uzh.ifi.hase.soprafs21.entity.BuildRoom;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.BuildRoomPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.BuildRoomService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BuildRoomController.class)
class BuildRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildRoomService buildRoomService;

    @Test
    void createBuildRoom_validInput_BuildRoomCreated() throws Exception{
        // given
        BuildRoom buildRoom = new BuildRoom();
        buildRoom.setRoomId("AbCd");
        buildRoom.setCreationTime(System.nanoTime());


        BuildRoomPostDTO buildRoomPostDTO = new BuildRoomPostDTO();
        buildRoomPostDTO.setRoomId("AbCd");

        given(buildRoomService.createRoom(Mockito.any())).willReturn(buildRoom);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/buildRooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(buildRoomPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId", is(buildRoom.getRoomId())))
                .andExpect(jsonPath("$.creationTime", is(buildRoom.getCreationTime())));

    }
    @Test
    void findBuildRoomWithGivenString_thenCalculateTimeDifference_updateTimeDifference() throws Exception {
        BuildRoom buildRoom = new BuildRoom();
        buildRoom.setRoomId("AbCd");
        buildRoom.setCreationTime(System.nanoTime());
        long currentTime = System.nanoTime();
        long timeDifference = buildRoom.getCreationTime() - currentTime;
        buildRoom.setTimeDifference(timeDifference);

        //this mocks the BuildRoomService -> we defined above what the BuildRoomService should return when updateCount() is called
        doNothing().when(buildRoomService).updateCount(buildRoom.getRoomId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/buildRooms/count/"+buildRoom.getRoomId());


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void  findBuildRoomWithGivenString_thenUpdateCreationTimeGuessing() throws Exception {

        BuildRoom buildRoom = new BuildRoom();
        buildRoom.setRoomId("AbCd");
        buildRoom.setCreationTimeGuessing(System.nanoTime());



        //this mocks the BuildRoomService -> we defined above what the BuildRoomService should return when setTimeGuessing() is called
        doNothing().when(buildRoomService).setTimeGuessing(buildRoom.getRoomId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/guessing/time/"+buildRoom.getRoomId());


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void findBuildRoomWithGivenString_thenCalculateTimeDifferenceGuessing_updateTimeDifferenceGuessing() throws Exception {
        BuildRoom buildRoom = new BuildRoom();
        buildRoom.setRoomId("AbCd");
        buildRoom.setCreationTimeGuessing(System.nanoTime());
        long currentTime = System.nanoTime();
        long timeDifference = buildRoom.getCreationTimeGuessing() - currentTime;
        buildRoom.setTimeDifferenceGuessing(timeDifference);

        //this mocks the BuildRoomService -> we defined above what the BuildRoomService should return when updateCountGuessing() is called
        doNothing().when(buildRoomService).updateCountGuessing(buildRoom.getRoomId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/guessing/count/"+buildRoom.getRoomId());


        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenRoom_whenFindByRoomId_thenReturnBuildRoom() throws Exception{
        // given
        BuildRoom buildRoom = new BuildRoom();
        buildRoom.setRoomId("aBcD");
        buildRoom.setCreationTime(System.nanoTime());
        buildRoom.setTimeDifference(1.0);
        buildRoom.setCreationTimeGuessing(System.nanoTime());
        buildRoom.setTimeDifferenceGuessing(1.0);



        // this mocks the BuildRoomService -> we define above what the buildRoomService should return when getBuildRoom() is called
        given(buildRoomService.getBuildRoom(buildRoom.getRoomId())).willReturn(buildRoom);

        // when
        MockHttpServletRequestBuilder getRequest = get("/buildRooms/"+buildRoom.getRoomId());

        //then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId", is(buildRoom.getRoomId())))
                .andExpect(jsonPath("$.creationTime", is(buildRoom.getCreationTime())))
                .andExpect(jsonPath("$.timeDifference", is(buildRoom.getTimeDifference())))
                .andExpect(jsonPath("$.creationTimeGuessing", is(buildRoom.getCreationTimeGuessing())))
                .andExpect(jsonPath("$.timeDifferenceGuessing", is(buildRoom.getTimeDifferenceGuessing())));

    }

    @Test
    public void resetRoomTest() throws Exception{
        BuildRoom buildRoom = new BuildRoom();
        buildRoom.setRoomId("aBcD");
        buildRoom.setCreationTime(System.nanoTime());
        buildRoom.setTimeDifference(1.0);
        buildRoom.setCreationTimeGuessing(System.nanoTime());
        buildRoom.setTimeDifferenceGuessing(1.0);

        given(buildRoomService.getBuildRoom(buildRoom.getRoomId())).willReturn(buildRoom);

        MockHttpServletRequestBuilder putRequest = put("/buildRooms/rounds/count/"+buildRoom.getRoomId());
        mockMvc.perform(putRequest).andExpect(status().isNoContent());
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
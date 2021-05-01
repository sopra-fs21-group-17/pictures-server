package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;
import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void testReturnOfPictureGetDTOsList() throws Exception {
        //given
        // what the gameService should return when getAllPictures() is called
        Picture testPicture = new Picture();
        testPicture.setPictureLink("testLink");

        // this mocks the GameService

        List<Picture> allPictures = new ArrayList<>();
        allPictures.add(testPicture);

        given(gameService.getListOfPictures()).willReturn(allPictures);

        //when incoming get request
        MockHttpServletRequestBuilder getRequest = get("/pictures").contentType(MediaType.APPLICATION_JSON);

        //then perform the request
        mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].pictureLink", is(testPicture.getPictureLink())));
    }
//
   @Test
    public void testReturnPictureCorrespondingToUser() throws Exception{
         //given
       Picture testPicture = new Picture();
       testPicture.setPictureLink("testLink");;


//       GamePlay testGame = new GamePlay();
//       testGame.setGameID(1L);
//       testGame.addPicture(testPicture,1);

       User testUser = new User();
       testUser.setUsername("TestUser");
       testUser.setId(1L);
       testUser.setAssignedCoordinates(1);
       testUser.setToken("token");



       given(gameService.getCorrespondingToUser(testUser.getId())).willReturn(testPicture);
       //when incoming request
       UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(testUser);

       MockHttpServletRequestBuilder getRequest = get("/picture").content(asJsonString(userGetDTO)).contentType(MediaType.APPLICATION_JSON);

       //then perform the request
       mockMvc.perform(getRequest).andExpect(status().isOk())
               .andExpect(jsonPath("$.pictureLink", is(testPicture.getPictureLink())));


       //mocking gameService

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

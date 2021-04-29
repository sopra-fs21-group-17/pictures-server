package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

//    @Test
//    public void testReturnOfPictureGetDTOsList() throws Exception {
//        //given
//        // what the gameService should return when getAllPictures() is called
//        Picture testPicture = new Picture();
//        testPicture.setPictureLink("testLink");
//
//        // this mocks the GameService
//
//        List<Picture> allPictures = new ArrayList<>();
//        allPictures.add(testPicture);
//
//        given(gameService.getListOfPictures()).willReturn(allPictures);
//
//        //when incoming get request
//        MockHttpServletRequestBuilder getRequest = get("/picture").contentType(MediaType.APPLICATION_JSON);
//
//        //then perform the request
//        mockMvc.perform(getRequest).andExpect(status().isOk())
//        .andExpect(jsonPath("$", hasSize(1)))
//        .andExpect(jsonPath("$[0].pictureLink", is(testPicture.getPictureLink())));
//    }
//
//}

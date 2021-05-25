package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.BuildRoom;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.BuildRoomRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class BuildRoomServiceIntegrationTest {
    @Qualifier("buildRoomRepository")
    @Autowired
    private BuildRoomRepository buildRoomRepository;

    @Autowired
    private BuildRoomService buildRoomService;
    @BeforeEach
    public void setup() {
            buildRoomRepository.deleteAll();
        }

    @Test
    public void createBuildRoom_validInputs_success() throws ParseException {
        // given
        assertNull(buildRoomRepository.findByRoomId("aBcD"));

        BuildRoom testRoom = new BuildRoom();
        testRoom.setRoomId("aBcD");


        // when
        BuildRoom createdRoom = buildRoomService.createRoom(testRoom);

        // then
        assertEquals(testRoom.getRoomId(), createdRoom.getRoomId());


    }

//    @Test
//    public void createUser_duplicateUsername_throwsException() throws ParseException {
//        assertNull(buildRoomRepository.findByRoomId("aBcD"));
//
//        BuildRoom testRoom = new BuildRoom();
//        testRoom.setRoomId("aBcD");
//        BuildRoom createdRoom = buildRoomService.createRoom(testRoom);
//
//        // attempt to create second BuildRoom with same BuildRoomId
//        BuildRoom testRoom2 = new BuildRoom();
//
//        testRoom2.setRoomId("aBcD");
//
//
//
//        // check that an error is thrown
//        assertThrows(ResponseStatusException.class, () -> buildRoomService.createRoom(testRoom2));
//
//        }

}



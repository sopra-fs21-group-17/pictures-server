package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.BuildRoom;
import ch.uzh.ifi.hase.soprafs21.repository.BuildRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class BuildRoomServiceTest {
    @Mock
    private BuildRoomRepository buildRoomRepository;

    @InjectMocks
    private BuildRoomService buildRoomService;

    private BuildRoom testBuildRoom;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testBuildRoom = new BuildRoom();
        testBuildRoom.setRoomId("AbCd");


        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Mockito.when(buildRoomRepository.save(Mockito.any())).thenReturn(testBuildRoom);


    }

    @Test
    public void create_BuildRoom_validInputs_success() {
        // when -> any object is being save in the buildRoomRepository -> return the dummy testBuildRoom
        BuildRoom createdBuildRoom = buildRoomService.createRoom(testBuildRoom);

        // then
        Mockito.verify(buildRoomRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testBuildRoom.getRoomId(), createdBuildRoom.getRoomId());
        assertEquals(testBuildRoom.getCreationTime(), createdBuildRoom.getCreationTime());
        assertEquals(testBuildRoom.getTimeDifference(), createdBuildRoom.getTimeDifference());
        assertEquals(testBuildRoom.getCreationTimeGuessing(), createdBuildRoom.getCreationTimeGuessing());
        assertEquals(testBuildRoom.getTimeDifferenceGuessing(), createdBuildRoom.getTimeDifferenceGuessing());

    }

//    @Test
//    public void createBuildRoom_duplicateBuildRoom_throwsException() {
//        // given -> a first buildRoom has already been created
//        buildRoomService.createRoom(testBuildRoom);
//
//        // when -> setup additional mocks for BuildRoomRepository
//        Mockito.when(BuildRoomRepository.findByRoomId(Mockito.any())).thenReturn(testBuildRoom);
//
//
//        // then -> attempt to create second BuildRoom with same BuildRoom -> check that an error is thrown
//        assertThrows(ResponseStatusException.class, () -> buildRoomService.createRoom(testBuildRoom));
//    }

}
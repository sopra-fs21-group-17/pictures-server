package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.BuildRoom;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.BuildRoomGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.BuildRoomPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.BuildRoomService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BuildRoomController {

    private final BuildRoomService buildRoomService;

    BuildRoomController(BuildRoomService buildRoomService) {
        this.buildRoomService = buildRoomService;
    }

    @PostMapping("/buildRooms")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BuildRoomGetDTO createBuildRoom(@RequestBody BuildRoomPostDTO buildRoomPostDTO) {

        BuildRoom buildRoomInput = DTOMapper.INSTANCE.convertBuildRoomPostDTOtoEntity(buildRoomPostDTO);

        //creates a new BuildRoom
        BuildRoom createdRoom = buildRoomService.createRoom(buildRoomInput);

        //returns the createdRoom to the API
        return DTOMapper.INSTANCE.convertEntityToBuildRoomGetDTO(createdRoom);
    }

    @PutMapping("/buildRooms/count/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateCount(@PathVariable String roomId)  {

        //updates the BuildRoom count
        buildRoomService.updateCount(roomId);
    }

    @PutMapping("/guessing/time/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void setGuessingTime(@PathVariable String roomId)  {

        //sets the creationTime of Guessing
        buildRoomService.setTimeGuessing(roomId);
    }

    @PutMapping("/guessing/count/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateCountGuessing(@PathVariable String roomId)  {

        //updates the Guessing count
        buildRoomService.updateCountGuessing(roomId);
    }

    @PutMapping("/buildRooms/rounds/count/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void resetRoom(@PathVariable String roomId){

        //resets the BuildRoom count
        buildRoomService.resetRoom(roomId);
    }

    @GetMapping("/buildRooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BuildRoomGetDTO getBuildRoom(@PathVariable String roomId){

        BuildRoom buildRoom = buildRoomService.getBuildRoom(roomId);

        return DTOMapper.INSTANCE.convertEntityToBuildRoomGetDTO(buildRoom);
    }


}

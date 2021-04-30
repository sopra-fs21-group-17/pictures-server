package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO){

        Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        //creates a new lobby
        Lobby createdLobby = lobbyService.createLobby(lobbyInput);

        //returns the createdLobby to the API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobbies/users/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addUserToLobby(@RequestBody UserPostDTO userPostDTO, @PathVariable String lobbyId){

        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        //posts user to lobbyArray

        lobbyService.addUserToLobby(userInput, lobbyId);
    }

    @PutMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void checkLobbyById(@PathVariable String lobbyId){

        //checks if the lobby Id is correct
        lobbyService.checkLobbyId(lobbyId);
    }
    @PutMapping("/lobbies/count/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateCount(@PathVariable String lobbyId)  {

        //updates the lobby count
        lobbyService.updateCount(lobbyId);
    }
    @GetMapping("/lobbies/ready/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO checkIsLobbyReady(@PathVariable String lobbyId) {

        //gets the current Lobby
        Lobby lobby = lobbyService.checkReadyAndGetCount(lobbyId);

        // convert lobby to the API representation

        //returns lobby
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }





}
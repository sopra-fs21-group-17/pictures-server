package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }
//    @GetMapping("/lobbies/{lobbyId}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public List<UserGetDTO> getAllUsers(@PathVariable String lobbyId) {
//        // fetch all users in the internal representation
//        List<User> users = lobbyService.getUsers(lobbyId);
//        List<UserGetDTO> userGetDTOs = new ArrayList<>();
//
//        // convert each user to the API representation
//        for (User user : users) {
//            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
//        }
//        //returns list
//        return userGetDTOs;
//    }
//    @GetMapping("/lobbies")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public List<LobbyGetDTO> getAllLobbies() {
//        // fetch all lobbies in the internal representation
//        List<Lobby> lobbies = lobbyService.getLobbies();
//        List<LobbyGetDTO> lobbyGetDTO = new ArrayList<>();
//
//        // convert each user to the API representation
//        for (Lobby lobby : lobbies) {
//        lobbyGetDTO.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
//     }
//        //returns list
//        return lobbyGetDTO;
//}

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO){

        Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        Lobby createdLobby = lobbyService.createLobby(lobbyInput);

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobbies/users/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    //post user to lobbyArray
    public void addUserToLobby(@RequestBody UserPostDTO userPostDTO, @PathVariable String lobbyId){

        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        lobbyService.addUserToLobby(userInput, lobbyId);
    }
    @PutMapping("/lobbies/{lobbyId}/users/")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    //post user to lobbyArray
    public void checkLobbyById(@PathVariable String lobbyId){

        lobbyService.checkLobbyId(lobbyId);
    }


    @GetMapping("/lobbies/users/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers(@PathVariable String lobbyId) {
        // fetch all users in the internal representation
        List<User> users = lobbyService.getUsersInLobby(lobbyId);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        //returns list
        return userGetDTOs;
    }

}

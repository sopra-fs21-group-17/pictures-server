package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.game.Guesses;

import java.util.List;
import java.util.Set;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

public class UserPutDTO {

    private String username;
    private String guesses;
    private String lobbyId;
    // guess1: {"adam":null}, guess2: {"eva": null}, guess3: null, guess4: null
    // [{"adam":null}, {"adam":null}]

    private Long id;
    private boolean isReady;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isReady() { return isReady; }

    public void setIsReady(boolean isReady) { this.isReady = isReady;}

    public Long getId() {
        return id;
    }

    public String getGuesses() {
        return guesses;
    }

    public void setGuesses(String guesses) {
        this.guesses = guesses;
    }

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }


}

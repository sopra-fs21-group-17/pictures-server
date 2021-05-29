package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

public class UserPutDTO {

    private String username;
    private String guesses;
    private String lobbyId;
    private Long id;
    private boolean isReady;
    private boolean isReadyBuildScreen;


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

    public void setId(Long id) { this.id = id; }

    public String getGuesses() {
        return guesses;
    }

    public void setGuesses(String guesses) {
        this.guesses = guesses;
    }

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public boolean isReadyBuildScreen() {
        return isReadyBuildScreen;
    }

    public void setReadyBuildScreen(boolean readyBuildScreen) {
        isReadyBuildScreen = readyBuildScreen;
    }


}

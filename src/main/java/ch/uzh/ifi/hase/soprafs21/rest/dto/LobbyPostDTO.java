package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;
import java.util.Map;

public class LobbyPostDTO {

    private String lobbyId;

    private int count;

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

}

package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.*;

public class LobbyPutDTO {

    private String lobbyId;

    private boolean lobbyReady;

    private int playersCount;

    private List<User> userList = new ArrayList<User>();

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public List<User> getUserList() { return userList; }

    public void setUserList(List<User> userList) { this.userList = userList; }

    public boolean isLobbyReady() { return lobbyReady; }

    public void setLobbyReady(boolean lobbyReady) { this.lobbyReady = lobbyReady; }

    public int getPlayersCount() { return playersCount; }

    public void setPlayersCount(int playersCount) { this.playersCount = playersCount; }
}

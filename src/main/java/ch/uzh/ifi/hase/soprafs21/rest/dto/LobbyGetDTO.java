package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.*;

public class LobbyGetDTO {

    private String lobbyId;

    private long creationTime;

    private double timeDifference;

    private boolean lobbyReady;

    private int playersCount;

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public long getCreationTime() { return creationTime; }

    public void setCreationTime(long creationTime) { this.creationTime = creationTime; }

    public double getTimeDifference() { return timeDifference; }

    public void setTimeDifference(double timeDifference) { this.timeDifference = timeDifference; }

    public boolean isLobbyReady() { return lobbyReady; }

    public void setLobbyReady(boolean lobbyReady) { this.lobbyReady = lobbyReady; }

    public int getPlayersCount() { return playersCount; }

    public void setPlayersCount(int playersCount) { this.playersCount = playersCount; }
}

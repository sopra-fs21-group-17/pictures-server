package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.*;

public class LobbyPutDTO {

    private String lobbyId;

    private boolean lobbyReady;

    private long creationTime;

    private double timeDifference;

    private int playersCount;


    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }


    public boolean isLobbyReady() { return lobbyReady; }

    public void setLobbyReady(boolean lobbyReady) { this.lobbyReady = lobbyReady; }

    public long getCreationTime() { return creationTime; }

    public void setCreationTime(long creationTime) { this.creationTime = creationTime; }

    public double getTimeDifference() { return timeDifference; }

    public void setTimeDifference(double timeDifference) { this.timeDifference = timeDifference; }

    public int getPlayersCount() { return playersCount; }

    public void setPlayersCount(int playersCount) { this.playersCount = playersCount; }
}

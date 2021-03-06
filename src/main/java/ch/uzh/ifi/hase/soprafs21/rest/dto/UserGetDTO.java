package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

import java.util.Date;
import java.util.Dictionary;

public class UserGetDTO {

    private Long id;
    private String username;
    private int assignedCoordinates;
    private String assignedSet;
    private String correctedGuesses;
    private String password;
    private String model;
    private int totalScore;
    private boolean isReady;
    private String lobbyId;
    private int points;
    private Boolean doneGuessing;
    private boolean isReadyBuildScreen;

    private UserStatus status;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }
    
    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public int getTotalScore() { return totalScore; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public boolean getIsReady() { return isReady; }

    public void setIsReady(boolean isReady) { this.isReady = isReady;}

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public int getAssignedCoordinates() {
        return assignedCoordinates;
    }

    public void setAssignedCoordinates(int assignedCoordinates) {
        this.assignedCoordinates = assignedCoordinates;
    }

    public String getAssignedSet() {
        return assignedSet;
    }

    public void setAssignedSet(String assignedSet) {
        this.assignedSet = assignedSet;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getCorrectedGuesses() {
        return correctedGuesses;
    }

    public void setCorrectedGuesses(String correctedGuesses) {
        this.correctedGuesses = correctedGuesses;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Boolean getDoneGuessing() {
        return doneGuessing;
    }

    public void setDoneGuessing(Boolean doneGuessing) {
        this.doneGuessing = doneGuessing;
    }

    public boolean isReadyBuildScreen() {
        return isReadyBuildScreen;
    }

    public void setReadyBuildScreen(boolean readyBuildScreen) {
        isReadyBuildScreen = readyBuildScreen;
    }
}

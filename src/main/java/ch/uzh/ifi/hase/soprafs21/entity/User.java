package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Embeddable
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    //@Column // for testing
    private String username;

    @Column(nullable = false)
    //@Column // for testing
    private String password;

    //@Column(nullable = false, unique = true)
    @Column // for testing
    private String token;

    @Column
    private String model;

    @Column
    private int totalScore;

    @Column
    private Boolean isReady;

    //@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column
    private String lobbyId;

    @Column
    private UserStatus status;

    @Column
    private String assignedSet; // nr corresponding to array with set names

    @Column
    private int assignedCoordinates; // mapping style: A1=0,A2=1...D5=15

    @Column
    private String correctedGuesses;

    @Column
    private String guesses;

    @Column( length = 1000000000) // max nr of possible chars
    private String screenshotURL;

    @Column
    private int points;

    @Column
    private boolean doneGuessing;

    @Column
    private boolean isReadyBuildScreen;

    @Column
    private int setIndex;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getAssignedSet() {
        return assignedSet;
    }

    public void setAssignedSet(String assignedSet) {
        this.assignedSet = assignedSet;
    }

    public int getAssignedCoordinates() {
        return assignedCoordinates;
    }

    public void setAssignedCoordinates(int assignedCoordinates) {
        this.assignedCoordinates = assignedCoordinates;
    }

    public String getCorrectedGuesses() {
        return correctedGuesses;
    }

    public void setCorrectedGuesses(String correctedGuesses) {
        this.correctedGuesses = correctedGuesses;
    }

    public void setPassword(String password) { this.password = password; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public int getTotalScore() { return totalScore; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public Boolean getIsReady() { return isReady; }

    public void setIsReady(Boolean isReady) { this.isReady = isReady; }

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public UserStatus getStatus() {
        return status;
    }

    public String getScreenshotURL() {
        return screenshotURL;
    }

    public void setScreenshotURL(String screenshotURL) {
        this.screenshotURL = screenshotURL;
    }

    public String getGuesses() {
        return guesses;
    }

    public void setGuesses(String guesses) {
        this.guesses = guesses;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean getDoneGuessing() {
        return doneGuessing;
    }

    public void setDoneGuessing(boolean doneGuessing) {
        this.doneGuessing = doneGuessing;
    }

    public boolean isReadyBuildScreen() {
        return isReadyBuildScreen;
    }

    public void setReadyBuildScreen(boolean readyBuildScreen) {
        isReadyBuildScreen = readyBuildScreen;
    }

    public int getSetIndex() { return setIndex; }

    public void setSetIndex(int setIndex) {
        this.setIndex = setIndex;
    }
}


package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    //@Column(nullable = false, unique = true)
    @Column(unique = true) // commented out for testing
    private String name;

    //@Column(nullable = false, unique = true)
    @Column // for testing
    private String username;

    //@Column(nullable = false)
    @Column // for testing
    private String password;

    @Column
    private String birthdate;

    //@Column(nullable = false, unique = true)
    @Column // for testing
    private String token;

    @Column
    private String model;

    @Column
    private String guess;

    @Column
    private int totalScore;

    @Column
    private Boolean isReady;

    @Column
    private UserStatus status;

    @Column
    private String assignedSet; // nr corresponding to array with set names

    @Column
    private int assignedCoordinates; // mapping style: A1=0,A2=1...D5=15

    @Column
    private String correctedGuess; // TODO change to array sth...

    @Column
    private String guesses; // TODO change to array sth...

    @Column
    private String screenshotURL;

    public User() {
    }

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
        return correctedGuess;
    }

    public void setCorrectedGuesses(String correctedGuess) {
        this.correctedGuess = correctedGuess;
    }

    public String getGuesses() {
        return guesses;
    }

    public void setGuesses(String guesses) {
        this.guesses = guesses;
    }

    public void setPassword(String password) { this.password = password; }

    public String getBirthdate() { return birthdate; }

    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getGuess() { return guess; }

    public void setGuess(String guess) { this.guess = guess; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public int getTotalScore() { return totalScore; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public Boolean getIsReady() { return isReady; }

    public void setIsReady(Boolean isReady) { this.isReady = isReady; }

    public UserStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenshotURL() {
        return screenshotURL;
    }

    public void setScreenshotURL(String screenshotURL) {
        this.screenshotURL = screenshotURL;
    }
}

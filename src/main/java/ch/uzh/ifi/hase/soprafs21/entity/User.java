package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UserStatus status;

    @Column
    private int assignedCoordinates; // A1 = 0, A2 = 1, D4 = 15 ...

    // this attribute saves the assigned set of each user
    @Column
    private String assignedSet;

    @Column
    private String correctGuesses; // TODO store correctGuesses for each round like this guesses = ["y", "n", "n" ...] or similar...

    @ElementCollection
    @CollectionTable
    private ArrayList<ArrayList<String>> guesses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getStatus() {
        return status;
    }

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

    public String getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(String correctGuesses) {
        this.correctGuesses = correctGuesses;
    }


    public ArrayList<ArrayList<String>> getGuesses() {
        return guesses;
    }

    public void setGuesses(ArrayList<ArrayList<String>> guesses) {
        this.guesses = guesses;
    }
}

package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Dictionary;

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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private Date birthdate;

    @Column(nullable = false, unique = true)
    private String token;

    @Column
    private String guess;

    @Column
    private String model;

    @Column
    private int totalScore;

    @Column
    private Boolean isReady;



    //@Column(nullable = false)
    //private UserStatus status;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Date getBirthdate() { return birthdate; }

    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGuess() { return guess; }

    public void setGuess(String guess) { this.guess = guess; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public int getTotalScore() { return totalScore; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public Boolean getIsReady() { return isReady; }

    public void setIsReady(Boolean isReady) { this.isReady = isReady; }

//    public UserStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(UserStatus status) {
//        this.status = status;
//    }
}

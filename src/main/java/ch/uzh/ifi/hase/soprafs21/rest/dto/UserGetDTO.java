package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.Date;
import java.util.Dictionary;

public class UserGetDTO {

    //private Long id;
    private String username;
    private String password;
    private Date birthdate;
    private String guess;
    private String model;
    private int totalScore;
    private boolean isReady;
    //private UserStatus status;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

    public Date getBirthdate() { return birthdate; }

    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }

    public String getGuess() { return guess; }

    public void setGuess(String guess) { this.guess = guess; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public int getTotalScore() { return totalScore; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public boolean getIsReady() { return isReady; }

    public void setIsReady(boolean isReady) { this.isReady = isReady;}

}

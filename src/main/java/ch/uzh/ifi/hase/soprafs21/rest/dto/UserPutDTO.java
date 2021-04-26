package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.Map;

package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class UserPutDTO {
    private String username;
    private String[] guesses; // guess1: {"adam":null}, guess2: {"eva": null}, guess3: null, guess4: null
    // [{"adam":null}, {"adam":null}]

    private String username;

    private boolean isReady;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isReady() { return isReady; }

    public void setIsReady(boolean isReady) { this.isReady = isReady;}
}

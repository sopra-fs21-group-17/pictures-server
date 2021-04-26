package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class UserPutDTO {

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

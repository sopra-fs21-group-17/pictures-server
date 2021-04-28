package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class ScreenshotPutDTO {
    private String URL;
    private Long userID;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}

package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * used to store the generated Screenshots as entities for the game
 */

@Embeddable
@Entity
@Table(name = "SCREENSHOT")
public class Screenshot implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String URL;

    @Column
    private Long userID;


    public Screenshot(){}

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

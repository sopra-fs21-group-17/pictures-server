package ch.uzh.ifi.hase.soprafs21.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "LOBBY")
public class Lobby {

    @Id
    private String lobbyId;

//    @Column
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "lobby")
//    private List<User> userList = new ArrayList<User>();

    @Column
    private long creationTime;

    @Column
    private double timeDifference;

    @Column int playersCount;

    @Column
    private boolean lobbyReady;

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }



    public long getCreationTime() { return creationTime; }

    public void setCreationTime(long creationTime) { this.creationTime = creationTime; }

    public double getTimeDifference() { return timeDifference; }

    public void setTimeDifference(double timeDifference) { this.timeDifference = timeDifference; }

    public boolean isLobbyReady() { return lobbyReady; }

    public void setLobbyReady(boolean lobbyReady) { this.lobbyReady = lobbyReady; }

    public int getPlayersCount() { return playersCount; }

    public void setPlayersCount(int playersCount) { this.playersCount = playersCount; }
}



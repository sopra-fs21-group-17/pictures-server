package ch.uzh.ifi.hase.soprafs21.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "LOBBY")
public class Lobby {

    @Id
    private String lobbyId;

    @Column
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "lobby")
    private List<User> userList = new ArrayList<User>();

    @Column
    private int count;

    @Column int playersCount;

    @Column
    private boolean lobbyReady;

    public String getLobbyId() { return lobbyId; }

    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public List<User> getUserList() { return userList; }

    public void setUserList(List<User> userList) { this.userList = userList; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public boolean isLobbyReady() { return lobbyReady; }

    public void setLobbyReady(boolean lobbyReady) { this.lobbyReady = lobbyReady; }

    public int getPlayersCount() { return playersCount; }

    public void setPlayersCount(int playersCount) { this.playersCount = playersCount; }
}

package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


//TODO discuss implementation for multiple instances of a game

/**
 * GamePlay will be used to handle incoming changes to the game from the client like UserGuesses
 * Maybe will need a Repository for the games which will change depending if the games are still active or not
 */


@Entity
@Table(name = "GAME")
public class GamePlay implements Serializable {

    public GamePlay() {
    }

    private static final long serialVersionUID = 1L;

    // TODO see if still needed and remove otherwise


    @Id
    private String correspondingLobbyID;


    @OneToOne(targetEntity = Lobby.class)
    @JoinColumn(columnDefinition = "ID_lobby")
    @MapsId
    private Lobby lobby;

    // counts all users that have finished the round
    @Column
    private int allUsersFinishedRound = 0;

    @Column
    private int roundsFinished = 0;

    @Column
    private int numberOfPlayers = 0;


    // key UserID value GuessCoordinate
    @ElementCollection
    @CollectionTable
    private final Map<Long, ArrayList<Integer>> guesses = new HashMap<>();


    // key coordinate, value Picture
    @ElementCollection
    @CollectionTable
    private final Map<Integer, String> selectedPicturesURLs = new HashMap<>();

    //TODO see if this and its methods needs to be deleted
    @Column
    @ElementCollection
    private final Set<Screenshot> screenshots = new HashSet<>();

    //TODO see if this and its methods need to be deleted
    public Map<Long, ArrayList<Integer>> getGuesses() {
        return guesses;
    }


    //*****PICTURE SELECTION handlers

    public void addPicture(String pictureURL, int coordinate) {
        selectedPicturesURLs.put(coordinate, pictureURL);
    }

    public String[] getSelectedPictures() {
        if (selectedPicturesURLs == null || selectedPicturesURLs.size() < 1) {
            return null;
        }

        String[] PictureURLs = new String[16];
        for (int i = 0; i < 16; i++) {
            PictureURLs[i] = selectedPicturesURLs.get(i);
        }

        return PictureURLs;
    }

    public String getPictureWithCoordinates(int coordinate) {
        return selectedPicturesURLs.get(coordinate);
    }

    public void clearSelectedPictures() {
        selectedPicturesURLs.clear();
    }


    //****CORRESPONDING LOBBY to GAME handlers

    public Lobby getLobbyForGamePlay() {
        return lobby;
    }

    public void setLobbyForGamePlay(Lobby lobby) {
        this.lobby = lobby;
    }

    public String getCorrespondingLobbyID() {
        return correspondingLobbyID;
    }

    public void setCorrespondingLobbyID(String correspondingLobbyID) {
        this.correspondingLobbyID = correspondingLobbyID;
    }


    //**** SCREENSHOT handlers currently not used may be deleted
    public void addScreenshot(Screenshot screenshot) {
        screenshots.add(screenshot);
    }

    public ArrayList<Screenshot> getListOfScreenshots() {
        return new ArrayList<>(screenshots);
    }

    public void clearScreenshots() {
        screenshots.clear();
    }

    //***** ROUND HANDLERs


    public int getAllUsersFinishedRound() {
        return allUsersFinishedRound;
    }

    public void setAllUsersFinishedRound(int allUsersFinishedRound) {
        this.allUsersFinishedRound = allUsersFinishedRound;
    }

    public int getRoundsFinished() {
        return roundsFinished;
    }

    public void setRoundsFinished(int roundsFinished) {
        this.roundsFinished = roundsFinished;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}

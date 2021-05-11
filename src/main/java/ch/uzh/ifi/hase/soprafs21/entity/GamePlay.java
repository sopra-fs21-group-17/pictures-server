package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


//TODO discuss implementation for multiple instances of a game

/**
 * GamePlay will be used to handle incoming changes to the game from the client like UserGuesses
 * Maybe will need a Repository for the games which will change depending if the games are still active or not
 *
 */


@Entity
@Table(name = "GAME")
public class GamePlay implements Serializable {

    public GamePlay(){}

    private static final long serialVersionUID = 1L;

    // TODO see if still needed and remove otherwise


    @GeneratedValue
    private Long gameID;


    @Id()
    private String correspondingLobbyID;


    // key UserID value GuessCoordinate
    @ElementCollection
    @CollectionTable
    private Map<Long, ArrayList<Integer>> guesses= new HashMap<>();



    // key PictureID, value Coordinate
    @ElementCollection
    @Column
    private Map<Integer,Picture> selectedPictures = new HashMap<>();

    //TODO see if this and its methods needs to be deleted
    @Column
    @ElementCollection
    private Set<Screenshot> screenshots = new HashSet<>();

    //TODO see if this and its methods need to be deleted
    public Map<Long, ArrayList<Integer>> getGuesses() {
        return guesses;
    }


    //*****PICTURE SELECTION handlers
    public void addPicture(Picture picture, int coordinate){
        selectedPictures.put(coordinate,picture);
    }

    public Picture[] getSelectedPictures(){
        if(selectedPictures == null || selectedPictures.size() < 1){
            return null;
        }
        ArrayList<Picture> pictureArraylist;
        Picture picturesArray[] = new Picture[16];
        for(int i = 0; i < 16;i++){
            picturesArray[i] = selectedPictures.get(i);
        }


        return picturesArray;}

    public Picture getPictureWithCoordinates(int coordinate){
        return selectedPictures.get(coordinate);
    }

    public void clearSelectedPictures(){
        selectedPictures.clear();
    }


    //*****GAME ID handlers
    public Long getGameID(){return gameID;}


    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    //****CORRESPONDING LOBBY ID to GAME handlers
    public String getCorrespondingLobbyID() {
        return correspondingLobbyID;
    }

    public void setCorrespondingLobbyID(String correspondingLobbyID) {
        this.correspondingLobbyID = correspondingLobbyID;
    }

    //**** SCREENSHOT handlers currently not used may be deleted
    public void addScreenshot(Screenshot screenshot){
        screenshots.add(screenshot);
    }

    public ArrayList<Screenshot> getListOfScreenshots(){
        return new ArrayList<>(screenshots);
    }

    public void clearScreenshots(){
        screenshots.clear();
    }



}

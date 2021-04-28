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

    @Id
    @GeneratedValue
    @Column
    private Long gameID;



    // key UserID value GuessCoordinate
    @ElementCollection
    @CollectionTable
    private Map<Long, ArrayList<Integer>> guesses= new HashMap<>();



    // key PictureID, value Coordinate
    @ElementCollection
    @CollectionTable
    @Column
    private Map<Integer,Picture> selectedPictures = new HashMap<>();

    @Column
    @ElementCollection
    private Set<Screenshot> screenshots = new HashSet<>();


    public Map<Long, ArrayList<Integer>> getGuesses() {
        return guesses;
    }

    public void addPicture(Picture picture, int coordinate){
        selectedPictures.put(coordinate,picture);
    }

    public Picture getPictureWithToken(int token){
        return selectedPictures.get(token);
    }

    public Long getGameID(){return gameID;}

    public ArrayList<Picture> getSelectedPictures(){
        ArrayList<Picture> pictureArraylist = new ArrayList<>(selectedPictures.values());
        return pictureArraylist;}

    public void addScreenshot(Screenshot screenshot){
        screenshots.add(screenshot);
    }

    public ArrayList<Screenshot> getListOfScreenshots(){
        return new ArrayList<>(screenshots);
    }
    public void clearScreenshots(){
        screenshots.clear();
    }
    public void clearSelectedPictures(){
        selectedPictures.clear();
    }
}

package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private Long gameID;



    // key UserID value GuessCoordinate
    @ElementCollection
    @CollectionTable
    private Map<Long, ArrayList<Integer>> guesses= new HashMap<>();


    // key PictureID, value Coordinate
    @ElementCollection
    @CollectionTable
    private Map<Long,Integer> selectedPictures = new HashMap<>();

    //TODO see how screenshots are sent to backend
    //    @ElementCollection
    //    @CollectionTable
    //    private Map<Long,Screenshot> rebuilds = new HashMap<>();


    public Map<Long, ArrayList<Integer>> getGuesses() {
        return guesses;
    }

    public Map<Long, Integer> getSelectedPictures() {
        return selectedPictures;
    }



}

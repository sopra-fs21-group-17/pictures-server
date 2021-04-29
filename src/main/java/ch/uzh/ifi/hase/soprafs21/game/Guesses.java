package ch.uzh.ifi.hase.soprafs21.game;

import java.util.ArrayList;

/**
 * This class represents a guess.
 * For example, who did a guess? What were the guesses? etc. ...
 *
 * */
public class Guesses {
    public String username;
    public Long userID;
    public ArrayList<String> guesses; // format: {{"username", "A1"}, {"username", "A1"}}

    Guesses(){}

}

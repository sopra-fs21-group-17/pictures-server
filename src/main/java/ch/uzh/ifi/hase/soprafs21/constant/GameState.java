package ch.uzh.ifi.hase.soprafs21.constant;

//TODO see if this is necessary
//main idea controll the flow of the came by having states that handle the current part of the game.

/**
 * Enum to determine the current state of the game, and handle events according to the given state.
 * This is used to trigger certain actions depending on the state.
 * START: ...
 * SETUP: ...
 * ...
 */
public enum GameState {
    START,SETUP,PLAYING,GUESSING,RANKING,END,EXIT;
}
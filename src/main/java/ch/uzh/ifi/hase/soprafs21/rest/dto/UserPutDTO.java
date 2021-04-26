package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.Map;

public class UserPutDTO {
    private String username;
    private String[] guesses; // guess1: {"adam":null}, guess2: {"eva": null}, guess3: null, guess4: null
    // [{"adam":null}, {"adam":null}]
}

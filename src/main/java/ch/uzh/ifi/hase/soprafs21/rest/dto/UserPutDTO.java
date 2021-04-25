package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.Map;

public class UserPutDTO {
    private String username;
    private Map<User, String> guesses;
}

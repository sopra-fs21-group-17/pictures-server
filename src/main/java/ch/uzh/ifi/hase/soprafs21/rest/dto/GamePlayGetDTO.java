package ch.uzh.ifi.hase.soprafs21.rest.dto;


public class GamePlayGetDTO {

    private int rounds;

    private int allUsersFinishedRound;

    private int numberOfPlayers;

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getAllUsersFinishedRound() {
        return allUsersFinishedRound;
    }

    public void setAllUsersFinishedRound(int allUsersFinishedRound) {
        this.allUsersFinishedRound = allUsersFinishedRound;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}

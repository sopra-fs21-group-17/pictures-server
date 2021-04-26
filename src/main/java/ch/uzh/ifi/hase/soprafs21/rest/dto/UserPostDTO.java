package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class UserPostDTO {

    private String name;
    private String username;
    private int assignedCoordinates;
    private String assignedSet;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private String password;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getAssignedCoordinates() {
        return assignedCoordinates;
    }
    public void setAssignedCoordinates(int assignedCoordinates) {
        this.assignedCoordinates = assignedCoordinates;
    }

    public String getAssignedSet() {
        return assignedSet;
    }
    public void setAssignedSet(String assignedSet) {
        this.assignedSet = assignedSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

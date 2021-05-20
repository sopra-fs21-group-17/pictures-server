package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class BuildRoomPutDTO {

    private String roomId;

    private long creationTime;

    private double timeDifference;

    private long creationTimeGuessing;

    private double timeDifferenceGuessing;


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public double getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(double timeDifference) {
        this.timeDifference = timeDifference;
    }

    public long getCreationTimeGuessing() {
        return creationTimeGuessing;
    }

    public void setCreationTimeGuessing(long creationTimeGuessing) {
        this.creationTimeGuessing = creationTimeGuessing;
    }

    public double getTimeDifferenceGuessing() {
        return timeDifferenceGuessing;
    }

    public void setTimeDifferenceGuessing(double timeDifferenceGuessing) {
        this.timeDifferenceGuessing = timeDifferenceGuessing;
    }
}

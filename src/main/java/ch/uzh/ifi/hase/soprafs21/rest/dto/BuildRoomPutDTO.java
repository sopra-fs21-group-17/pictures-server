package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class BuildRoomPutDTO {

    private String roomId;

    private long creationTime;

    private double timeDifference;



    public String getRoomId() { return roomId; }

    public void setRoomId(String roomId) { this.roomId = roomId; }

    public long getCreationTime() { return creationTime; }

    public void setCreationTime(long creationTime) { this.creationTime = creationTime; }

    public double getTimeDifference() { return timeDifference; }

    public void setTimeDifference(double timeDifference) { this.timeDifference = timeDifference; }


}

package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class PicturesGetDTO {

    // picture ID is missing since it is only needed to randomize pictures

    private String pictureLink;
    private int coordinate;
    private Long UserID;

    public String getPictureLink() {
        return pictureLink;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public Long getUserID() {
        return UserID;
    }


}

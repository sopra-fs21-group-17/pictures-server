package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import ch.uzh.ifi.hase.soprafs21.entity.Screenshot;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "assignedCoordinates", target = "assignedCoordinates")
    @Mapping(source = "assignedSet", target = "assignedSet")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "birthdate", target = "birthdate")
    @Mapping(source = "guess", target = "guess")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "totalScore", target = "totalScore")
    @Mapping(source = "isReady", target = "isReady")
    //@Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    User convertUserGetDTOtoEntity(UserGetDTO userGetDTO);


    // TODO user PUT dto mapper
    @Mapping(source = "id", target =  "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "guesses", target = "guesses")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "pictureLink", target="pictureLink")
    PicturesGetDTO convertEntityToPicturesGetDTO(Picture picture);

    @Mapping(source = "URL", target = "URL")
    @Mapping(source = "userID", target = "userID")
    Screenshot convertScreenshotPutDTOtoEntity(ScreenshotPutDTO screenshotPutDTO);

    @Mapping(source = "URL", target = "URL")
    @Mapping(source = "userID", target = "userID")
    ScreenshotGetDTO convertEntityToScreenshotGetDTO(Screenshot screenshot);

//@Mapping(source = "pictureLink", target = "pictureLink")
//@Mapping(source = "coordinate", target = "coordinate")
//@Mapping(source = "userID" , target = "userID")
//    PicturesGetDTO convertEntityTOPicturesGetDTO(Picture picture);

}

package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Picture;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repo saves 50 pictures
 * Those pictures get randomly selected through an external API call
 * 16 out of those pictures get assigned a userID, so they can be used for the game
 * */


@Repository("picturesRepository")
public interface PicturesRepository extends JpaRepository<Picture, Long> {

    Picture findById(ID id);
    Picture findByuserID(Long userID);
    Picture findByPictureLink(String pictureLink);
    Picture findByCoordinates(int coordinate);

}

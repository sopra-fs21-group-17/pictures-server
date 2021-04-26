package ch.uzh.ifi.hase.soprafs21.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ch.uzh.ifi.hase.soprafs21.entity.GamePlay;

/**
 * Repository to be used to store information for the different game sessions.
 * it is used to store current selected pictures from their repositroy
 * also it is used to store the user guesses for the current game
 * aswell it will store the "screenshots" of the current game
 */
@Repository("GameSessionRepository")
public interface GameSessionRepository {

    GamePlay findbyGameID(Long gameID);
}

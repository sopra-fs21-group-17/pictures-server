package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.BuildRoom;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.BuildRoomRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BuildRoomService {


    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final BuildRoomRepository buildRoomRepository;

    @Autowired
    public BuildRoomService(@Qualifier("buildRoomRepository") BuildRoomRepository buildRoomRepository,@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository){
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.buildRoomRepository = buildRoomRepository;
    }


    //creates a new BuildRoom
    public BuildRoom createRoom(BuildRoom newBuildRoom)  {


        newBuildRoom.setCreationTime(System.nanoTime());

        // saves the given entity but data is only persisted in the database once flush() is called
        newBuildRoom = buildRoomRepository.save(newBuildRoom);
        buildRoomRepository.flush();


        return newBuildRoom;
    }

    //calculates and sets the timeDifference in order to after update the count
    public void updateCount(String roomId){
        BuildRoom foundByRoomId = buildRoomRepository.findByRoomId(roomId);

        if(foundByRoomId != null){
            long currentTime = System.nanoTime();

            long timeDifference = foundByRoomId.getCreationTime() - currentTime;

            foundByRoomId.setTimeDifference((double) timeDifference /1_000_000_000);
        }

        buildRoomRepository.flush();

    }
    public void resetRoom(String roomId){
        buildRoomRepository.deleteById(roomId);
        buildRoomRepository.flush();
    }

    public BuildRoom getBuildRoom(String roomId){

        BuildRoom buildRoom = buildRoomRepository.findByRoomId(roomId);
        return buildRoom;
    }

}

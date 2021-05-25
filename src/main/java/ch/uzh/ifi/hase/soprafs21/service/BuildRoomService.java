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



    private final BuildRoomRepository buildRoomRepository;

    @Autowired
    public BuildRoomService(@Qualifier("buildRoomRepository") BuildRoomRepository buildRoomRepository,@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository){

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

    //set creationTime for GuessingTimer
    public void setTimeGuessing(String roomId){
        BuildRoom foundByRoomId = buildRoomRepository.findByRoomId(roomId);

        if(foundByRoomId != null){
            foundByRoomId.setCreationTimeGuessing(System.nanoTime());

            buildRoomRepository.flush();

        }
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
    //calculates and sets the timeDifferenceGuessing in order to update the count
    public void updateCountGuessing(String roomId){
        BuildRoom foundByRoomId = buildRoomRepository.findByRoomId(roomId);

        if(foundByRoomId != null){
            long currentTime = System.nanoTime();

            long timeDifference= foundByRoomId.getCreationTimeGuessing() - currentTime;

            foundByRoomId.setTimeDifferenceGuessing((double) timeDifference /1_000_000_000);
        }

        buildRoomRepository.flush();
    }

    //deletes the room from the repository
    public void resetRoom(String roomId){
        buildRoomRepository.deleteById(roomId);
        buildRoomRepository.flush();
    }

    //gets the searched BuildRoom by the roomId
    public BuildRoom getBuildRoom(String roomId){

        BuildRoom buildRoom = buildRoomRepository.findByRoomId(roomId);
        return buildRoom;
    }

}

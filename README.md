# Pictures: Game of the year 2020
*Done by Group 17 as part of the course "Software-Praktikum" at UZH, spring semester 2021*

## Introduction
We created a digital version of the game of the year 2020 called Pictures. 
Pictures can be an ideal online game which provides lighthearted fun. We want to decouple that game from its board game constraints such as having to be on the same table with in a group of 3 to 5 or being limited to the pictures delivered with the board game. 

In pictures, every player gets a picture assigned that they have to re-build with a determined set ob objects (for example with wood blocks).
The goal of the game consists of guessing which picture the other players were trying to build. Guessing correctly gives the player who guessed correctly a point as well as the player who build the picture.
At the end of 5 rounds, by which every player has been able to try out every set out of the 5 available ones, the points are counted, and the person with the most points wins.

## 🛠️ Used technologies
We use the API Unsplash to fetch our photos. [Unsplash](https://unsplash.com/) provides freely-usable images that need no permission from photographs.
For our REST-environement we use Spring Boot. For more Information about Spring Boot go to: https://spring.io/

## Our core server components

### LobbyService
The lobby service handles the Lobby entities. 
The Lobby is essentially the place where players gather to start a game.
For this reason the lobby Service handles users that enter a lobby and stores them and their credentials.

This component basically manages Lobby entities which afterwards are used to identify a running game by assigning them to
Gameplay entities. The lobby entity is also the component providing the users for GameService.
GameService needs LobbyService to be able to operate on the received lobbies.

Link: [LobbyService](main/java/ch/uzh/ifi/hase/soprafs21/service/LobbyService.java)  
###GameService
The game service handles the entire setup of a game.
It contains the core game logic meaning any guesses users make
and computes their score.

This component coordinates the setup for every round:
* fetching 16 new pictures
* assign the sets and the coordinate for building a picture to the respective users.
* reset any handles for checking if all players have finished.

GameService manages GamePlay entities for every lobby. Initializing them for a new lobby or handling their content
during the game. 

Link: [GameService](main/java/ch/uzh/ifi/hase/soprafs21/service/GameService.java)

### ScheduledTasks
The ScheduledTask class is responsible for fetching the pictures from our external API.
It ensures that pictures are up to date and basically enables the game to stay challenging since
The pictures always change. The pictures are stored in the PicturesRepository,the GameService 
selects 16 of those pictures for every round. 

Link: [ScheduledTasks](main/java/ch/uzh/ifi/hase/soprafs21/schedulingtasks/ScheduledTasks.java)

## Launch and Deployment
### Installations needed for server
##### GitHub
To be able to collaborate and plan new developers need a gitHub account.
For registration see: https://github.com/


##### Heroku
We deploy our application on heroku, you as a new developer can make a free account on
https://www.heroku.com.

Install the heroku CLI for easy management of the app from the terminal.
Follow the instructions for installation [here](https://devcenter.heroku.com/articles/heroku-cli).

##### SonarQube
Our Projects code is assessed by SonarQube, make an account using your gitHub account on https://sonarcloud.io 

##### Gradle
The server part of "Pictures" uses gradle for building. To install Gradle follow the instructions [here](https://docs.gradle.org/current/userguide/installation.html).


## 🚀 Launch & deployment
After your following the installation guide to gradle you can build and run the code using the following commands:
using the gradle wrapper:

* for building:
```bash
./gradlew build
```

* for running:
```bash
./gradlew bootRun
```

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

## Testing
You can run the testcases as usual using your IDE or the following command:
```bash
./gradlew test
```
### What do we test:
#### Controller Testing
We have controller tests for our REST-Controllers to ensure that they are behaving as expected. 
The tests also help you to see what the respective method will send as a response if there is any. 
The most crucial part of this test is seen when mockMvc.perform is called: most checks are made here using 
.andExpect(...). You can see this in the example below:
```java
      @Test
       public void testGetScreenshots() throws Exception {
           Lobby testLobby = new Lobby();
           testLobby.setLobbyId("test");
    
           User testUser = new User();
           testUser.setId(((long) 1));
           testUser.setUsername("Test"+1);
           testUser.setAssignedCoordinates(1);
           testUser.setLobbyId(testLobby.getLobbyId());
    
           Screenshot testShot = new Screenshot();
           testShot.setUserID(testUser.getId());
           testShot.setURL("testURL");
    
           ArrayList<ArrayList<String>> userScreenshots = new ArrayList<>();
           ArrayList<String> screenshots = new ArrayList<>();
           screenshots.add(testShot.getURL());
           userScreenshots.add(screenshots);
    
           given(gameService.getUsersScreenshots("test")).willReturn(userScreenshots);
    
           MockHttpServletRequestBuilder getRequest = get("/screenshots/"+testLobby.getLobbyId());
    
           mockMvc.perform(getRequest).andExpect(status().isOk())
                   .andExpect(jsonPath("$.[0][0]",is(userScreenshots.get(0).get(0))));
    
       }
```
#### Unit Testing
We make unit tests to see if our methods behave in their core logic how they are expected to, many problems become visible 
once you write a testcase for any method. The central part are the assertions. Here is an example for our unit tests:
```java
     @Test
        public void create_BuildRoom_validInputs_success() {
            // when -> any object is being save in the buildRoomRepository -> return the dummy testBuildRoom
            BuildRoom createdBuildRoom = buildRoomService.createRoom(testBuildRoom);
    
            // then
            Mockito.verify(buildRoomRepository, Mockito.times(1)).save(Mockito.any());
    
            assertEquals(testBuildRoom.getRoomId(), createdBuildRoom.getRoomId());
            assertEquals(testBuildRoom.getCreationTime(), createdBuildRoom.getCreationTime());
            assertEquals(testBuildRoom.getTimeDifference(), createdBuildRoom.getTimeDifference());
            assertEquals(testBuildRoom.getCreationTimeGuessing(), createdBuildRoom.getCreationTimeGuessing());
            assertEquals(testBuildRoom.getTimeDifferenceGuessing(), createdBuildRoom.getTimeDifferenceGuessing());
    
        }
 ```       
#### Integration Testing
Integration tests ensure that our methods behave as expected when they collaborate with other components depending on successful situations or failures of the system.
Those tests are needed if have methods that heavily rely on for example Repositories. Here is an example for a possible 
Integration test:

```java
        @Test
        public void createLobby_validInputs_success() {
            // given
            assertNull(lobbyRepository.findByLobbyId("AbCd"));
            assertNull(userRepository.findByUsername("testUsername"));
    
            Lobby testLobby = new Lobby();
            testLobby.setLobbyId("AbCd");
    
    
            // when
            Lobby createdLobby = lobbyService.createLobby(testLobby);
    
    
            // then
            assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());
    
        }
```      
More extensive examples can be found in [GameServiceIntegrationTest](test/java/ch/uzh/ifi/hase/soprafs21/service/GameServiceIntegrationTest.java)



### Current Issues
Currently, we have trouble running all testcases for integration testing consecutively.
in the GameServiceIntegrationTest class. You can run individual tests without problems but
gradle will hang if you try to run all tests at once from GameServiceIntegrationTest. 

## 🚗 Roadmap - what would be good additions?
* Currently, you can only enter a lobby if you have a code, a new feature could entail having lists of public lobbies, that
can be joined by anyone much like in other games. This would also entail having to manage visibility settings of a lobby.

* Another nice feature if storing the recreated pictures (the screenshots) have a history to see how you built them. Outside the game.
This would mean that a user would need a profile where content like this is stored. In addition to that have metrics like "how many games played", "best score", 
visible for other players if the user chooses so.

* A third feature that enhances usability would be a means of communication between players like a written chat or voice chat.     
 

## 🖋️ Authors and acknowledgement
@dkajin, @jukosta, @lakicv, @olstra, @xinox2000  
*We want to thank community of photographers on Unsplash for their wonderful work. 
As we fetch the photos at random, the single contributors are hard to identify. We highly encourage 
you to visit [Unsplash](https://unsplash.com/) as our form of appreciation.*


## ⚖️ License
This project is licensed under the terms of the XXX license.  

package ch.uzh.ifi.hase.soprafs21.schedulingtasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.Picture;

import ch.uzh.ifi.hase.soprafs21.repository.PicturesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private PicturesRepository picturesRepository;

    @Autowired
    public void ScheduledTask(@Qualifier("picturesRepository") PicturesRepository picturesRepository) {
        this.picturesRepository = picturesRepository;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @RequestMapping(value="/Object/getList/", method=RequestMethod.GET)
    public @ResponseBody List<Object> findAllObjects() {
        List<Object> objects = new ArrayList<Object>();
        return objects;
    }

    /**
     * calls the Unsplash API en retrieves a list with all the URLs of the pictures in a specified interval
     */
    @Scheduled(fixedRate = 1000*60*60*12) //timeinterval set to execute every 12 hours
    public void fetchPictures() throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();

            //executes API call to retrieve all the Pictures
            ResponseEntity<Object[]> response1 = restTemplate.getForEntity("https://api.unsplash.com/collections/92251930/photos/?client_id=XOKcoSRnH7ajyAT1Qd0kQ0K0-fDjw_FGvqFyZE27Zso&per_page=30", Object[].class);
            ResponseEntity<Object[]> response2 = restTemplate.getForEntity("https://api.unsplash.com/collections/92251930/photos/?client_id=XOKcoSRnH7ajyAT1Qd0kQ0K0-fDjw_FGvqFyZE27Zso&per_page=30&page=2", Object[].class);

            //response bodies added to a object list
            Object[] objects1 = response1.getBody();
            Object[] objects2 = response2.getBody();

            //creates a picture entity and adds it to the picturesrepository for every picture in the objectlist
            for (int i = 0; i < objects1.length; i++) {

                String test = objects1[i].toString();
                Picture newPicture = new Picture();
                newPicture.setPictureLink(test.substring(test.indexOf("regular=") + 8, test.indexOf(",", test.indexOf("regular="))));
                createPicture(newPicture);
            }

            //creates a picture entity and adds it to the picturesrepository for every picture in the objectlist
            for (int i = 0; i < objects2.length; i++) {

                String test = objects2[i].toString();
                Picture newPicture = new Picture();
                newPicture.setPictureLink(test.substring(test.indexOf("regular=") + 8, test.indexOf(",", test.indexOf("regular="))));
                createPicture(newPicture);
            }

            //created for testing purposes
            log.info("Pictures Fetched");
//        log.info("The time is now {}", dateFormat.format(new Date()));
//        log.info(String.valueOf(this.picturesRepository.findAll()));
        }
        catch (Exception e){
            throw new Exception ("Failed to fetch Images)");
        }
    };

    public void createPicture(Picture newPicture)  {

        // saves the given entity but data is only persisted in the database once flush() is called
        picturesRepository.save(newPicture);
        picturesRepository.flush();
    }
}

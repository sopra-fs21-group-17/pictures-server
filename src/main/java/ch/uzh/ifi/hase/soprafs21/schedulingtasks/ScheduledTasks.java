package ch.uzh.ifi.hase.soprafs21.schedulingtasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //TODO change schedule to weekly (cron="0 0 12 * * MON")
    //TODO add API call to retrieve pictures collection from unsplash (https://api.unsplash.com/collections/92251930/photos/?client_id=XOKcoSRnH7ajyAT1Qd0kQ0K0-fDjw_FGvqFyZE27Zso)
    @Scheduled(cron="*/5 * * * * *")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}

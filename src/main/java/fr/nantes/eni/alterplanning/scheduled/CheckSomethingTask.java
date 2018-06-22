package fr.nantes.eni.alterplanning.scheduled;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CheckSomethingTask {

    private final static Logger LOGGER = Logger.getLogger(CheckSomethingTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "*/30 * * * * *") //every thirty seconds.
    public void checkSomethingEveryThirtySeconds() {
        LOGGER.info("The time is now " +  dateFormat.format(new Date()));
    }
}

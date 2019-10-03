package com.qatestlab.olegvolik;

import com.qatestlab.olegvolik.domain.Event;
import com.qatestlab.olegvolik.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimerTask;

/**This class purpose is check the database so that
 * to determine the upcoming events. It also prints out
 * these events. These actions are performed by the only
 * method run().
 * @author Oleg Volik
 */
@Slf4j
@Component
public class EventChecker extends TimerTask {

    // The basic service for interaction with domain objects
    @Autowired
    private EventService eventService;

    public EventChecker () {}

    @Override
    public void run() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusInterval = now.plusHours(12);
        List<Event> upcomingEvents = eventService.findByDateTimeBetween(now, nowPlusInterval);

        if (!upcomingEvents.isEmpty()) {
            log.info("*** NEW EVENTS COMING WITHIN 12 HOURS!!! ***");
            upcomingEvents.stream().forEach(System.out::println);
        }
    }
}

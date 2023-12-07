package com.sportevents.event;

import com.sportevents.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@Slf4j
public class EventScheduler {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Autowired
    public EventScheduler(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Async
    @Scheduled(fixedDelay = 30000) //runs every 30 seconds
    public void checkIfEventIsOver() {
        var counterWrapper = new Object(){ int counter = 0; };
        eventRepository.findAllByActiveAndDateBefore(true, new Date()).stream().filter(event -> event.getDate().before(new Date()))
                .forEach(event -> {
                    if(event.isCyclical()) {
                        event.setDate(Date.from(event.getDate().toInstant().plus(
                                Duration.ofDays(event.getCyclicalPeriodInDays())
                        )));
                    } else {
                        event.setActive(false);
                    }
            eventRepository.save(event);
            counterWrapper.counter++;
        });
        log.info("Scheduler is running - deactivated " + counterWrapper.counter + " events");
    }

}

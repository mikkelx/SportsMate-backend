package com.sportevents.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class EventScheduler {

    private EventRepository eventRepository;

    @Autowired
    public EventScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Async
    @Scheduled(fixedDelay = 30000) //runs every 30 seconds
    public void checkIfEventIsOver() {
        var counterWrapper = new Object(){ int counter = 0; };
        eventRepository.findAllByActiveAndDateBefore(true, new Date()).stream().filter(event -> event.getDate().before(new Date()))
                .forEach(event -> {
            event.setActive(false);
            eventRepository.save(event);
            counterWrapper.counter++;
        });
        log.info("Scheduler is running - deactivated " + counterWrapper.counter + " events");
    }

}

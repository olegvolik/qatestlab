package com.qatestlab.olegvolik.service;

import com.qatestlab.olegvolik.dao.EventRepository;
import com.qatestlab.olegvolik.domain.Event;
import com.qatestlab.olegvolik.domain.MyEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** This class is the EventService interface implementation.
 * It consists of interface methods, overridden by means of
 * using corresponding methods of EventRepository.
 * @author Oleg Volik
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    /** Get all events from the DB
     *
     * @return list of events
     */
    @Override
    public List<Event> getList () {
        return eventRepository.findAll();
    }

    /** The method for searching events by type
     * using the Spring Data DSL method signature
     *
     * @param type - Event type
     * @return list of events
     */
    @Override
    public List<Event> findByType (MyEventType type) {
        return eventRepository.findByType(type);
    }

    /** The method for searching events by date
     * using the Spring Data DSL method signature
     *
     * @param date - Event date
     * @return list of events
     */
    @Override
    public List<Event> findByDate (LocalDate date) {
        return eventRepository.findByDate(date);
    }

    /** The method for searching events by type
     * using the Spring Data DSL method signature
     *
     * @param name - Event name
     * @return single event
     */
    @Override
    public Event findByName (String name) {
        return eventRepository.findByName(name);
    }

    /** Save event to the DB
     *
     * @param event - event to save
     * @return saved event
     */
    @Override
    public Event save (Event event) {
        return eventRepository.save(event);
    }

    /** Save multiple events to the DB
     *
     * @param events - events to save
     * @return collection of saved events
     */
    @Override
    public Iterable<Event> saveAll (Iterable<Event> events) {
        return eventRepository.saveAll(events);
    }

    /** Delete event from the DB
     *
     * @param event - event to delete
     */
    @Override
    public void delete (Event event) {
        eventRepository.delete(event);
    }

    /** The method for searching events by date and type
     * using the Spring Data DSL method signature
     *
     * @param date - Event date
     * @param type - Event type
     * @return list of events
     */
    @Override
    public List<Event> findByTypeAndDate (MyEventType type, LocalDate date) {
     return eventRepository.findByTypeAndDate(type, date);
    }

    /** The method for searching for upcoming events
     * within the time interval described with method parameters
     * using the Spring Data DSL method signature
     *
     * @param now - Present time
     * @param nowPlusInterval - Future time
     * @return list of events
     */
    @Override
    public List<Event> findByDateTimeBetween (LocalDateTime now, LocalDateTime nowPlusInterval) {
        return eventRepository.findByDateTimeBetween(now, nowPlusInterval);
    }
}

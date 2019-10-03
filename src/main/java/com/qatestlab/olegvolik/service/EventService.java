package com.qatestlab.olegvolik.service;

import com.qatestlab.olegvolik.domain.Event;
import com.qatestlab.olegvolik.domain.MyEventType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** This interface is a service interface. It is an intermediary
 * between DAO class and controller. It declares general actions
 * can be performed to the Event objects and which reproduce the
 * repository methods.
 * @author Oleg Volik
 */
public interface EventService {

    /** Get all events from the DB
     *
     * @return list of events
     */
    List<Event> getList ();

    /** The method for searching events by type
     * using the Spring Data DSL method signature
     *
     * @param type - Event type
     * @return list of events
     */
    List<Event> findByType (MyEventType type);

    /** The method for searching events by date
     * using the Spring Data DSL method signature
     *
     * @param date - Event date
     * @return list of events
     */
    List<Event> findByDate (LocalDate date);

    /** The method for searching events by type
     * using the Spring Data DSL method signature
     *
     * @param name - Event name
     * @return single event
     */
    Event findByName (String name);

    /** Save event to the DB
     *
     * @param event - event to save
     * @return saved event
     */
    Event save (Event event);

    /** Save multiple events to the DB
     *
     * @param events - events to save
     * @return collection of saved events
     */
    Iterable<Event> saveAll (Iterable<Event> events);

    /** Delete event from the DB
     *
     * @param event - event to delete
     */
    void delete (Event event);

    /** The method for searching events by date and type
     * using the Spring Data DSL method signature
     *
     * @param date - Event date
     * @param type - Event type
     * @return list of events
     */
    List<Event> findByTypeAndDate (MyEventType type, LocalDate date);

    /** The method for searching for upcoming events
     * within the time interval described with method parameters
     * using the Spring Data DSL method signature
     *
     * @param now - Present time
     * @param nowPlusInterval - Future time
     * @return list of events
     */
    List<Event> findByDateTimeBetween (LocalDateTime now, LocalDateTime nowPlusInterval);
}

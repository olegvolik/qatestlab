package com.qatestlab.olegvolik.dao;

import com.qatestlab.olegvolik.aop.Loggable;
import com.qatestlab.olegvolik.domain.Event;
import com.qatestlab.olegvolik.domain.MyEventType;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** The class is a part of the application DAO layer. The purpose of it is
 * an interaction between the application and the database. It provides methods
 * for getting the event objects from the database, saving and deleting them.
 * @author Oleg Volik
 */
public interface EventRepository extends CrudRepository<Event, Long> {

    /** Get all events from the DB
     *
     * @return list of events
     */
    @Loggable
    @Override
    List<Event> findAll();

    /** The method for searching events by type
     * using the Spring Data DSL method signature
     *
     * @param type - Event type
     * @return list of events
     */
    @Loggable
    List<Event> findByType (MyEventType type);

    /** The method for searching events by date
     * using the Spring Data DSL method signature
     *
     * @param date - Event date
     * @return list of events
     */
    @Loggable
    List<Event> findByDate (LocalDate date);

    /** The method for searching events by type
     * using the Spring Data DSL method signature
     *
     * @param name - Event name
     * @return single event
     */
    @Loggable
    Event findByName (String name);

    /** The method for searching events by date and type
     * using the Spring Data DSL method signature
     *
     * @param date - Event date
     * @param type - Event type
     * @return list of events
     */
    @Loggable
    List<Event> findByTypeAndDate (MyEventType type, LocalDate date);

    /** The method for searching for upcoming events
     * within the time interval described with method parameters
     * using the Spring Data DSL method signature
     *
     * @param now - Present time
     * @param nowPlusInterval - Future time
     * @return list of events
     */
    @Loggable
    List<Event> findByDateTimeBetween (LocalDateTime now, LocalDateTime nowPlusInterval);
}

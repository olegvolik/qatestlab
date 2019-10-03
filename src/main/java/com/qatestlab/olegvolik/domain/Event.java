package com.qatestlab.olegvolik.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** The class is a part of the application domain layer. It is an entity class,
 * so its objects can be stored in the database. This class represents a real
 * event with its detail properties list.
 * {@link Event}
 * @author Oleg Volik
 */
@Data
@Entity
@Table (name = "events", schema = "qatestlab")
public class Event {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private MyEventType type;

    @Column
    private LocalDate date;

    @Column
    private LocalTime startTime;

    @Column
    private LocalDateTime dateTime;

    @Column
    private LocalDateTime createdAt;

    public Event() {}

    @PrePersist
    void createDatesFields() {
        this.createdAt = LocalDateTime.now();
        this.dateTime = LocalDateTime.of(this.date, this.startTime);
    }

    /* Getters and Setters are automatically generated, the equals() and hashCode() methods are
     * automatically overridden through the use of Lombok's @Data annotation
     */

}

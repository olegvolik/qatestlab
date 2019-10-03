package com.qatestlab.olegvolik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qatestlab.olegvolik.domain.Event;
import com.qatestlab.olegvolik.domain.MyEventType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
class PostgresTest {

    private static Connection connection;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final File EVENTFILE = new File
            ("E:\\Programming\\IdeaProjects\\qatestlab\\src\\test\\java\\com\\qatestlab\\olegvolik\\events.json");

    @BeforeAll
    static void createConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EventNotifier",
                    "postgres", "*****");
            log.info("***Connection created successfully***");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDataBaseRead() {
        List<Event> mappedEvents = mapEvents(EVENTFILE);
        ArrayList<Event> readEvents = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();

            testRowsDelete();
            testDataBaseAdd(mappedEvents);

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM qatestlab.events");
            Assertions.assertNotNull(resultSet);

            while (resultSet.next()) {
                int i = 0;
                Event event = new Event();
                event.setId(resultSet.getLong("id"));
                event.setName(resultSet.getString("name"));
                event.setType(MyEventType.valueOf(resultSet.getString("type")));
                event.setDate(resultSet.getDate("date") == null ?
                        null : resultSet.getDate("date").toLocalDate());
                event.setStartTime(resultSet.getTime("start_time") == null ?
                        null : resultSet.getTime("start_time").toLocalTime());
                event.setDateTime(resultSet.getTimestamp("date_time") == null ?
                        null : resultSet.getTimestamp("date_time").toLocalDateTime());
                event.setCreatedAt(resultSet.getTimestamp("created_at") == null ?
                        null : resultSet.getTimestamp("created_at").toLocalDateTime());
                readEvents.add(event);
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            Assertions.assertEquals(mappedEvents.size(), readEvents.size());
    }

    List<Event> mapEvents (File file) {
        Event[] events = null;
        try {
            events = MAPPER.readValue(file, Event[].class);
            log.info("***Events have been successfully mapped from file " + file + " ***");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(Optional.ofNullable(events).orElse(new Event[0]));
    }

    void testDataBaseAdd (List<Event> events) {
        log.info("***Adding new entries***");
        try {
            int i = 0;
            for (Event ev : events) {
                PreparedStatement stmt = connection.prepareStatement
                        ("INSERT INTO qatestlab.events VALUES(?, ?, ?, ?, ?, ?, ?)");
                stmt.setLong(1, ev.getId());
                stmt.setString(4, ev.getName());
                stmt.setString(6, ev.getType().toString());
                stmt.setDate(3, Date.valueOf(
                        Optional.ofNullable(ev.getDate()).orElse(LocalDate.now())));
                stmt.setTime(5, Time.valueOf(
                        Optional.ofNullable(ev.getStartTime()).orElse(LocalTime.now())));
                stmt.setTimestamp(2, Timestamp.valueOf(
                        Optional.ofNullable(ev.getDateTime()).orElse(LocalDateTime.now())));
                stmt.setTimestamp(7, Timestamp.valueOf(
                        Optional.ofNullable(ev.getCreatedAt()).orElse(LocalDateTime.now())));
                stmt.executeUpdate();

                log.info("Entry #" + ++i + " added successfully***");
                stmt.close();
            }

            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM qatestlab.events");
            resultSet.last();

            Assertions.assertEquals(events.size(), resultSet.getRow());

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRowsDelete () {
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.execute("DELETE FROM qatestlab.events");
            log.info("***All entries deleted***");

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM qatestlab.events");
            Assertions.assertNotNull(resultSet);
            resultSet.last();

            Assertions.assertEquals(0, resultSet.getRow());

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.smartcampus.dao;

import com.smartcampus.model.Event;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    // GET ALL EVENTS
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        String sql = """
                SELECT event_id,
                       event_name,
                       description,
                       event_date,
                       start_time,
                       end_time,
                       organizer_id,
                       organizing_department,
                       organized_by
                FROM events
                ORDER BY event_date, start_time
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("event_id"),
                        resultSet.getString("event_name"),
                        resultSet.getString("description"),
                        resultSet.getDate("event_date").toLocalDate(),
                        resultSet.getTime("start_time").toLocalTime(),
                        resultSet.getTime("end_time").toLocalTime(),
                        resultSet.getInt("organizer_id")
                );

                event.setOrganizingDepartment(
                        resultSet.getString("organizing_department"));
                event.setOrganizedBy(
                        resultSet.getString("organized_by"));

                events.add(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    // ADD EVENT AND RETURN GENERATED DATABASE ID
    public int addEventAndReturnId(Event event) {
        String sql = """
                INSERT INTO events
                (
                    event_name,
                    description,
                    event_date,
                    start_time,
                    end_time,
                    organizer_id,
                    organizing_department,
                    organized_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(event.getEventDate()));
            statement.setTime(4, java.sql.Time.valueOf(event.getStartTime()));
            statement.setTime(5, java.sql.Time.valueOf(event.getEndTime()));
            statement.setInt(6, event.getOrganizerId());
            statement.setString(7, event.getOrganizingDepartment());
            statement.setString(8, event.getOrganizedBy());

            int affected = statement.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    event.setEventId(generatedId);
                    return generatedId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ADD EVENT
    public boolean addEvent(Event event) {
        String sql = """
                INSERT INTO events
                (
                    event_name,
                    description,
                    event_date,
                    start_time,
                    end_time,
                    organizer_id,
                    organizing_department,
                    organized_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(event.getEventDate()));
            statement.setTime(4, java.sql.Time.valueOf(event.getStartTime()));
            statement.setTime(5, java.sql.Time.valueOf(event.getEndTime()));
            statement.setInt(6, event.getOrganizerId());
            statement.setString(7, event.getOrganizingDepartment());
            statement.setString(8, event.getOrganizedBy());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // UPDATE EVENT
    public boolean updateEvent(Event event) {
        String sql = """
                UPDATE events
                SET event_name = ?,
                    description = ?,
                    event_date = ?,
                    start_time = ?,
                    end_time = ?,
                    organizer_id = ?,
                    organizing_department = ?,
                    organized_by = ?
                WHERE event_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(event.getEventDate()));
            statement.setTime(4, java.sql.Time.valueOf(event.getStartTime()));
            statement.setTime(5, java.sql.Time.valueOf(event.getEndTime()));
            statement.setInt(6, event.getOrganizerId());
            statement.setString(7, event.getOrganizingDepartment());
            statement.setString(8, event.getOrganizedBy());
            statement.setInt(9, event.getEventId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // DELETE EVENT
    public boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM events WHERE event_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);
            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}

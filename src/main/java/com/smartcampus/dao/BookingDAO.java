package com.smartcampus.dao;

import com.smartcampus.model.Booking;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // Get all bookings, including CANCELLED bookings
    public List<Booking> getAllBookings() {

        List<Booking> bookings = new ArrayList<>();

        String sql = """
                SELECT booking_id, user_id, resource_id, event_id,
                       booking_date, start_time, end_time, booking_status
                FROM bookings
                ORDER BY booking_date, start_time
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bookings.add(mapBooking(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // Get bookings belonging to a particular user
    public List<Booking> getBookingsByUserId(int userId) {

        List<Booking> bookings = new ArrayList<>();

        String sql = """
                SELECT booking_id, user_id, resource_id, event_id,
                       booking_date, start_time, end_time, booking_status
                FROM bookings
                WHERE user_id = ?
                ORDER BY booking_date, start_time
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapBooking(resultSet));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // Convert a database row into a Booking object
    private Booking mapBooking(ResultSet resultSet) throws Exception {

        return new Booking(
                resultSet.getInt("booking_id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("resource_id"),
                resultSet.getInt("event_id"),
                resultSet.getDate("booking_date").toLocalDate(),
                resultSet.getTime("start_time").toLocalTime(),
                resultSet.getTime("end_time").toLocalTime(),
                resultSet.getString("booking_status")
        );
    }

    // Create a booking with initial status PENDING
    // Create a booking with conflict prevention
public boolean createBooking(Booking booking) {

    if (booking == null
        || booking.getUserId() <= 0
        || booking.getResourceId() <= 0
        || booking.getBookingDate() == null
        || booking.getStartTime() == null
        || booking.getEndTime() == null
        || booking.getBookingDate().isBefore(java.time.LocalDate.now())
        || (booking.getBookingDate().isEqual(java.time.LocalDate.now())
            && !booking.getStartTime().isAfter(java.time.LocalTime.now()))
        || !booking.getEndTime().isAfter(booking.getStartTime())) {
    return false;
}

    String conflictSql = """
            SELECT COUNT(*)
            FROM bookings
            WHERE resource_id = ?
              AND booking_date = ?
              AND booking_status IN ('PENDING', 'APPROVED')
              AND start_time < ?
              AND end_time > ?
            """;

    String insertSql = """
            INSERT INTO bookings
            (user_id, resource_id, event_id,
             booking_date, start_time, end_time, booking_status)
            VALUES (?, ?, ?, ?, ?, ?, 'PENDING')
            """;

    try (Connection connection = DBConnection.getConnection()) {

        // Prevent two simultaneous booking transactions from
        // checking the same resource at the same time.
        connection.setAutoCommit(false);

        try {
            // Lock the resource row while checking and inserting.
            String lockSql = """
                    SELECT resource_id
                    FROM resources
                    WHERE resource_id = ?
                    FOR UPDATE
                    """;

            try (PreparedStatement lockStatement =
                         connection.prepareStatement(lockSql)) {

                lockStatement.setInt(1, booking.getResourceId());

                try (ResultSet resultSet = lockStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        connection.rollback();
                        return false;
                    }
                }
            }

            // Check for overlapping bookings.
            try (PreparedStatement conflictStatement =
                         connection.prepareStatement(conflictSql)) {

                conflictStatement.setInt(1, booking.getResourceId());
                conflictStatement.setDate(
                        2,
                        java.sql.Date.valueOf(booking.getBookingDate())
                );
                conflictStatement.setTime(
                        3,
                        java.sql.Time.valueOf(booking.getEndTime())
                );
                conflictStatement.setTime(
                        4,
                        java.sql.Time.valueOf(booking.getStartTime())
                );

                try (ResultSet resultSet =
                             conflictStatement.executeQuery()) {

                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        connection.rollback();
                        return false;
                    }
                }
            }


            try (PreparedStatement insertStatement =
                         connection.prepareStatement(insertSql)) {

                insertStatement.setInt(1, booking.getUserId());
                insertStatement.setInt(2, booking.getResourceId());

                if (booking.getEventId() > 0) {
                    insertStatement.setInt(3, booking.getEventId());
                } else {
                    insertStatement.setNull(3, java.sql.Types.INTEGER);
                }

                insertStatement.setDate(
                        4,
                        java.sql.Date.valueOf(booking.getBookingDate())
                );
                insertStatement.setTime(
                        5,
                        java.sql.Time.valueOf(booking.getStartTime())
                );
                insertStatement.setTime(
                        6,
                        java.sql.Time.valueOf(booking.getEndTime())
                );

                boolean success = insertStatement.executeUpdate() > 0;

                if (success) {
                    connection.commit();
                } else {
                    connection.rollback();
                }

                return success;
            }

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public boolean updateBookingStatus(int bookingId, String status) {

    if (bookingId <= 0 || !isValidStatus(status)) {
        return false;
    }

    String selectSql = """
            SELECT resource_id, booking_date, start_time, end_time,
                   booking_status
            FROM bookings
            WHERE booking_id = ?
            FOR UPDATE
            """;

    String conflictSql = """
            SELECT COUNT(*)
            FROM bookings
            WHERE resource_id = ?
              AND booking_date = ?
              AND booking_id <> ?
              AND booking_status = 'APPROVED'
              AND start_time < ?
              AND end_time > ?
            """;

    String updateSql = """
            UPDATE bookings
            SET booking_status = ?
            WHERE booking_id = ?
              AND (
                    (
                        booking_status = 'PENDING'
                        AND ? IN ('APPROVED', 'REJECTED', 'CANCELLED')
                    )
                    OR
                    (
                        booking_status = 'APPROVED'
                        AND (
                            ? = 'CANCELLED'
                            OR (
                                ? = 'COMPLETED'
                                AND TIMESTAMP(booking_date, end_time) < NOW()
                            )
                        )
                    )
              )
            """;

    try (Connection connection = DBConnection.getConnection()) {

        connection.setAutoCommit(false);

        try {
            int resourceId;
            java.sql.Date bookingDate;
            java.sql.Time startTime;
            java.sql.Time endTime;
            String currentStatus;

            // Lock and retrieve the selected booking.
            try (PreparedStatement selectStatement =
                         connection.prepareStatement(selectSql)) {

                selectStatement.setInt(1, bookingId);

                try (ResultSet rs = selectStatement.executeQuery()) {

                    if (!rs.next()) {
                        connection.rollback();
                        return false;
                    }

                    resourceId = rs.getInt("resource_id");
                    bookingDate = rs.getDate("booking_date");
                    startTime = rs.getTime("start_time");
                    endTime = rs.getTime("end_time");
                    currentStatus = rs.getString("booking_status");
                }
            }

            // Before approval, check for an existing approved conflict.
            if ("APPROVED".equals(status)
                    && "PENDING".equals(currentStatus)) {

                // Lock the resource row to coordinate approval
                // with booking creation and other approvals.
                String resourceLockSql = """
                        SELECT resource_id
                        FROM resources
                        WHERE resource_id = ?
                        FOR UPDATE
                        """;

                try (PreparedStatement lockStatement =
                             connection.prepareStatement(resourceLockSql)) {

                    lockStatement.setInt(1, resourceId);

                    try (ResultSet rs = lockStatement.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement conflictStatement =
                             connection.prepareStatement(conflictSql)) {

                    conflictStatement.setInt(1, resourceId);
                    conflictStatement.setDate(2, bookingDate);
                    conflictStatement.setInt(3, bookingId);
                    conflictStatement.setTime(4, endTime);
                    conflictStatement.setTime(5, startTime);

                    try (ResultSet rs = conflictStatement.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            connection.rollback();
                            return false;
                        }
                    }
                }
            }

            // Update only if the status transition is permitted.
            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql)) {

                updateStatement.setString(1, status);
                updateStatement.setInt(2, bookingId);
                updateStatement.setString(3, status);
                updateStatement.setString(4, status);
                updateStatement.setString(5, status);

                boolean success = updateStatement.executeUpdate() > 0;

                if (success) {
                    connection.commit();
                } else {
                    connection.rollback();
                }

                return success;
            }

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean updatePendingBookingDetails(
            int bookingId,
            int resourceId,
            java.time.LocalDate bookingDate,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime) {

        if (bookingId <= 0
                || resourceId <= 0
                || bookingDate == null
                || startTime == null
                || endTime == null
                || !endTime.isAfter(startTime)) {
            return false;
        }

        String bookingSql = """
                SELECT event_id, booking_status
                FROM bookings
                WHERE booking_id = ?
                FOR UPDATE
                """;
        String resourceSql = """
                SELECT availability_status
                FROM resources
                WHERE resource_id = ?
                FOR UPDATE
                """;
        String conflictSql = """
                SELECT COUNT(*)
                FROM bookings
                WHERE resource_id = ?
                  AND booking_date = ?
                  AND booking_id <> ?
                  AND booking_status IN ('PENDING', 'APPROVED')
                  AND start_time < ?
                  AND end_time > ?
                """;
        String updateBookingSql = """
                UPDATE bookings
                SET resource_id = ?, booking_date = ?, start_time = ?, end_time = ?
                WHERE booking_id = ? AND booking_status = 'PENDING'
                """;
        String updateEventSql = """
                UPDATE events
                SET event_date = ?, start_time = ?, end_time = ?
                WHERE event_id = ?
                """;

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int eventId;
                try (PreparedStatement statement =
                             connection.prepareStatement(bookingSql)) {
                    statement.setInt(1, bookingId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()
                                || !"PENDING".equals(
                                        resultSet.getString("booking_status"))) {
                            connection.rollback();
                            return false;
                        }
                        eventId = resultSet.getInt("event_id");
                        if (resultSet.wasNull()) {
                            eventId = 0;
                        }
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(resourceSql)) {
                    statement.setInt(1, resourceId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()
                                || !"AVAILABLE".equals(
                                        resultSet.getString("availability_status"))) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(conflictSql)) {
                    statement.setInt(1, resourceId);
                    statement.setDate(
                            2, java.sql.Date.valueOf(bookingDate));
                    statement.setInt(3, bookingId);
                    statement.setTime(4, java.sql.Time.valueOf(endTime));
                    statement.setTime(5, java.sql.Time.valueOf(startTime));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookingSql)) {
                    statement.setInt(1, resourceId);
                    statement.setDate(
                            2, java.sql.Date.valueOf(bookingDate));
                    statement.setTime(3, java.sql.Time.valueOf(startTime));
                    statement.setTime(4, java.sql.Time.valueOf(endTime));
                    statement.setInt(5, bookingId);
                    if (statement.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                if (eventId > 0) {
                    try (PreparedStatement statement =
                                 connection.prepareStatement(updateEventSql)) {
                        statement.setDate(
                                1, java.sql.Date.valueOf(bookingDate));
                        statement.setTime(2, java.sql.Time.valueOf(startTime));
                        statement.setTime(3, java.sql.Time.valueOf(endTime));
                        statement.setInt(4, eventId);
                        statement.executeUpdate();
                    }
                }

                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBooking(int bookingId) {
        if (bookingId <= 0) {
            return false;
        }

        String sql = """
                DELETE FROM bookings
                WHERE booking_id = ?
                  AND booking_status IN ('REJECTED', 'CANCELLED')
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cancel a user's own pending or approved booking
    public boolean cancelBooking(int bookingId, int userId) {

        if (bookingId <= 0 || userId <= 0) {
            return false;
        }

        String sql = """
                UPDATE bookings
                SET booking_status = 'CANCELLED'
                WHERE booking_id = ?
                  AND user_id = ?
                  AND booking_status IN ('PENDING', 'APPROVED')
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, bookingId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Validate supported booking statuses
    private boolean isValidStatus(String status) {

        if (status == null) {
            return false;
        }

        return status.equals("PENDING")
                || status.equals("APPROVED")
                || status.equals("REJECTED")
                || status.equals("CANCELLED")
                || status.equals("COMPLETED");
    }
}
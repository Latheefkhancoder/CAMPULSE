package com.smartcampus.service;

import com.smartcampus.dao.BookingDAO;
import com.smartcampus.model.Booking;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();

    public enum BookingResult {
        SUCCESS,
        INVALID_DETAILS,
        RESOURCE_NOT_FOUND,
        RESOURCE_UNAVAILABLE,
        BOOKING_CONFLICT,
        DATABASE_ERROR
    }

    /**
     * Checks resource status and whether an active booking overlaps
     * the requested date/time. This method returns false if the database
     * cannot be checked.
     */
    public boolean isResourceAvailable(
            int resourceId,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime) {

        if (resourceId <= 0
                || bookingDate == null
                || startTime == null
                || endTime == null
                || !endTime.isAfter(startTime)
                || bookingDate.isBefore(LocalDate.now())
                || (bookingDate.isEqual(LocalDate.now())
                    && !startTime.isAfter(LocalTime.now()))) {
            return false;
        }

        String resourceSql = """
                SELECT availability_status
                FROM resources
                WHERE resource_id = ?
                """;

        String bookingSql = """
                SELECT COUNT(*)
                FROM bookings
                WHERE resource_id = ?
                  AND booking_date = ?
                  AND booking_status IN ('PENDING', 'APPROVED')
                  AND start_time < ?
                  AND end_time > ?
                """;

        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement(resourceSql)) {

                statement.setInt(1, resourceId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()
                            || !"AVAILABLE".equals(
                                    resultSet.getString("availability_status"))) {
                        return false;
                    }
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(bookingSql)) {

                statement.setInt(1, resourceId);
                statement.setDate(2, java.sql.Date.valueOf(bookingDate));
                statement.setTime(3, java.sql.Time.valueOf(endTime));
                statement.setTime(4, java.sql.Time.valueOf(startTime));

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt(1) == 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates a booking, checks resource availability and conflicts,
     * then asks BookingDAO to persist it.
     */
    public BookingResult createBookingWithResult(Booking booking) {
        if (booking == null
                || booking.getUserId() <= 0
                || booking.getResourceId() <= 0
                || booking.getBookingDate() == null
                || booking.getStartTime() == null
                || booking.getEndTime() == null
                || booking.getEventId() < 0
                || !booking.getEndTime().isAfter(booking.getStartTime())
                || booking.getBookingDate().isBefore(LocalDate.now())
                || (booking.getBookingDate().isEqual(LocalDate.now())
                    && !booking.getStartTime().isAfter(LocalTime.now()))) {
            return BookingResult.INVALID_DETAILS;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String resourceSql = """
                    SELECT availability_status
                    FROM resources
                    WHERE resource_id = ?
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(resourceSql)) {
                statement.setInt(1, booking.getResourceId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return BookingResult.RESOURCE_NOT_FOUND;
                    }

                    if (!"AVAILABLE".equals(
                            resultSet.getString("availability_status"))) {
                        return BookingResult.RESOURCE_UNAVAILABLE;
                    }
                }
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

            try (PreparedStatement statement =
                         connection.prepareStatement(conflictSql)) {
                statement.setInt(1, booking.getResourceId());
                statement.setDate(
                        2, java.sql.Date.valueOf(booking.getBookingDate()));
                statement.setTime(
                        3, java.sql.Time.valueOf(booking.getEndTime()));
                statement.setTime(
                        4, java.sql.Time.valueOf(booking.getStartTime()));

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return BookingResult.BOOKING_CONFLICT;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return BookingResult.DATABASE_ERROR;
        }

        try {
            booking.setBookingStatus("PENDING");
            return bookingDAO.createBooking(booking)
                    ? BookingResult.SUCCESS
                    : BookingResult.DATABASE_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return BookingResult.DATABASE_ERROR;
        }
    }

    public boolean createBooking(Booking booking) {
        return createBookingWithResult(booking) == BookingResult.SUCCESS;
    }

    public boolean completeBooking(int bookingId) {
        if (bookingId <= 0) {
            return false;
        }

        String sql = """
                UPDATE bookings
                SET booking_status = 'COMPLETED'
                WHERE booking_id = ?
                  AND booking_status = 'APPROVED'
                  AND TIMESTAMP(booking_date, end_time) < NOW()
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

    public boolean updatePendingBookingDetails(
            int bookingId,
            int resourceId,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime) {
        if (bookingId <= 0
                || resourceId <= 0
                || bookingDate == null
                || startTime == null
                || endTime == null
                || !endTime.isAfter(startTime)
                || bookingDate.isBefore(LocalDate.now())
                || (bookingDate.isEqual(LocalDate.now())
                    && !startTime.isAfter(LocalTime.now()))) {
            return false;
        }

        return bookingDAO.updatePendingBookingDetails(
                bookingId, resourceId, bookingDate, startTime, endTime);
    }
}

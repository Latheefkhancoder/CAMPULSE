package com.smartcampus.dao;

import com.smartcampus.model.EquipmentBooking;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipmentBookingDAO {

    public List<EquipmentBooking> getAllEquipmentBookings() {

        List<EquipmentBooking> bookings = new ArrayList<>();

        String sql = """
                SELECT equipment_booking_id, user_id, equipment_id,
                       quantity, issue_date, return_date, booking_status
                FROM equipment_bookings
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                EquipmentBooking booking = new EquipmentBooking(
                        resultSet.getInt("equipment_booking_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("equipment_id"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("issue_date").toLocalDate(),
                        resultSet.getDate("return_date") != null
                                ? resultSet.getDate("return_date").toLocalDate()
                                : null,
                        resultSet.getString("booking_status")
                );

                bookings.add(booking);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public boolean createEquipmentBooking(
            EquipmentBooking booking) {

        String sql = """
                INSERT INTO equipment_bookings
                (user_id, equipment_id, quantity,
                 issue_date, return_date, booking_status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, booking.getUserId());
            statement.setInt(2, booking.getEquipmentId());
            statement.setInt(3, booking.getQuantity());

            statement.setDate(
                    4,
                    java.sql.Date.valueOf(booking.getIssueDate())
            );

            if (booking.getReturnDate() != null) {
                statement.setDate(
                        5,
                        java.sql.Date.valueOf(booking.getReturnDate())
                );
            } else {
                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }

            statement.setString(
                    6,
                    booking.getBookingStatus()
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
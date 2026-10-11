package com.smartcampus.model;

import java.time.LocalDate;

public class EquipmentBooking {

    private int equipmentBookingId;
    private int userId;
    private int equipmentId;
    private int quantity;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String bookingStatus;

    public EquipmentBooking() {
    }

    public EquipmentBooking(
            int equipmentBookingId,
            int userId,
            int equipmentId,
            int quantity,
            LocalDate issueDate,
            LocalDate returnDate,
            String bookingStatus) {

        this.equipmentBookingId = equipmentBookingId;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.bookingStatus = bookingStatus;
    }

    public int getEquipmentBookingId() {
        return equipmentBookingId;
    }

    public void setEquipmentBookingId(int equipmentBookingId) {
        this.equipmentBookingId = equipmentBookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
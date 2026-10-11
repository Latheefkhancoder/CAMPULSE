package com.smartcampus.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {

    private int bookingId;
    private int userId;
    private int resourceId;
    private int eventId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String bookingStatus;

    public Booking() {
    }

    public Booking(int bookingId, int userId, int resourceId,
                   int eventId, LocalDate bookingDate,
                   LocalTime startTime, LocalTime endTime,
                   String bookingStatus) {

        this.bookingId = bookingId;
        this.userId = userId;
        this.resourceId = resourceId;
        this.eventId = eventId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
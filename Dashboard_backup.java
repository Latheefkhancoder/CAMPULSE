package com.smartcampus;

import java.time.LocalDate;
import java.time.LocalTime;

import com.smartcampus.dao.BookingDAO;
import com.smartcampus.dao.EquipmentDAO;
import com.smartcampus.dao.EventDAO;
import com.smartcampus.dao.ResourceDAO;
import com.smartcampus.model.Booking;
import com.smartcampus.model.User;
import com.smartcampus.service.BookingService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard {

    public void show(User user, Stage stage) {


        // =========================
        // TITLE
        // =========================

        Label title = new Label(
                "Smart Campus Dashboard"
        );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        Label welcome = new Label(
                "Welcome, " + user.getName()
        );

        welcome.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Label role = new Label(
                "Role: " + user.getRole()
        );


        // =========================
        // RESOURCES BUTTON
        // =========================

        Button resourcesButton =
                new Button("Resources");

        resourcesButton.setOnAction(event -> {

            ResourceDAO resourceDAO =
                    new ResourceDAO();

            var resources =
                    resourceDAO.getAllResources();

            StringBuilder data =
                    new StringBuilder();

            if (resources.isEmpty()) {

                data.append("No resources found.");

            } else {

                for (var resource : resources) {

                    data.append(
                            resource.getResourceName()
                    )
                    .append(" | ")
                    .append(
                            resource.getResourceType()
                    )
                    .append(" | ")
                    .append(
                            resource.getLocation()
                    )
                    .append(" | Capacity: ")
                    .append(
                            resource.getCapacity()
                    )
                    .append(" | ")
                    .append(
                            resource.getAvailabilityStatus()
                    )
                    .append("\n");
                }
            }

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Campus Resources"
            );

            alert.setHeaderText(
                    "Available Resources"
            );

            alert.setContentText(
                    data.toString()
            );

            alert.showAndWait();
        });


        // =========================
        // EVENTS BUTTON
        // =========================

        Button eventsButton =
                new Button("Events");

        eventsButton.setOnAction(event -> {

            EventDAO eventDAO =
                    new EventDAO();

            var events =
                    eventDAO.getAllEvents();

            StringBuilder data =
                    new StringBuilder();

            if (events.isEmpty()) {

                data.append("No events found.");

            } else {

                for (var eventItem : events) {

                    data.append(
                            eventItem.getEventName()
                    )
                    .append("\n")
                    .append("Date: ")
                    .append(
                            eventItem.getEventDate()
                    )
                    .append("\n")
                    .append("Time: ")
                    .append(
                            eventItem.getStartTime()
                    )
                    .append(" - ")
                    .append(
                            eventItem.getEndTime()
                    )
                    .append("\n")
                    .append("Description: ")
                    .append(
                            eventItem.getDescription()
                    )
                    .append("\n\n");
                }
            }

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Campus Events"
            );

            alert.setHeaderText(
                    "Upcoming Events"
            );

            alert.setContentText(
                    data.toString()
            );

            alert.showAndWait();
        });


        // =========================
        // BOOKINGS BUTTON
        // =========================

        Button bookingsButton =
                new Button("Bookings");

        bookingsButton.setOnAction(event -> {

            BookingDAO bookingDAO =
                    new BookingDAO();

            var bookings =
                    bookingDAO.getAllBookings();

            StringBuilder data =
                    new StringBuilder();

            if (bookings.isEmpty()) {

                data.append(
                        "No bookings found."
                );

            } else {

                for (var booking : bookings) {

                    data.append("Booking ID: ")
                        .append(
                                booking.getBookingId()
                        )
                        .append("\n")
                        .append("Resource ID: ")
                        .append(
                                booking.getResourceId()
                        )
                        .append("\n")
                        .append("Date: ")
                        .append(
                                booking.getBookingDate()
                        )
                        .append("\n")
                        .append("Time: ")
                        .append(
                                booking.getStartTime()
                        )
                        .append(" - ")
                        .append(
                                booking.getEndTime()
                        )
                        .append("\n")
                        .append("Status: ")
                        .append(
                                booking.getBookingStatus()
                        )
                        .append("\n\n");
                }
            }

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle("Bookings");

            alert.setHeaderText(
                    "Campus Bookings"
            );

            alert.setContentText(
                    data.toString()
            );

            alert.showAndWait();
        });


        // =========================
        // NEW BOOKING BUTTON
        // =========================

        Button newBookingButton =
                new Button("New Booking");

        newBookingButton.setOnAction(event -> {

            Stage bookingStage =
                    new Stage();

            Label heading =
                    new Label(
                            "Create New Booking"
                    );

            heading.setStyle(
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;"
            );

            TextField resourceIdField =
                    new TextField();

            resourceIdField.setPromptText(
                    "Enter Resource ID (e.g. 1)"
            );

            Label resourceIdLabel = new Label("Resource ID");

            TextField eventIdField =
                    new TextField();

            eventIdField.setPromptText(
                    "Event ID (optional)"
            );

            Label eventIdLabel = new Label("Event ID (optional)");

            DatePicker bookingDatePicker =
                    new DatePicker();

            TextField startTimeField =
                    new TextField();

            startTimeField.setPromptText(
                    "Start Time (HH:MM)"
            );

            TextField endTimeField =
                    new TextField();

            endTimeField.setPromptText(
                    "End Time (HH:MM)"
            );

            Button submitButton =
                    new Button(
                            "Book Resource"
                    );

            Label message =
                    new Label();
submitButton.setOnAction(e -> {

    try {
        // 1. Validate Resource ID
        String resourceText = resourceIdField.getText().trim();

        if (resourceText.isEmpty()) {
            message.setText("Please enter a Resource ID.");
            return;
        }

        int resourceId = Integer.parseInt(resourceText);

        if (resourceId <= 0) {
            message.setText("Resource ID must be positive.");
            return;
        }

        // 2. Validate Event ID
        String eventText = eventIdField.getText().trim();
        int eventId = 0;

        if (!eventText.isEmpty()) {
            eventId = Integer.parseInt(eventText);

            if (eventId <= 0) {
                message.setText(
                        "Event ID must be positive or left empty."
                );
                return;
            }
        }

        // 3. Validate booking date
        LocalDate bookingDate = bookingDatePicker.getValue();

        if (bookingDate == null) {
            message.setText("Please select a booking date.");
            return;
        }

        if (bookingDate.isBefore(LocalDate.now())) {
            message.setText("Past dates cannot be booked.");
            return;
        }

        // 4. Validate times
        LocalTime startTime =
                LocalTime.parse(startTimeField.getText().trim());

        LocalTime endTime =
                LocalTime.parse(endTimeField.getText().trim());

        if (!endTime.isAfter(startTime)) {
            message.setText(
                    "End time must be after start time."
            );
            return;
        }

        if (bookingDate.isEqual(LocalDate.now())
                && !startTime.isAfter(LocalTime.now())) {
            message.setText(
                    "Please select a future time for today's booking."
            );
            return;
        }

        // 5. Create booking
        Booking booking = new Booking(
                0,
                user.getUserId(),
                resourceId,
                eventId,
                bookingDate,
                startTime,
                endTime,
                "PENDING"
        );

        BookingService bookingService = new BookingService();

        BookingService.BookingResult result =
                bookingService.createBookingWithResult(booking);

        switch (result) {
            case SUCCESS:
                message.setText(
                        "Booking submitted successfully! Status: PENDING."
                );
                break;

            case INVALID_DETAILS:
                message.setText(
                        "Invalid booking details. Check the date and time."
                );
                break;

            case RESOURCE_NOT_FOUND:
                message.setText(
                        "The selected resource does not exist."
                );
                break;

            case RESOURCE_UNAVAILABLE:
                message.setText(
                        "This resource is unavailable or under maintenance."
                );
                break;

            case BOOKING_CONFLICT:
                message.setText(
                        "This resource is already booked for that time slot."
                );
                break;

            case DATABASE_ERROR:
                message.setText(
                        "Booking failed due to a database or booking error."
                );
                break;
        }

    } catch (NumberFormatException ex) {
        message.setText(
                "Resource ID and Event ID must be valid numbers."
        );

    } catch (java.time.format.DateTimeParseException ex) {
        message.setText(
                "Invalid time format. Use HH:MM, e.g. 14:30."
        );

    } catch (Exception ex) {
        ex.printStackTrace();
        message.setText(
                "An unexpected error occurred while creating the booking."
        );
    }
});

            VBox bookingRoot =
                    new VBox(
                            15,
                            heading,
                            resourceIdLabel,
                            resourceIdField,
                            eventIdLabel,
                            eventIdField,
                            new Label(
                                    "Booking Date"
                            ),
                            bookingDatePicker,
                            startTimeField,
                            endTimeField,
                            submitButton,
                            message
                    );

            bookingRoot.setAlignment(
                    Pos.CENTER
            );

            bookingRoot.setPadding(
                    new Insets(30)
            );

            Scene bookingScene =
                    new Scene(
                            bookingRoot,
                            450,
                            550
                    );

            bookingStage.setTitle(
                    "New Booking"
            );

            bookingStage.setScene(
                    bookingScene
            );

            bookingStage.show();
        });


        // =========================
        // EQUIPMENT BUTTON
        // =========================

        Button equipmentButton =
                new Button("Equipment");

        equipmentButton.setOnAction(event -> {

            EquipmentDAO equipmentDAO =
                    new EquipmentDAO();

            var equipmentList =
                    equipmentDAO.getAllEquipment();

            StringBuilder data =
                    new StringBuilder();

            if (equipmentList.isEmpty()) {

                data.append(
                        "No equipment found."
                );

            } else {

                for (var equipment :
                        equipmentList) {

                    data.append("Equipment: ")
                        .append(
                                equipment
                                        .getEquipmentName()
                        )
                        .append("\n")
                        .append("Category: ")
                        .append(
                                equipment.getCategory()
                        )
                        .append("\n")
                        .append("Total Quantity: ")
                        .append(
                                equipment.getQuantity()
                        )
                        .append("\n")
                        .append("Available: ")
                        .append(
                                equipment
                                        .getAvailableQuantity()
                        )
                        .append("\n")
                        .append("Condition: ")
                        .append(
                                equipment
                                        .getConditionStatus()
                        )
                        .append("\n\n");
                }
            }

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Equipment"
            );

            alert.setHeaderText(
                    "Campus Equipment"
            );

            alert.setContentText(
                    data.toString()
            );

            alert.showAndWait();
        });


        // =========================
        // LOGOUT BUTTON
        // =========================

        Button logoutButton =
                new Button("Logout");

        logoutButton.setOnAction(
                event -> stage.close()
        );


        // =========================
        // BUTTON SIZES
        // =========================

        resourcesButton.setPrefWidth(220);
        eventsButton.setPrefWidth(220);
        bookingsButton.setPrefWidth(220);
        newBookingButton.setPrefWidth(220);
        equipmentButton.setPrefWidth(220);
        logoutButton.setPrefWidth(220);


        // =========================
        // DASHBOARD LAYOUT
        // =========================

        VBox root =
                new VBox(
                        15,
                        title,
                        welcome,
                        role,
                        resourcesButton,
                        eventsButton,
                        bookingsButton,
                        newBookingButton,
                        equipmentButton,
                        logoutButton
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(40)
        );

        root.setStyle(
                "-fx-background-color: #f5f7fa;"
        );


        // =========================
        // SCENE
        // =========================

        Scene scene =
                new Scene(
                        root,
                        600,
                        600
                );

        stage.setTitle(
                "Smart Campus - Dashboard"
        );

        stage.setScene(
                scene
        );

        stage.show();
    }
}
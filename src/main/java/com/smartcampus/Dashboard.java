package com.smartcampus;

import java.time.LocalDate;
import java.time.LocalTime;

import com.smartcampus.dao.BookingDAO;
import com.smartcampus.dao.EquipmentDAO;
import com.smartcampus.dao.EventDAO;
import com.smartcampus.dao.ResourceDAO;
import com.smartcampus.model.Booking;
import com.smartcampus.model.Event;
import com.smartcampus.model.Resource;
import com.smartcampus.model.User;
import com.smartcampus.service.BookingService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Dashboard {

    private BorderPane mainLayout;
    private StackPane contentArea;

    public void show(User user, Stage stage) {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f4f7fb;");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(28));
        contentArea.setStyle("-fx-background-color: #f4f7fb;");

        Label appName = new Label("SMART CAMPUS");
        appName.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label userName = new Label(user.getName());
        userName.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label userRole = new Label("Faculty Dashboard");
        userRole.setStyle("-fx-font-size: 12px; -fx-text-fill: #cbd5e1;");

        VBox profile = new VBox(4, userName, userRole);
        profile.setPadding(new Insets(20, 0, 20, 0));

        Button homeButton = navButton("Dashboard");
        Button resourcesButton = navButton("Resources");
        Button eventsButton = navButton("Events");
        Button bookingsButton = navButton("My Bookings");
        Button newBookingButton = navButton("Create Booking");
        Button equipmentButton = navButton("Equipment");
        Button logoutButton = navButton("Logout");

        homeButton.setOnAction(e -> showPage(buildHomePage(user)));
        resourcesButton.setOnAction(e -> showPage(buildResourcesPage()));
        eventsButton.setOnAction(e -> showPage(buildEventsPage()));
        bookingsButton.setOnAction(e -> showPage(buildBookingsPage(user)));
        newBookingButton.setOnAction(e -> showPage(buildNewBookingPage(user)));
        equipmentButton.setOnAction(e -> showPage(buildEquipmentPage()));
        logoutButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText("Log out of Smart Campus?");
            confirm.setContentText("You will return to the login screen the next time you launch the application.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    stage.close();
                }
            });
        });

        VBox sidebar = new VBox(12, appName, profile, homeButton, resourcesButton,
                eventsButton, bookingsButton, newBookingButton, equipmentButton, logoutButton);
        sidebar.setPadding(new Insets(28, 18, 24, 18));
        sidebar.setPrefWidth(225);
        sidebar.setStyle("-fx-background-color: #172554;");

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);
        showPage(buildHomePage(user));

        Scene scene = new Scene(mainLayout, 1100, 720);
        stage.setTitle("Smart Campus - Faculty Dashboard");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    private Button navButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(12, 14, 12, 14));
        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-cursor: hand;"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-cursor: hand;"));
        return button;
    }

    private void showPage(Node page) {
        contentArea.getChildren().setAll(page);
    }

    private VBox pageContainer(String headingText, String subtitleText) {
        Label heading = new Label(headingText);
        heading.setStyle("-fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #172554;");
        Label subtitle = new Label(subtitleText);
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
        VBox page = new VBox(8, heading, subtitle);
        page.setPadding(new Insets(4));
        page.setFillWidth(true);
        return page;
    }

    private VBox card(String title, String description) {
        Label heading = new Label(title);
        heading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label body = new Label(description);
        body.setWrapText(true);
        body.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
        VBox box = new VBox(10, heading, body);
        box.setPadding(new Insets(20));
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #e2e8f0; -fx-border-radius: 12px;");
        return box;
    }

    private VBox summaryCard(String title, String count, String description, Runnable action) {
        Label heading = new Label(title);
        heading.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label number = new Label(count);
        number.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1d4ed8;");
        Label body = new Label(description);
        body.setWrapText(true);
        body.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
        VBox box = new VBox(10, heading, number, body);
        box.setPadding(new Insets(22));
        box.setMinWidth(170);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #dbe4f0; -fx-border-radius: 12px; -fx-cursor: hand;");
        box.setOnMouseClicked(e -> action.run());
        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color: #eff6ff; -fx-background-radius: 12px; -fx-border-color: #93c5fd; -fx-border-radius: 12px; -fx-cursor: hand;"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #dbe4f0; -fx-border-radius: 12px; -fx-cursor: hand;"));
        return box;
    }

    private Node buildHomePage(User user) {
        VBox page = pageContainer("Welcome, " + user.getName(), "Manage campus resources, events and your bookings from one place.");
        int resourceCount = 0;
        int eventCount = 0;
        int bookingCount = 0;
        try {
            var resources = new ResourceDAO().getAllResources();
            resourceCount = resources == null ? 0 : resources.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            var events = new EventDAO().getAllEvents();
            eventCount = events == null ? 0 : events.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            var bookings = new BookingDAO().getAllBookings();
            if (bookings != null) {
                for (Booking booking : bookings) {
                    if (booking.getUserId() == user.getUserId()) bookingCount++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VBox resourcesCard = summaryCard("Resources", String.valueOf(resourceCount),
                "Registered campus venues and resources", () -> showPage(buildResourcesPage()));
        VBox eventsCard = summaryCard("Events", String.valueOf(eventCount),
                "Scheduled campus events", () -> showPage(buildEventsPage()));
        VBox bookingsCard = summaryCard("My Bookings", String.valueOf(bookingCount),
                "Your booking requests", () -> showPage(buildBookingsPage(user)));
        HBox cards = new HBox(16, resourcesCard, eventsCard, bookingsCard);
        cards.setAlignment(Pos.TOP_LEFT);
        cards.setFillHeight(true);
        resourcesCard.setMaxWidth(Double.MAX_VALUE);
        eventsCard.setMaxWidth(Double.MAX_VALUE);
        bookingsCard.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(resourcesCard, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(eventsCard, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(bookingsCard, javafx.scene.layout.Priority.ALWAYS);
        VBox.setMargin(cards, new Insets(22, 0, 0, 0));

        Label section = new Label("Quick Overview");
        section.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #172554;");
        Label note = new Label("Select a summary card or use the navigation menu to open a module. All pages stay in this window.");
        note.setWrapText(true);
        note.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569;");
        page.getChildren().addAll(section, cards, note);
        return wrapScrollable(page);
    }

    private Node buildResourcesPage() {
        VBox page = pageContainer("Campus Resources", "Venue and resource availability details.");
        StringBuilder data = new StringBuilder();
        try {
            var resources = new ResourceDAO().getAllResources();
            if (resources == null || resources.isEmpty()) {
                data.append("No resources found.");
            } else {
                for (var resource : resources) {
                    data.append("Resource: ").append(resource.getResourceName()).append('\n')
                        .append("Type: ").append(resource.getResourceType()).append('\n')
                        .append("Location: ").append(resource.getLocation()).append('\n')
                        .append("Capacity: ").append(resource.getCapacity()).append('\n')
                        .append("Availability: ").append(resource.getAvailabilityStatus()).append("\n\n");
                }
            }
        } catch (Exception ex) {
            data.append("Could not load resources. Please check the database connection.");
            ex.printStackTrace();
        }
        page.getChildren().add(card("Resource List", data.toString()));
        return wrapScrollable(page);
    }

    private Node buildEventsPage() {
        VBox page = pageContainer("Campus Events", "Review scheduled campus events.");
        StringBuilder data = new StringBuilder();
        try {
            var events = new EventDAO().getAllEvents();
            if (events == null || events.isEmpty()) {
                data.append("No events found.");
            } else {
                for (var item : events) {
                    data.append(item.getEventName()).append('\n')
                        .append("Date: ").append(item.getEventDate()).append('\n')
                        .append("Time: ").append(item.getStartTime()).append(" - ").append(item.getEndTime()).append('\n')
                        .append("Description: ").append(item.getDescription()).append("\n\n");
                }
            }
        } catch (Exception ex) {
            data.append("Could not load events. Please check the database connection.");
            ex.printStackTrace();
        }
        page.getChildren().add(card("Event List", data.toString()));
        return wrapScrollable(page);
    }

    private Node buildBookingsPage(User user) {
        VBox page = pageContainer("My Bookings", "Booking requests submitted by your account.");
        StringBuilder data = new StringBuilder();
        try {
            var bookings = new BookingDAO().getAllBookings();
            boolean found = false;
            if (bookings != null) {
                for (Booking booking : bookings) {
                    if (booking.getUserId() == user.getUserId()) {
                        found = true;
                        data.append("Booking ID: ").append(booking.getBookingId()).append('\n')
                            .append("Resource ID: ").append(booking.getResourceId()).append('\n')
                            .append("Date: ").append(booking.getBookingDate()).append('\n')
                            .append("Time: ").append(booking.getStartTime()).append(" - ").append(booking.getEndTime()).append('\n')
                            .append("Status: ").append(booking.getBookingStatus()).append("\n\n");
                    }
                }
            }
            if (!found) data.append("You have not submitted any bookings yet.");
        } catch (Exception ex) {
            data.append("Could not load bookings. Please check the database connection.");
            ex.printStackTrace();
        }
        page.getChildren().add(card("Your Booking Requests", data.toString()));
        return wrapScrollable(page);
    }

    private Node buildEquipmentPage() {
        VBox page = pageContainer("Campus Equipment", "View equipment inventory.");
        StringBuilder data = new StringBuilder();
        try {
            var items = new EquipmentDAO().getAllEquipment();
            if (items == null || items.isEmpty()) {
                data.append("No equipment found.");
            } else {
                for (var item : items) {
                    data.append("Equipment: ").append(item.getEquipmentName()).append('\n')
                        .append("Category: ").append(item.getCategory()).append('\n')
                        .append("Total quantity: ").append(item.getQuantity()).append('\n')
                        .append("Available: ").append(item.getAvailableQuantity()).append('\n')
                        .append("Condition: ").append(item.getConditionStatus()).append("\n\n");
                }
            }
        } catch (Exception ex) {
            data.append("Could not load equipment. Please check the database connection.");
            ex.printStackTrace();
        }
        page.getChildren().add(card("Equipment Inventory", data.toString()));
        return wrapScrollable(page);
    }

    private Node buildNewBookingPage(User user) {
        VBox page = pageContainer(
                "Create a Booking",
                "Enter the event details, choose a venue and submit a request for admin approval."
        );

        TextField eventNameField = new TextField();
        eventNameField.setPromptText("e.g. AI Innovation Summit");

        ComboBox<String> departmentBox = new ComboBox<>();
        departmentBox.getItems().addAll(
                "AI & DS", "CSE", "IT", "ECE", "EEE", "Mechanical",
                "Civil", "MBA", "MCA", "Other"
        );
        departmentBox.setPromptText("Select organizing department");
        departmentBox.setMaxWidth(Double.MAX_VALUE);

        TextField organizedByField = new TextField();
        organizedByField.setPromptText("e.g. Rajalakshmi Engineering College");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Choose event date");

        ComboBox<Resource> venueBox = new ComboBox<>();
        venueBox.setPromptText("Select venue");
        venueBox.setMaxWidth(Double.MAX_VALUE);
        venueBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Resource resource) {
                if (resource == null) return "";
                return resource.getResourceName() + " — " + resource.getLocation()
                        + " (Capacity: " + resource.getCapacity() + ")";
            }

            @Override
            public Resource fromString(String string) {
                return null;
            }
        });

        Label venueHint = new Label();
        venueHint.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        try {
            var resources = new ResourceDAO().getAllResources();
            if (resources != null) {
                for (Resource resource : resources) {
                    if ("AVAILABLE".equalsIgnoreCase(resource.getAvailabilityStatus())) {
                        venueBox.getItems().add(resource);
                    }
                }
            }
            if (venueBox.getItems().isEmpty()) {
                venueHint.setText("No available venues found. Ask an administrator to check resource availability.");
            } else {
                venueHint.setText(venueBox.getItems().size() + " available venue(s)");
            }
        } catch (Exception ex) {
            venueHint.setText("Could not load venues. Check the database connection.");
            ex.printStackTrace();
        }

        ComboBox<Integer> startHour = hourBox();
        ComboBox<String> startMinute = minuteBox();
        ComboBox<String> startAmPm = amPmBox();
        HBox startTimeControls = new HBox(8, startHour, startMinute, startAmPm);
        startTimeControls.setAlignment(Pos.CENTER_LEFT);

        ComboBox<Integer> endHour = hourBox();
        ComboBox<String> endMinute = minuteBox();
        ComboBox<String> endAmPm = amPmBox();
        HBox endTimeControls = new HBox(8, endHour, endMinute, endAmPm);
        endTimeControls.setAlignment(Pos.CENTER_LEFT);

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Briefly describe the event and its requirements");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);

        Label message = new Label();
        message.setWrapText(true);
        message.setStyle("-fx-text-fill: #b91c1c; -fx-font-size: 13px;");

        Button submit = new Button("Submit Booking Request");
        submit.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 20 12 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        GridPane fields = new GridPane();
        fields.setHgap(22);
        fields.setVgap(16);
        fields.add(fieldGroup("Event Name *", eventNameField), 0, 0);
        fields.add(fieldGroup("Organizing Department *", departmentBox), 1, 0);
        fields.add(fieldGroup("Organized By *", organizedByField), 0, 1);
        fields.add(fieldGroup("Event Date *", datePicker), 1, 1);
        fields.add(fieldGroup("Start Time *", startTimeControls), 0, 2);
        fields.add(fieldGroup("End Time *", endTimeControls), 1, 2);
        fields.add(fieldGroup("Venue *", venueBox), 0, 3, 2, 1);
        fields.add(venueHint, 0, 4, 2, 1);
        fields.add(fieldGroup("Description *", descriptionArea), 0, 5, 2, 1);
        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        fields.getColumnConstraints().addAll(col1, col2);

        submit.setOnAction(e -> {
            try {
                String eventName = eventNameField.getText() == null ? "" : eventNameField.getText().trim();
                String department = departmentBox.getValue();
                String organizedBy = organizedByField.getText() == null ? "" : organizedByField.getText().trim();
                String description = descriptionArea.getText() == null ? "" : descriptionArea.getText().trim();
                LocalDate eventDate = datePicker.getValue();
                Resource selectedVenue = venueBox.getValue();

                if (eventName.isEmpty() || department == null || organizedBy.isEmpty()
                        || eventDate == null || selectedVenue == null || description.isEmpty()) {
                    message.setStyle("-fx-text-fill: #b91c1c;");
                    message.setText("Please complete all required fields marked with *.");
                    return;
                }
                if (eventDate.isBefore(LocalDate.now())) {
                    message.setText("Past dates cannot be booked.");
                    return;
                }
                if (startHour.getValue() == null || startMinute.getValue() == null || startAmPm.getValue() == null
                        || endHour.getValue() == null || endMinute.getValue() == null || endAmPm.getValue() == null) {
                    message.setText("Please select both start and end times.");
                    return;
                }

                LocalTime startTime = toLocalTime(startHour.getValue(), startMinute.getValue(), startAmPm.getValue());
                LocalTime endTime = toLocalTime(endHour.getValue(), endMinute.getValue(), endAmPm.getValue());

                if (!endTime.isAfter(startTime)) {
                    message.setText("End time must be later than start time.");
                    return;
                }
                if (eventDate.isEqual(LocalDate.now()) && !startTime.isAfter(LocalTime.now())) {
                    message.setText("Choose a future start time for today's booking.");
                    return;
                }

                Event event = new Event();
                event.setEventName(eventName);
                event.setDescription(description);
                event.setEventDate(eventDate);
                event.setStartTime(startTime);
                event.setEndTime(endTime);
                event.setOrganizerId(user.getUserId());
                event.setOrganizingDepartment(department);
                event.setOrganizedBy(organizedBy);

                EventDAO eventDAO = new EventDAO();
                int eventId = eventDAO.addEventAndReturnId(event);
                if (eventId <= 0) {
                    message.setText("Could not save the event. No booking was submitted.");
                    return;
                }

                Booking booking = new Booking(
                        0, user.getUserId(), selectedVenue.getResourceId(), eventId,
                        eventDate, startTime, endTime, "PENDING"
                );

                BookingService.BookingResult result =
                        new BookingService().createBookingWithResult(booking);

                if (result == BookingService.BookingResult.SUCCESS) {
                    message.setStyle("-fx-text-fill: #15803d; -fx-font-weight: bold;");
                    message.setText("Request submitted successfully! Event ID: " + eventId
                            + ". Venue: " + selectedVenue.getResourceName()
                            + ". Status: PENDING.");
                    eventNameField.clear();
                    departmentBox.getSelectionModel().clearSelection();
                    organizedByField.clear();
                    datePicker.setValue(null);
                    venueBox.getSelectionModel().clearSelection();
                    startHour.getSelectionModel().clearSelection();
                    startMinute.getSelectionModel().clearSelection();
                    startAmPm.getSelectionModel().clearSelection();
                    endHour.getSelectionModel().clearSelection();
                    endMinute.getSelectionModel().clearSelection();
                    endAmPm.getSelectionModel().clearSelection();
                    descriptionArea.clear();
                } else {
                    message.setStyle("-fx-text-fill: #b91c1c;");
                    switch (result) {
                        case INVALID_DETAILS -> message.setText("Invalid date or time. Please review the form.");
                        case RESOURCE_NOT_FOUND -> message.setText("The selected venue no longer exists.");
                        case RESOURCE_UNAVAILABLE -> message.setText("This venue is unavailable or under maintenance.");
                        case BOOKING_CONFLICT -> message.setText("This venue already has a pending or approved booking during that time.");
                        case DATABASE_ERROR -> message.setText("The event was saved, but the booking could not be created. Check Events before retrying to avoid duplicate events.");
                        default -> message.setText("The booking request could not be submitted.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.setStyle("-fx-text-fill: #b91c1c;");
                message.setText("Could not submit the request: " + ex.getMessage());
            }
        });

        VBox form = new VBox(18, fields, submit, message);
        form.setPadding(new Insets(28));
        form.setMaxWidth(900);
        form.setPrefWidth(900);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 14px; -fx-border-color: #dbe4f0; -fx-border-radius: 14px;");

        HBox formWrapper = new HBox(form);
        formWrapper.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(formWrapper, new Insets(18, 0, 0, 0));
        page.getChildren().add(formWrapper);
        return wrapScrollable(page);
    }

    private ComboBox<Integer> hourBox() {
        ComboBox<Integer> box = new ComboBox<>();
        for (int hour = 1; hour <= 12; hour++) box.getItems().add(hour);
        box.setPromptText("Hour");
        box.setMaxWidth(Double.MAX_VALUE);
        box.setPrefWidth(100);
        return box;
    }

    private ComboBox<String> minuteBox() {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll("00", "15", "30", "45");
        box.setPromptText("Min");
        box.setPrefWidth(100);
        return box;
    }

    private ComboBox<String> amPmBox() {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll("AM", "PM");
        box.setPromptText("AM/PM");
        box.setPrefWidth(100);
        return box;
    }

    private LocalTime toLocalTime(int hour, String minute, String amPm) {
        int hour24 = hour % 12;
        if ("PM".equals(amPm)) hour24 += 12;
        return LocalTime.of(hour24, Integer.parseInt(minute));
    }

    private VBox fieldGroup(String labelText, Node input) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");
        if (input instanceof TextField) {
            ((TextField) input).setMaxWidth(Double.MAX_VALUE);
        }
        if (input instanceof DatePicker) {
            ((DatePicker) input).setMaxWidth(Double.MAX_VALUE);
        }
        if (input instanceof ComboBox<?>) {
            ((ComboBox<?>) input).setMaxWidth(Double.MAX_VALUE);
        }
        if (input instanceof TextArea) {
            ((TextArea) input).setMaxWidth(Double.MAX_VALUE);
        }
        VBox group = new VBox(6, label, input);
        return group;
    }

    private Node wrapScrollable(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #f4f7fb; -fx-background-color: #f4f7fb;");
        return scrollPane;
    }
}

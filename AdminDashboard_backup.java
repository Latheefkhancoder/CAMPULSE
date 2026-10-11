package com.smartcampus;

import com.smartcampus.dao.BookingDAO;
import com.smartcampus.dao.EquipmentDAO;
import com.smartcampus.dao.EventDAO;
import com.smartcampus.dao.ResourceDAO;
import com.smartcampus.model.Booking;
import com.smartcampus.model.Equipment;
import com.smartcampus.model.Event;
import com.smartcampus.model.Resource;
import com.smartcampus.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AdminDashboard {

    public void show(User user, Stage stage) {


        // ====================================
        // TITLE
        // ====================================

        Label title = new Label(
                "Smart Campus - Admin Dashboard"
        );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        // ====================================
        // WELCOME
        // ====================================

        Label welcome = new Label(
                "Welcome, " + user.getName()
        );

        welcome.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        // ====================================
        // MANAGE RESOURCES
        // ====================================

        Button manageResourcesButton =
                new Button("Manage Resources");

        manageResourcesButton.setOnAction(action -> {

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Manage Resources"
            );

            alert.setHeaderText(
                    "Resource Management"
            );

            ButtonType addButton =
                    new ButtonType("Add Resource");

            ButtonType editButton =
                    new ButtonType("Edit Resource");

            ButtonType deleteButton =
                    new ButtonType("Delete Resource");

            ButtonType viewButton =
                    new ButtonType("View Resources");

            ButtonType closeButton =
                    new ButtonType("Close");

            alert.getButtonTypes().setAll(
                    addButton,
                    editButton,
                    deleteButton,
                    viewButton,
                    closeButton
            );

            var result =
                    alert.showAndWait();

            if (result.isEmpty()) {
                return;
            }

            if (result.get() == addButton) {

                showAddResourceWindow();

            } else if (result.get() == editButton) {

                showEditResourceWindow();

            } else if (result.get() == deleteButton) {

                showDeleteResourceWindow();

            } else if (result.get() == viewButton) {

                showResources();
            }
        });

        // ====================================
        // MANAGE EVENTS
        // ====================================

        Button manageEventsButton =
                new Button("Manage Events");

        manageEventsButton.setOnAction(action -> {

            showEventManagement();
        });

        // ====================================
        // MANAGE BOOKINGS
        // ====================================

        Button manageBookingsButton = new Button("Manage Bookings");

        manageBookingsButton.setOnAction(action -> showBookingManagement());

        // ====================================
        // MANAGE EQUIPMENT
        // ====================================

        Button manageEquipmentButton =
                new Button("Manage Equipment");

        manageEquipmentButton.setOnAction(action -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Manage Equipment");
            alert.setHeaderText("Equipment Management");

            ButtonType addButton = new ButtonType("Add Equipment");
            ButtonType editButton = new ButtonType("Edit Equipment");
            ButtonType deleteButton = new ButtonType("Delete Equipment");
            ButtonType viewButton = new ButtonType("View Equipment");
            ButtonType closeButton = new ButtonType("Close");

            alert.getButtonTypes().setAll(addButton, editButton, deleteButton, viewButton, closeButton);

            var result = alert.showAndWait();
            if (result.isEmpty()) return;

            if (result.get() == addButton) showAddEquipmentWindow();
            else if (result.get() == editButton) showEditEquipmentWindow();
            else if (result.get() == deleteButton) showDeleteEquipmentWindow();
            else if (result.get() == viewButton) showEquipment();
        });

        // ====================================
        // LOGOUT
        // ====================================

        Button logoutButton =
                new Button("Logout");

        logoutButton.setOnAction(
                action -> stage.close()
        );

        // ====================================
        // BUTTON WIDTH
        // ====================================

        manageResourcesButton.setPrefWidth(240);
        manageEventsButton.setPrefWidth(240);
        manageBookingsButton.setPrefWidth(240);
        manageEquipmentButton.setPrefWidth(240);
        logoutButton.setPrefWidth(240);

        // ====================================
        // ROOT
        // ====================================

        VBox root =
                new VBox(
                        15,
                        title,
                        welcome,
                        manageResourcesButton,
                        manageEventsButton,
                        manageBookingsButton,
                        manageEquipmentButton,
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

        // ====================================
        // SCENE
        // ====================================

        Scene scene =
                new Scene(
                        root,
                        600,
                        550
                );

        stage.setTitle(
                "Smart Campus - Admin Dashboard"
        );

        stage.setScene(scene);

        stage.show();
    }

    // ====================================
    // BOOKING MANAGEMENT TABLE
    // ====================================

    private void showBookingManagement() {

        BookingDAO bookingDAO = new BookingDAO();

        Stage bookingStage = new Stage();
        bookingStage.setTitle("Manage Campus Bookings");

        Label heading = new Label("Campus Booking Management");
        heading.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll(
                "ALL", "PENDING", "APPROVED", "REJECTED",
                "CANCELLED", "COMPLETED"
        );
        statusFilter.setValue("ALL");
        statusFilter.setPromptText("Filter by status");

        TableView<Booking> table = new TableView<>();
table.setColumnResizePolicy(
        TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
);

        TableColumn<Booking, Integer> bookingIdColumn =
                new TableColumn<>("Booking ID");
        bookingIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookingId"));

        TableColumn<Booking, Integer> userIdColumn =
                new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("userId"));

        TableColumn<Booking, Integer> resourceIdColumn =
                new TableColumn<>("Resource ID");
        resourceIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("resourceId"));

        TableColumn<Booking, LocalDate> dateColumn =
                new TableColumn<>("Date");
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookingDate"));

        TableColumn<Booking, LocalTime> startColumn =
                new TableColumn<>("Start");
        startColumn.setCellValueFactory(
                new PropertyValueFactory<>("startTime"));

        TableColumn<Booking, LocalTime> endColumn =
                new TableColumn<>("End");
        endColumn.setCellValueFactory(
                new PropertyValueFactory<>("endTime"));

        TableColumn<Booking, String> statusColumn =
                new TableColumn<>("Status");
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookingStatus"));

        table.getColumns().add(bookingIdColumn);
        table.getColumns().add(userIdColumn);
        table.getColumns().add(resourceIdColumn);
        table.getColumns().add(dateColumn);
        table.getColumns().add(startColumn);
        table.getColumns().add(endColumn);
        table.getColumns().add(statusColumn);

        ObservableList<Booking> tableData =
                FXCollections.observableArrayList();
        table.setItems(tableData);

        Runnable refreshTable = () -> {
            String selectedStatus = statusFilter.getValue();
            tableData.setAll(
                    bookingDAO.getAllBookings().stream()
                            .filter(booking ->
                                    selectedStatus == null
                                    || selectedStatus.equals("ALL")
                                    || selectedStatus.equalsIgnoreCase(
                                            booking.getBookingStatus()))
                            .toList()
            );
        };

        statusFilter.setOnAction(event -> refreshTable.run());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshTable.run());

        Button approveButton = new Button("Approve");
        Button rejectButton = new Button("Reject");
        Button cancelButton = new Button("Cancel Booking");
        Button completeButton = new Button("Complete");
        Button closeButton = new Button("Close");

        java.util.function.Consumer<String> updateSelectedBooking = requestedStatus -> {
            Booking selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showMessage("Please select a booking from the table first.");
                return;
            }

            String currentStatus = selected.getBookingStatus();

            boolean validTransition =
                    ("PENDING".equalsIgnoreCase(currentStatus)
                            && ("APPROVED".equals(requestedStatus)
                            || "REJECTED".equals(requestedStatus)
                            || "CANCELLED".equals(requestedStatus)))
                    || ("APPROVED".equalsIgnoreCase(currentStatus)
                            && "CANCELLED".equals(requestedStatus))
                    || ("APPROVED".equalsIgnoreCase(currentStatus)
                            && "COMPLETED".equals(requestedStatus)
                            && !LocalDateTime.of(
                                    selected.getBookingDate(),
                                    selected.getEndTime())
                                    .isAfter(LocalDateTime.now()));

            if (!validTransition) {
                if ("COMPLETED".equals(requestedStatus)
                        && "APPROVED".equalsIgnoreCase(currentStatus)) {
                    showMessage("This booking cannot be completed before its scheduled end time.");
                } else {
                    showMessage("Cannot change booking status from "
                            + currentStatus + " to " + requestedStatus + ".");
                }
                return;
            }

            boolean success = bookingDAO.updateBookingStatus(
                    selected.getBookingId(), requestedStatus);

            if (success) {
                showMessage("Booking #" + selected.getBookingId()
                        + " status updated to " + requestedStatus + ".");
            } else {
                showMessage("Status update failed. The booking may have changed, "
                        + "or the requested transition is not allowed.");
            }

            refreshTable.run();
        };

        approveButton.setOnAction(event ->
                updateSelectedBooking.accept("APPROVED"));
        rejectButton.setOnAction(event ->
                updateSelectedBooking.accept("REJECTED"));
        cancelButton.setOnAction(event ->
                updateSelectedBooking.accept("CANCELLED"));
        completeButton.setOnAction(event ->
                updateSelectedBooking.accept("COMPLETED"));
        closeButton.setOnAction(event -> bookingStage.close());

        HBox filterRow = new HBox(10,
                new Label("Status:"), statusFilter, refreshButton);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        HBox actionRow = new HBox(10,
                approveButton, rejectButton, cancelButton,
                completeButton, closeButton);
        actionRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, heading, filterRow, table, actionRow);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f7fa;");

        bookingStage.setScene(new Scene(root, 950, 600));
        refreshTable.run();
        bookingStage.show();
    }

    // ====================================
    // EQUIPMENT METHODS
    // ====================================

    private void showEquipment() {
        EquipmentDAO dao = new EquipmentDAO();
        var list = dao.getAllEquipment();
        StringBuilder data = new StringBuilder();

        if (list.isEmpty()) {
            data.append("No equipment found.");
        } else {
            for (var equipmentItem : list) {
                data.append("Equipment ID: ").append(equipmentItem.getEquipmentId())
                    .append("\nName: ").append(equipmentItem.getEquipmentName())
                    .append("\nCategory: ").append(equipmentItem.getCategory())
                    .append("\nTotal Quantity: ").append(equipmentItem.getQuantity())
                    .append("\nAvailable Quantity: ").append(equipmentItem.getAvailableQuantity())
                    .append("\nCondition: ").append(equipmentItem.getConditionStatus())
                    .append("\n\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Campus Equipment");
        alert.setHeaderText("All Equipment");
        alert.setContentText(data.toString());
        alert.showAndWait();
    }

    private void showAddEquipmentWindow() {
        Stage stage = new Stage();
        Label heading = new Label("Add New Equipment");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField(); nameField.setPromptText("Equipment Name");
        TextField categoryField = new TextField(); categoryField.setPromptText("Category");
        TextField quantityField = new TextField(); quantityField.setPromptText("Total Quantity");
        TextField availableField = new TextField(); availableField.setPromptText("Available Quantity");
        TextField conditionField = new TextField(); conditionField.setPromptText("GOOD / DAMAGED / UNDER_MAINTENANCE");
        Button addButton = new Button("Add Equipment");
        Label message = new Label();

        addButton.setOnAction(action -> {
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int available = Integer.parseInt(availableField.getText().trim());
                String condition = conditionField.getText().trim().toUpperCase();

                if (name.isEmpty() || category.isEmpty() || condition.isEmpty()) { message.setText("Please fill all fields."); return; }
                if (quantity <= 0) { message.setText("Total quantity must be greater than 0."); return; }
                if (available < 0 || available > quantity) { message.setText("Available quantity must be between 0 and total quantity."); return; }
                if (!isValidEquipmentCondition(condition)) { message.setText("Invalid condition status."); return; }

                Equipment equipment = new Equipment(0, name, category, quantity, available, condition);
                boolean success = new EquipmentDAO().addEquipment(equipment);
                message.setText(success ? "Equipment added successfully!" : "Failed to add equipment.");
                if (success) { nameField.clear(); categoryField.clear(); quantityField.clear(); availableField.clear(); conditionField.clear(); }
            } catch (NumberFormatException e) { message.setText("Quantity must be valid numbers."); }
        });

        VBox root = new VBox(15, heading, nameField, categoryField, quantityField, availableField, conditionField, addButton, message);
        root.setAlignment(Pos.CENTER); root.setPadding(new Insets(30));
        stage.setTitle("Add Equipment"); stage.setScene(new Scene(root, 500, 550)); stage.show();
    }

    private void showEditEquipmentWindow() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Equipment"); dialog.setHeaderText("Enter Equipment ID"); dialog.setContentText("Equipment ID:");
        var result = dialog.showAndWait();
        if (result.isEmpty()) return;

        try {
            int id = Integer.parseInt(result.get().trim());
            Equipment selected = null;
            for (var equipmentItem : new EquipmentDAO().getAllEquipment()) {
                if (equipmentItem.getEquipmentId() == id) { selected = equipmentItem; break; }
            }
            if (selected == null) { showMessage("Equipment ID not found."); return; }
            showEditEquipmentForm(selected);
        } catch (NumberFormatException e) { showMessage("Please enter a valid Equipment ID."); }
    }

    private void showEditEquipmentForm(Equipment equipmentData) {
        Stage stage = new Stage();
        Label heading = new Label("Edit Equipment");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField nameField = new TextField(equipmentData.getEquipmentName());
        TextField categoryField = new TextField(equipmentData.getCategory());
        TextField quantityField = new TextField(String.valueOf(equipmentData.getQuantity()));
        TextField availableField = new TextField(String.valueOf(equipmentData.getAvailableQuantity()));
        TextField conditionField = new TextField(equipmentData.getConditionStatus());
        Button updateButton = new Button("Update Equipment");
        Label message = new Label();

        updateButton.setOnAction(action -> {
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int available = Integer.parseInt(availableField.getText().trim());
                String condition = conditionField.getText().trim().toUpperCase();
                if (name.isEmpty() || category.isEmpty() || condition.isEmpty()) { message.setText("Please fill all fields."); return; }
                if (quantity <= 0) { message.setText("Total quantity must be greater than 0."); return; }
                if (available < 0 || available > quantity) { message.setText("Available quantity must be between 0 and total quantity."); return; }
                if (!isValidEquipmentCondition(condition)) { message.setText("Invalid condition status."); return; }

                equipmentData.setEquipmentName(name); equipmentData.setCategory(category); equipmentData.setQuantity(quantity);
                equipmentData.setAvailableQuantity(available); equipmentData.setConditionStatus(condition);
                boolean success = new EquipmentDAO().updateEquipment(equipmentData);
                message.setText(success ? "Equipment updated successfully!" : "Failed to update equipment.");
            } catch (NumberFormatException e) { message.setText("Quantity must be valid numbers."); }
        });

        VBox root = new VBox(15, heading, nameField, categoryField, quantityField, availableField, conditionField, updateButton, message);
        root.setAlignment(Pos.CENTER); root.setPadding(new Insets(30));
        stage.setTitle("Edit Equipment"); stage.setScene(new Scene(root, 500, 550)); stage.show();
    }

    private void showDeleteEquipmentWindow() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Equipment"); dialog.setHeaderText("Delete Campus Equipment"); dialog.setContentText("Enter Equipment ID:");
        var result = dialog.showAndWait();
        if (result.isEmpty()) return;

        try {
            int id = Integer.parseInt(result.get().trim());
            boolean exists = false;
            for (var equipmentItem : new EquipmentDAO().getAllEquipment()) {
                if (equipmentItem.getEquipmentId() == id) { exists = true; break; }
            }
            if (!exists) { showMessage("Equipment ID not found."); return; }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete"); confirmation.setHeaderText("Delete Equipment " + id + "?");
            confirmation.setContentText("This action cannot be undone.");
            var confirm = confirmation.showAndWait();
            if (confirm.isPresent() && confirm.get() == ButtonType.OK) {
                boolean success = new EquipmentDAO().deleteEquipment(id);
                showMessage(success ? "Equipment deleted successfully!" : "Failed to delete equipment.");
            }
        } catch (NumberFormatException e) { showMessage("Please enter a valid Equipment ID."); }
    }

    private boolean isValidEquipmentCondition(String condition) {
        return condition.equals("GOOD") || condition.equals("DAMAGED") || condition.equals("UNDER_MAINTENANCE");
    }

    // ====================================
    // EVENT MANAGEMENT MENU
    // ====================================

    private void showEventManagement() {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Manage Events"
        );

        alert.setHeaderText(
                "Event Management"
        );

        ButtonType addButton =
                new ButtonType("Add Event");

        ButtonType editButton =
                new ButtonType("Edit Event");

        ButtonType deleteButton =
                new ButtonType("Delete Event");

        ButtonType viewButton =
                new ButtonType("View Events");

        ButtonType closeButton =
                new ButtonType("Close");

        alert.getButtonTypes().setAll(
                addButton,
                editButton,
                deleteButton,
                viewButton,
                closeButton
        );

        var result =
                alert.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        if (result.get() == addButton) {

            showAddEventWindow();

        } else if (result.get() == editButton) {

            showEditEventWindow();

        } else if (result.get() == deleteButton) {

            showDeleteEventWindow();

        } else if (result.get() == viewButton) {

            showEvents();
        }
    }

    // ====================================
    // VIEW EVENTS
    // ====================================

    private void showEvents() {

        EventDAO eventDAO =
                new EventDAO();

        var events =
                eventDAO.getAllEvents();

        StringBuilder data =
                new StringBuilder();

        if (events.isEmpty()) {

            data.append(
                    "No events found."
            );

        } else {

            // IMPORTANT:
            // Use eventItem instead of event
            // to avoid conflict with JavaFX
            // event/action variables.

            for (var eventItem : events) {

                data.append("Event ID: ")
                    .append(
                            eventItem.getEventId()
                    )
                    .append("\n")

                    .append("Event Name: ")
                    .append(
                            eventItem.getEventName()
                    )
                    .append("\n")

                    .append("Description: ")
                    .append(
                            eventItem.getDescription()
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

                    .append("Organizer ID: ")
                    .append(
                            eventItem.getOrganizerId()
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
                "All Events"
        );

        alert.setContentText(
                data.toString()
        );

        alert.showAndWait();
    }

    // ====================================
    // ADD EVENT
    // ====================================

    private void showAddEventWindow() {

        Stage eventStage =
                new Stage();

        Label heading =
                new Label(
                        "Add New Event"
                );

        heading.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Event Name"
        );

        TextField descriptionField =
                new TextField();

        descriptionField.setPromptText(
                "Description"
        );

        DatePicker datePicker =
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

        TextField organizerField =
                new TextField();

        organizerField.setPromptText(
                "Organizer User ID"
        );

        Button addButton =
                new Button(
                        "Add Event"
                );

        Label message =
                new Label();

        addButton.setOnAction(action -> {

            try {

                String name =
                        nameField.getText().trim();

                String description =
                        descriptionField
                                .getText()
                                .trim();

                LocalDate date =
                        datePicker.getValue();

                LocalTime startTime =
                        LocalTime.parse(
                                startTimeField
                                        .getText()
                                        .trim()
                        );

                LocalTime endTime =
                        LocalTime.parse(
                                endTimeField
                                        .getText()
                                        .trim()
                        );

                int organizerId =
                        Integer.parseInt(
                                organizerField
                                        .getText()
                                        .trim()
                        );

                if (name.isEmpty()
                        || description.isEmpty()
                        || date == null) {

                    message.setText(
                            "Please fill all fields."
                    );

                    return;
                }

                if (!endTime.isAfter(
                        startTime
                )) {

                    message.setText(
                            "End time must be after start time."
                    );

                    return;
                }

                if (organizerId <= 0) {

                    message.setText(
                            "Enter a valid Organizer ID."
                    );

                    return;
                }

                Event newEvent =
                        new Event(
                                0,
                                name,
                                description,
                                date,
                                startTime,
                                endTime,
                                organizerId
                        );

                EventDAO eventDAO =
                        new EventDAO();

                boolean success =
                        eventDAO.addEvent(
                                newEvent
                        );

                if (success) {

                    message.setText(
                            "Event added successfully!"
                    );

                    nameField.clear();
                    descriptionField.clear();
                    datePicker.setValue(null);
                    startTimeField.clear();
                    endTimeField.clear();
                    organizerField.clear();

                } else {

                    message.setText(
                            "Failed to add event."
                    );
                }

            } catch (
                    Exception e
            ) {

                message.setText(
                        "Please enter valid event details."
                );
            }
        });

        VBox root =
                new VBox(
                        15,
                        heading,
                        nameField,
                        descriptionField,
                        new Label("Event Date"),
                        datePicker,
                        startTimeField,
                        endTimeField,
                        organizerField,
                        addButton,
                        message
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(30)
        );

        Scene scene =
                new Scene(
                        root,
                        500,
                        600
                );

        eventStage.setTitle(
                "Add Event"
        );

        eventStage.setScene(
                scene
        );

        eventStage.show();
    }

    // ====================================
    // EDIT EVENT
    // ====================================

    private void showEditEventWindow() {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Edit Event"
        );

        dialog.setHeaderText(
                "Enter Event ID"
        );

        dialog.setContentText(
                "Event ID:"
        );

        var result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        try {

            int eventId =
                    Integer.parseInt(
                            result.get().trim()
                    );

            EventDAO eventDAO =
                    new EventDAO();

            Event selectedEvent =
                    null;

            var events =
                    eventDAO.getAllEvents();

            for (var eventItem : events) {

                if (
                        eventItem.getEventId()
                                == eventId
                ) {

                    selectedEvent =
                            eventItem;

                    break;
                }
            }

            if (selectedEvent == null) {

                showMessage(
                        "Event ID not found."
                );

                return;
            }

            showEditEventForm(
                    selectedEvent
            );

        } catch (
                NumberFormatException e
        ) {

            showMessage(
                    "Please enter a valid Event ID."
            );
        }
    }

    // ====================================
    // EDIT EVENT FORM
    // ====================================

    private void showEditEventForm(
            Event eventData
    ) {

        Stage editStage =
                new Stage();

        Label heading =
                new Label(
                        "Edit Event"
                );

        heading.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        TextField nameField =
                new TextField(
                        eventData.getEventName()
                );

        TextField descriptionField =
                new TextField(
                        eventData.getDescription()
                );

        DatePicker datePicker =
                new DatePicker(
                        eventData.getEventDate()
                );

        TextField startTimeField =
                new TextField(
                        eventData.getStartTime()
                                .toString()
                );

        TextField endTimeField =
                new TextField(
                        eventData.getEndTime()
                                .toString()
                );

        TextField organizerField =
                new TextField(
                        String.valueOf(
                                eventData.getOrganizerId()
                        )
                );

        Button updateButton =
                new Button(
                        "Update Event"
                );

        Label message =
                new Label();

        updateButton.setOnAction(action -> {

            try {

                String name =
                        nameField.getText().trim();

                String description =
                        descriptionField
                                .getText()
                                .trim();

                LocalDate date =
                        datePicker.getValue();

                LocalTime startTime =
                        LocalTime.parse(
                                startTimeField
                                        .getText()
                                        .trim()
                        );

                LocalTime endTime =
                        LocalTime.parse(
                                endTimeField
                                        .getText()
                                        .trim()
                        );

                int organizerId =
                        Integer.parseInt(
                                organizerField
                                        .getText()
                                        .trim()
                        );

                if (name.isEmpty()
                        || description.isEmpty()
                        || date == null) {

                    message.setText(
                            "Please fill all fields."
                    );

                    return;
                }

                if (!endTime.isAfter(
                        startTime
                )) {

                    message.setText(
                            "End time must be after start time."
                    );

                    return;
                }

                if (organizerId <= 0) {

                    message.setText(
                            "Enter a valid Organizer ID."
                    );

                    return;
                }

                eventData.setEventName(
                        name
                );

                eventData.setDescription(
                        description
                );

                eventData.setEventDate(
                        date
                );

                eventData.setStartTime(
                        startTime
                );

                eventData.setEndTime(
                        endTime
                );

                eventData.setOrganizerId(
                        organizerId
                );

                EventDAO eventDAO =
                        new EventDAO();

                boolean success =
                        eventDAO.updateEvent(
                                eventData
                        );

                if (success) {

                    message.setText(
                            "Event updated successfully!"
                    );

                } else {

                    message.setText(
                            "Failed to update event."
                    );
                }

            } catch (
                    Exception e
            ) {

                message.setText(
                        "Please enter valid event details."
                );
            }
        });

        VBox root =
                new VBox(
                        15,
                        heading,
                        nameField,
                        descriptionField,
                        new Label("Event Date"),
                        datePicker,
                        startTimeField,
                        endTimeField,
                        organizerField,
                        updateButton,
                        message
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(30)
        );

        Scene scene =
                new Scene(
                        root,
                        500,
                        600
                );

        editStage.setTitle(
                "Edit Event"
        );

        editStage.setScene(
                scene
        );

        editStage.show();
    }

    // ====================================
    // DELETE EVENT
    // ====================================

    private void showDeleteEventWindow() {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Delete Event"
        );

        dialog.setHeaderText(
                "Delete Campus Event"
        );

        dialog.setContentText(
                "Enter Event ID:"
        );

        var result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        try {

            int eventId =
                    Integer.parseInt(
                            result.get().trim()
                    );

            EventDAO eventDAO =
                    new EventDAO();

            boolean exists = false;

            var events =
                    eventDAO.getAllEvents();

            for (var eventItem : events) {

                if (
                        eventItem.getEventId()
                                == eventId
                ) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {

                showMessage(
                        "Event ID not found."
                );

                return;
            }

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Confirm Delete"
            );

            confirmation.setHeaderText(
                    "Delete Event " + eventId + "?"
            );

            confirmation.setContentText(
                    "This action cannot be undone."
            );

            var confirmResult =
                    confirmation.showAndWait();

            if (
                    confirmResult.isPresent()
                    &&
                    confirmResult.get()
                            == ButtonType.OK
            ) {

                boolean success =
                        eventDAO.deleteEvent(
                                eventId
                        );

                if (success) {

                    showMessage(
                            "Event deleted successfully!"
                    );

                } else {

                    showMessage(
                            "Failed to delete event."
                    );
                }
            }

        } catch (
                NumberFormatException e
        ) {

            showMessage(
                    "Please enter a valid Event ID."
            );
        }
    }

    // ====================================
    // RESOURCE METHODS
    // ====================================

    private void showResources() {

        ResourceDAO resourceDAO =
                new ResourceDAO();

        var resources =
                resourceDAO.getAllResources();

        StringBuilder data =
                new StringBuilder();

        if (resources.isEmpty()) {

            data.append(
                    "No resources found."
            );

        } else {

            for (var resource : resources) {

                data.append("Resource ID: ")
                    .append(
                            resource.getResourceId()
                    )
                    .append("\n")

                    .append("Name: ")
                    .append(
                            resource.getResourceName()
                    )
                    .append("\n")

                    .append("Type: ")
                    .append(
                            resource.getResourceType()
                    )
                    .append("\n")

                    .append("Location: ")
                    .append(
                            resource.getLocation()
                    )
                    .append("\n")

                    .append("Capacity: ")
                    .append(
                            resource.getCapacity()
                    )
                    .append("\n")

                    .append("Status: ")
                    .append(
                            resource.getAvailabilityStatus()
                    )
                    .append("\n\n");
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
                "All Resources"
        );

        alert.setContentText(
                data.toString()
        );

        alert.showAndWait();
    }

    // ====================================
    // ADD RESOURCE
    // ====================================

    private void showAddResourceWindow() {

        Stage resourceStage =
                new Stage();

        Label heading =
                new Label(
                        "Add New Resource"
                );

        heading.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Resource Name"
        );

        TextField typeField =
                new TextField();

        typeField.setPromptText(
                "CLASSROOM / LAB / SEMINAR_HALL / SPORTS_FACILITY"
        );

        TextField locationField =
                new TextField();

        locationField.setPromptText(
                "Location"
        );

        TextField capacityField =
                new TextField();

        capacityField.setPromptText(
                "Capacity"
        );

        Button addButton =
                new Button(
                        "Add Resource"
                );

        Label message =
                new Label();

        addButton.setOnAction(action -> {

            try {

                String name =
                        nameField.getText().trim();

                String type =
                        typeField.getText()
                                .trim()
                                .toUpperCase();

                String location =
                        locationField.getText()
                                .trim();

                int capacity =
                        Integer.parseInt(
                                capacityField
                                        .getText()
                                        .trim()
                        );

                if (name.isEmpty()
                        || type.isEmpty()
                        || location.isEmpty()) {

                    message.setText(
                            "Please fill all fields."
                    );

                    return;
                }

                if (capacity <= 0) {

                    message.setText(
                            "Capacity must be greater than 0."
                    );

                    return;
                }

                if (!isValidResourceType(type)) {

                    message.setText(
                            "Invalid resource type."
                    );

                    return;
                }

                Resource resource =
                        new Resource(
                                0,
                                name,
                                type,
                                location,
                                capacity,
                                "AVAILABLE"
                        );

                ResourceDAO resourceDAO =
                        new ResourceDAO();

                boolean success =
                        resourceDAO.addResource(
                                resource
                        );

                if (success) {

                    message.setText(
                            "Resource added successfully!"
                    );

                    nameField.clear();
                    typeField.clear();
                    locationField.clear();
                    capacityField.clear();

                } else {

                    message.setText(
                            "Failed to add resource."
                    );
                }

            } catch (
                    NumberFormatException e
            ) {

                message.setText(
                        "Capacity must be a valid number."
                );
            }
        });

        VBox root =
                new VBox(
                        15,
                        heading,
                        nameField,
                        typeField,
                        locationField,
                        capacityField,
                        addButton,
                        message
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(30)
        );

        Scene scene =
                new Scene(
                        root,
                        500,
                        450
                );

        resourceStage.setTitle(
                "Add Resource"
        );

        resourceStage.setScene(
                scene
        );

        resourceStage.show();
    }

    // ====================================
    // EDIT RESOURCE
    // ====================================

    private void showEditResourceWindow() {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Edit Resource"
        );

        dialog.setHeaderText(
                "Enter Resource ID"
        );

        dialog.setContentText(
                "Resource ID:"
        );

        var result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        try {

            int resourceId =
                    Integer.parseInt(
                            result.get().trim()
                    );

            ResourceDAO resourceDAO =
                    new ResourceDAO();

            Resource selectedResource =
                    null;

            var resources =
                    resourceDAO.getAllResources();

            for (var resource : resources) {

                if (
                        resource.getResourceId()
                                == resourceId
                ) {

                    selectedResource =
                            resource;

                    break;
                }
            }

            if (selectedResource == null) {

                showMessage(
                        "Resource ID not found."
                );

                return;
            }

            showEditResourceForm(
                    selectedResource
            );

        } catch (
                NumberFormatException e
        ) {

            showMessage(
                    "Please enter a valid Resource ID."
            );
        }
    }

    // ====================================
    // EDIT RESOURCE FORM
    // ====================================

    private void showEditResourceForm(
            Resource resource
    ) {

        Stage editStage =
                new Stage();

        Label heading =
                new Label(
                        "Edit Resource"
                );

        heading.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        TextField nameField =
                new TextField(
                        resource.getResourceName()
                );

        TextField typeField =
                new TextField(
                        resource.getResourceType()
                );

        TextField locationField =
                new TextField(
                        resource.getLocation()
                );

        TextField capacityField =
                new TextField(
                        String.valueOf(
                                resource.getCapacity()
                        )
                );

        TextField statusField =
                new TextField(
                        resource.getAvailabilityStatus()
                );

        Button updateButton =
                new Button(
                        "Update Resource"
                );

        Label message =
                new Label();

        updateButton.setOnAction(action -> {

            try {

                String name =
                        nameField.getText().trim();

                String type =
                        typeField.getText()
                                .trim()
                                .toUpperCase();

                String location =
                        locationField.getText().trim();

                int capacity =
                        Integer.parseInt(
                                capacityField
                                        .getText()
                                        .trim()
                        );

                String status =
                        statusField.getText()
                                .trim()
                                .toUpperCase();

                if (name.isEmpty()
                        || type.isEmpty()
                        || location.isEmpty()
                        || status.isEmpty()) {

                    message.setText(
                            "Please fill all fields."
                    );

                    return;
                }

                if (capacity <= 0) {

                    message.setText(
                            "Capacity must be greater than 0."
                    );

                    return;
                }

                if (!isValidResourceType(type)) {

                    message.setText(
                            "Invalid resource type."
                    );

                    return;
                }

                if (!status.equals("AVAILABLE")
                        && !status.equals("MAINTENANCE")) {

                    message.setText(
                            "Status must be AVAILABLE or MAINTENANCE."
                    );

                    return;
                }

                resource.setResourceName(
                        name
                );

                resource.setResourceType(
                        type
                );

                resource.setLocation(
                        location
                );

                resource.setCapacity(
                        capacity
                );

                resource.setAvailabilityStatus(
                        status
                );

                ResourceDAO resourceDAO =
                        new ResourceDAO();

                boolean success =
                        resourceDAO.updateResource(
                                resource
                        );

                if (success) {

                    message.setText(
                            "Resource updated successfully!"
                    );

                } else {

                    message.setText(
                            "Failed to update resource."
                    );
                }

            } catch (
                    NumberFormatException e
            ) {

                message.setText(
                        "Capacity must be a valid number."
                );
            }
        });

        VBox root =
                new VBox(
                        15,
                        heading,
                        nameField,
                        typeField,
                        locationField,
                        capacityField,
                        statusField,
                        updateButton,
                        message
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(30)
        );

        Scene scene =
                new Scene(
                        root,
                        500,
                        500
                );

        editStage.setTitle(
                "Edit Resource"
        );

        editStage.setScene(
                scene
        );

        editStage.show();
    }

    // ====================================
    // DELETE RESOURCE
    // ====================================

    private void showDeleteResourceWindow() {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Delete Resource"
        );

        dialog.setHeaderText(
                "Delete Campus Resource"
        );

        dialog.setContentText(
                "Enter Resource ID:"
        );

        var result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        try {

            int resourceId =
                    Integer.parseInt(
                            result.get().trim()
                    );

            ResourceDAO resourceDAO =
                    new ResourceDAO();

            boolean exists = false;

            var resources =
                    resourceDAO.getAllResources();

            for (var resource : resources) {

                if (
                        resource.getResourceId()
                                == resourceId
                ) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {

                showMessage(
                        "Resource ID not found."
                );

                return;
            }

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Confirm Delete"
            );

            confirmation.setHeaderText(
                    "Delete Resource " + resourceId + "?"
            );

            confirmation.setContentText(
                    "This action cannot be undone."
            );

            var confirmResult =
                    confirmation.showAndWait();

            if (
                    confirmResult.isPresent()
                    &&
                    confirmResult.get()
                            == ButtonType.OK
            ) {

                boolean success =
                        resourceDAO.deleteResource(
                                resourceId
                        );

                if (success) {

                    showMessage(
                            "Resource deleted successfully!"
                    );

                } else {

                    showMessage(
                            "Failed to delete resource."
                    );
                }
            }

        } catch (
                NumberFormatException e
        ) {

            showMessage(
                    "Please enter a valid Resource ID."
            );
        }
    }

    // ====================================
    // RESOURCE TYPE VALIDATION
    // ====================================

    private boolean isValidResourceType(
            String type
    ) {

        return type.equals("CLASSROOM")
                || type.equals("LAB")
                || type.equals("SEMINAR_HALL")
                || type.equals("SPORTS_FACILITY");
    }

    // ====================================
    // COMMON MESSAGE
    // ====================================

    private void showMessage(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}


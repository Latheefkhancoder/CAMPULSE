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
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AdminDashboard {

    private javafx.scene.layout.BorderPane mainLayout;
    private javafx.scene.layout.StackPane contentArea;
    private User currentUser;

    public void show(User user, Stage stage) {
        this.currentUser = user;

        mainLayout = new javafx.scene.layout.BorderPane();
        mainLayout.setStyle("-fx-background-color: #f4f7fb;");

        // Left navigation sidebar
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(26, 18, 22, 18));
        sidebar.setPrefWidth(245);
        sidebar.setStyle("-fx-background-color: #14243a;");

        Label brand = new Label("SMART CAMPUS");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label subtitle = new Label("RESOURCE MANAGEMENT");
        subtitle.setStyle("-fx-text-fill: #9fb3ca; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label adminBadge = new Label("ADMINISTRATOR");
        adminBadge.setStyle("-fx-text-fill: #b9d5ff; -fx-background-color: #263d5c; -fx-background-radius: 14; -fx-padding: 7 12 7 12; -fx-font-size: 11px;");

        Button dashboardButton = navigationButton("Dashboard");
        Button resourcesButton = navigationButton("Manage Resources");
        Button eventsButton = navigationButton("Manage Events");
        Button bookingsButton = navigationButton("Manage Bookings");
        Button equipmentButton = navigationButton("Manage Equipment");

        dashboardButton.setOnAction(e -> showAdminHome(user));
        resourcesButton.setOnAction(e -> showManagementPage(
                "Manage Resources", "Manage campus rooms, halls and other bookable resources.",
                new String[]{"Add Resource", "Edit Resource", "Delete Resource", "View Resources"},
                new Runnable[]{this::showAddResourceWindow, this::showEditResourceWindow,
                        this::showDeleteResourceWindow, this::showResources}));
        eventsButton.setOnAction(e -> showManagementPage(
                "Manage Events", "Create, update and review campus events.",
                new String[]{"Add Event", "Edit Event", "Delete Event", "View Events"},
                new Runnable[]{this::showAddEventWindow, this::showEditEventWindow,
                        this::showDeleteEventWindow, this::showEvents}));
        bookingsButton.setOnAction(e -> showBookingManagement());
        equipmentButton.setOnAction(e -> showManagementPage(
                "Manage Equipment", "Maintain campus equipment inventory and condition.",
                new String[]{"Add Equipment", "Edit Equipment", "Delete Equipment", "View Equipment"},
                new Runnable[]{this::showAddEquipmentWindow, this::showEditEquipmentWindow,
                        this::showDeleteEquipmentWindow, this::showEquipment}));

        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Button logoutButton = navigationButton("Logout");
        logoutButton.setStyle("-fx-background-color: #7f2935; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12; -fx-alignment: CENTER_LEFT;");
        logoutButton.setOnAction(e -> stage.close());

        sidebar.getChildren().addAll(
                brand, subtitle, new javafx.scene.control.Separator(),
                adminBadge, dashboardButton, resourcesButton, eventsButton,
                bookingsButton, equipmentButton, spacer, logoutButton
        );
        mainLayout.setLeft(sidebar);

        contentArea = new javafx.scene.layout.StackPane();
        contentArea.setPadding(new Insets(28));
        mainLayout.setCenter(contentArea);
        showAdminHome(user);

        Scene scene = new Scene(mainLayout, 1180, 760);
        stage.setTitle("Smart Campus | Administration");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    private Button navigationButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #e5edf7; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 12 14 12 14; -fx-background-radius: 8; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #263d5c; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 12 14 12 14; -fx-background-radius: 8; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #e5edf7; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 12 14 12 14; -fx-background-radius: 8; -fx-cursor: hand;"));
        return button;
    }

    private void showManagementPage(String title, String description, String[] labels, Runnable[] actions) {
        VBox page = new VBox(22);
        page.setPadding(new Insets(8));
        Label eyebrow = new Label("ADMINISTRATION / MANAGEMENT");
        eyebrow.setStyle("-fx-text-fill: #526b8a; -fx-font-size: 11px; -fx-font-weight: bold;");
        Label heading = new Label(title);
        heading.setStyle("-fx-text-fill: #14243a; -fx-font-size: 29px; -fx-font-weight: bold;");
        Label subtitle = new Label(description);
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-text-fill: #65758b; -fx-font-size: 14px;");
        HBox cards = new HBox(16);
        cards.setAlignment(Pos.TOP_LEFT);
        for (int i = 0; i < labels.length; i++) {
            final Runnable action = actions[i];
            VBox card = new VBox(14);
            card.setPadding(new Insets(22));
            card.setPrefWidth(230);
            card.setMinHeight(150);
            card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #e0e7f0; -fx-border-radius: 14;");
            Label cardTitle = new Label(labels[i]);
            cardTitle.setStyle("-fx-text-fill: #14243a; -fx-font-size: 16px; -fx-font-weight: bold;");
            Label detail = new Label(switch (labels[i]) {
                case "Add Resource", "Add Event", "Add Equipment" -> "Create a new record in the campus system.";
                case "Edit Resource", "Edit Event", "Edit Equipment" -> "Update an existing record.";
                case "Delete Resource", "Delete Event", "Delete Equipment" -> "Remove a record after confirmation.";
                default -> "View the current records stored in the database.";
            });
            detail.setWrapText(true);
            detail.setStyle("-fx-text-fill: #65758b; -fx-font-size: 12px;");
            Region spacer = new Region();
            VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            Button open = new Button(labels[i]);
            open.setMaxWidth(Double.MAX_VALUE);
            open.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10; -fx-cursor: hand;");
            open.setOnAction(e -> action.run());
            card.getChildren().addAll(cardTitle, detail, spacer, open);
            cards.getChildren().add(card);
        }
        Button back = new Button("← Back to Dashboard");
        back.setStyle("-fx-background-color: #e7edf5; -fx-text-fill: #14243a; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 16;");
        back.setOnAction(e -> showAdminHome(currentUser));
        page.getChildren().addAll(eyebrow, heading, subtitle, cards, back);
        contentArea.getChildren().setAll(page);
    }

    private void showAdminHome(User user) {
        VBox home = new VBox(22);
        home.setMaxWidth(Double.MAX_VALUE);

        Label eyebrow = new Label("ADMINISTRATION / OVERVIEW");
        eyebrow.setStyle("-fx-text-fill: #526b8a; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label heading = new Label("Welcome back, " + user.getName());
        heading.setStyle("-fx-text-fill: #14243a; -fx-font-size: 29px; -fx-font-weight: bold;");

        Label description = new Label("Monitor campus activity and manage bookings, events, resources and equipment.");
        description.setStyle("-fx-text-fill: #65758b; -fx-font-size: 14px;");

        HBox headingRow = new HBox(12, heading);
        headingRow.setAlignment(Pos.CENTER_LEFT);

        int totalBookings = 0;
        int pendingBookings = 0;
        int approvedBookings = 0;
        int totalResources = 0;
        int totalEvents = 0;
        try {
            var bookings = new BookingDAO().getAllBookings();
            totalBookings = bookings.size();
            for (Booking booking : bookings) {
                if ("PENDING".equalsIgnoreCase(booking.getBookingStatus())) pendingBookings++;
                if ("APPROVED".equalsIgnoreCase(booking.getBookingStatus())) approvedBookings++;
            }
            totalResources = new ResourceDAO().getAllResources().size();
            totalEvents = new EventDAO().getAllEvents().size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        HBox cards = new HBox(16,
                metricCard("TOTAL BOOKINGS", String.valueOf(totalBookings), "All submitted requests", "#2563eb"),
                metricCard("PENDING REQUESTS", String.valueOf(pendingBookings), "Awaiting admin review", "#d97706"),
                metricCard("APPROVED BOOKINGS", String.valueOf(approvedBookings), "Confirmed reservations", "#059669"),
                metricCard("CAMPUS RESOURCES", String.valueOf(totalResources), totalEvents + " registered events", "#7c3aed")
        );
        cards.setAlignment(Pos.CENTER);
        for (javafx.scene.Node node : cards.getChildren()) {
            HBox.setHgrow(node, javafx.scene.layout.Priority.ALWAYS);
        }

        Label quickTitle = new Label("Quick actions");
        quickTitle.setStyle("-fx-text-fill: #14243a; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox quickActions = new HBox(16,
                actionCard("Review bookings", "Approve or reject faculty booking requests.", "Open booking management", () -> showBookingManagement()),
                actionCard("Manage resources", "Add, update, view or remove campus venues.", "Open resource tools", () -> showManagementPage(
                    "Manage Resources", "Manage campus rooms, halls and other bookable resources.",
                    new String[]{"Add Resource", "Edit Resource", "Delete Resource", "View Resources"},
                    new Runnable[]{this::showAddResourceWindow, this::showEditResourceWindow,
                            this::showDeleteResourceWindow, this::showResources})),
                actionCard("Manage events", "Maintain campus event information.", "Open event tools", () -> showEventManagement())
        );
        quickActions.setAlignment(Pos.CENTER_LEFT);
        for (javafx.scene.Node node : quickActions.getChildren()) {
            HBox.setHgrow(node, javafx.scene.layout.Priority.ALWAYS);
        }

        Label note = new Label("Tip: Review pending requests regularly to avoid venue scheduling conflicts.");
        note.setWrapText(true);
        note.setStyle("-fx-text-fill: #415875; -fx-background-color: #e7effb; -fx-background-radius: 10; -fx-padding: 14; -fx-font-size: 13px;");

        home.getChildren().addAll(eyebrow, headingRow, description, cards, quickTitle, quickActions, note);
        contentArea.getChildren().setAll(home);
    }

    private VBox metricCard(String title, String value, String subtitle, String accent) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setMinWidth(145);
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #e0e7f0; -fx-border-radius: 14;");
        Label label = new Label(title);
        label.setStyle("-fx-text-fill: #64748b; -fx-font-size: 10px; -fx-font-weight: bold;");
        Label number = new Label(value);
        number.setStyle("-fx-text-fill: " + accent + "; -fx-font-size: 30px; -fx-font-weight: bold;");
        Label detail = new Label(subtitle);
        detail.setWrapText(true);
        detail.setStyle("-fx-text-fill: #718096; -fx-font-size: 11px;");
        Region stripe = new Region();
        stripe.setPrefHeight(4);
        stripe.setMaxWidth(48);
        stripe.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 2;");
        card.getChildren().addAll(stripe, label, number, detail);
        return card;
    }

    private VBox actionCard(String title, String description, String buttonText, Runnable action) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefWidth(260);
        card.setMinHeight(180);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #e0e7f0; -fx-border-radius: 14;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #14243a; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #65758b; -fx-font-size: 12px;");
        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Button button = new Button(buttonText);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10;");
        button.setOnAction(e -> action.run());
        card.getChildren().addAll(titleLabel, descLabel, spacer, button);
        return card;
    }

    // ====================================
    // BOOKING MANAGEMENT TABLE
    // ====================================

    private void showBookingManagement() {

        BookingDAO bookingDAO = new BookingDAO();


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
        closeButton.setText("Back to Dashboard");
        closeButton.setOnAction(event -> showAdminHome(currentUser));

        HBox filterRow = new HBox(10,
                new Label("Status:"), statusFilter, refreshButton);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        HBox actionRow = new HBox(10,
                approveButton, rejectButton, cancelButton,
                completeButton, closeButton);
        actionRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, heading, filterRow, table, actionRow);
        root.setPadding(new Insets(8));
        root.setStyle("-fx-background-color: #f4f7fb;");
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
        table.setMinHeight(300);
        contentArea.getChildren().setAll(root);
        refreshTable.run();
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
        showManagementPage("Manage Events", "Create, update and review campus events.",
                new String[]{"Add Event", "Edit Event", "Delete Event", "View Events"},
                new Runnable[]{this::showAddEventWindow, this::showEditEventWindow,
                        this::showDeleteEventWindow, this::showEvents});
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


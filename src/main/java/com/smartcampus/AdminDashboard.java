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
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AdminDashboard {

    private javafx.scene.layout.BorderPane mainLayout;
    private javafx.scene.layout.StackPane contentArea;
    private User currentUser;
    private Stage primaryStage;
    private Runnable logoutAction;

    public void show(User user, Stage stage) {
        show(user, stage, stage::close);
    }

    public void show(User user, Stage stage, Runnable logoutAction) {
        this.currentUser = user;
        this.primaryStage = stage;
        this.logoutAction = logoutAction;

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
        resourcesButton.setOnAction(e -> showResources());
        eventsButton.setOnAction(e -> showEvents());
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
        logoutButton.setOnAction(e -> this.logoutAction.run());

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
                actionCard("Manage resources", "Add, update, view or remove campus venues.",
                        "Open resource tools", this::showResources),
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

        Button createButton = new Button("Create Booking");
        createButton.setOnAction(event -> showCreateBookingForm());

        Button approveButton = new Button("Approve");
        Button rejectButton = new Button("Reject");
        Button cancelButton = new Button("Cancel Booking");
        Button completeButton = new Button("Complete");
        Button editButton = new Button("Update Details");
        Button deleteButton = new Button("Delete Booking");
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
        editButton.setOnAction(event ->
                showUpdateBookingDialog(
                        table.getSelectionModel().getSelectedItem(),
                        refreshTable));
        deleteButton.setOnAction(event ->
                deleteSelectedBooking(
                        table.getSelectionModel().getSelectedItem(),
                        refreshTable));
        closeButton.setText("Back to Dashboard");
        closeButton.setOnAction(event -> showAdminHome(currentUser));

        HBox filterRow = new HBox(10,
                new Label("Status:"), statusFilter, refreshButton, createButton);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        HBox actionRow = new HBox(10,
                approveButton, rejectButton, cancelButton,
                completeButton, editButton, deleteButton, closeButton);
        actionRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, heading, filterRow, table, actionRow);
        root.setPadding(new Insets(8));
        root.setStyle("-fx-background-color: #f4f7fb;");
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
        table.setMinHeight(300);
        contentArea.getChildren().setAll(root);
        refreshTable.run();
    }

    private void showCreateBookingForm() {
        Button backButton = new Button("Back to Bookings");
        backButton.setOnAction(event -> showBookingManagement());

        Node bookingForm = new Dashboard().buildNewBookingPage(
                currentUser, this::showBookingManagement);
        VBox page = new VBox(12, backButton, bookingForm);
        VBox.setVgrow(bookingForm, Priority.ALWAYS);
        contentArea.getChildren().setAll(page);
    }

    private void showUpdateBookingDialog(
            Booking booking, Runnable refreshTable) {
        if (booking == null) {
            showMessage("Please select a booking from the table first.");
            return;
        }
        if (!"PENDING".equalsIgnoreCase(booking.getBookingStatus())) {
            showMessage("Only pending bookings can be updated.");
            return;
        }

        var availableResources = new ResourceDAO().getAllResources().stream()
                .filter(resource ->
                        "AVAILABLE".equalsIgnoreCase(
                                resource.getAvailabilityStatus()))
                .toList();
        if (availableResources.isEmpty()) {
            showMessage("No available resources can be assigned to this booking.");
            return;
        }

        ComboBox<Resource> resourceBox = new ComboBox<>(
                FXCollections.observableArrayList(availableResources));
        resourceBox.setMaxWidth(Double.MAX_VALUE);
        resourceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Resource resource) {
                return resource == null
                        ? ""
                        : resource.getResourceName() + " — "
                                + resource.getLocation();
            }

            @Override
            public Resource fromString(String value) {
                return null;
            }
        });
        resourceBox.getItems().stream()
                .filter(resource ->
                        resource.getResourceId() == booking.getResourceId())
                .findFirst()
                .ifPresentOrElse(
                        resourceBox::setValue,
                        () -> resourceBox.getSelectionModel().selectFirst());

        DatePicker datePicker = new DatePicker(booking.getBookingDate());
        TextField startTimeField = new TextField(
                booking.getStartTime().toString());
        startTimeField.setPromptText("HH:mm");
        TextField endTimeField = new TextField(
                booking.getEndTime().toString());
        endTimeField.setPromptText("HH:mm");

        GridPane fields = new GridPane();
        fields.setHgap(12);
        fields.setVgap(12);
        fields.addRow(0, new Label("Resource"), resourceBox);
        fields.addRow(1, new Label("Date"), datePicker);
        fields.addRow(2, new Label("Start time (24-hour HH:mm)"), startTimeField);
        fields.addRow(3, new Label("End time (24-hour HH:mm)"), endTimeField);
        GridPane.setHgrow(resourceBox, Priority.ALWAYS);
        GridPane.setHgrow(datePicker, Priority.ALWAYS);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Pending Booking");
        dialog.setHeaderText(
                "Change the resource or schedule. The linked event schedule will also be updated.");
        dialog.getDialogPane().setContent(fields);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL);

        if (dialog.showAndWait().filter(ButtonType.OK::equals).isEmpty()) {
            return;
        }

        try {
            Resource resource = resourceBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime startTime = LocalTime.parse(
                    startTimeField.getText().trim());
            LocalTime endTime = LocalTime.parse(
                    endTimeField.getText().trim());
            if (resource == null || date == null) {
                showMessage("Please select a resource and date.");
                return;
            }

            boolean success = new com.smartcampus.service.BookingService()
                    .updatePendingBookingDetails(
                            booking.getBookingId(),
                            resource.getResourceId(),
                            date,
                            startTime,
                            endTime);
            if (success) {
                showMessage("Booking #" + booking.getBookingId()
                        + " details updated.");
                refreshTable.run();
            } else {
                showMessage("Booking details could not be updated. The booking "
                        + "must still be pending, and the resource and time "
                        + "must be available and conflict-free.");
            }
        } catch (DateTimeParseException e) {
            showMessage("Enter start and end times in 24-hour HH:mm format.");
        }
    }

    private void deleteSelectedBooking(
            Booking booking, Runnable refreshTable) {
        if (booking == null) {
            showMessage("Please select a booking from the table first.");
            return;
        }
        if (!"REJECTED".equalsIgnoreCase(booking.getBookingStatus())
                && !"CANCELLED".equalsIgnoreCase(
                        booking.getBookingStatus())) {
            showMessage("Only rejected or cancelled bookings can be deleted. "
                    + "Pending, approved, and completed bookings are retained.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Booking");
        confirmation.setHeaderText(
                "Delete booking #" + booking.getBookingId() + "?");
        confirmation.setContentText(
                "Only this rejected/cancelled booking record will be removed; "
                        + "its linked event will remain.");
        if (confirmation.showAndWait().filter(
                ButtonType.OK::equals).isEmpty()) {
            return;
        }

        if (new BookingDAO().deleteBooking(booking.getBookingId())) {
            showMessage("Booking #" + booking.getBookingId() + " deleted.");
            refreshTable.run();
        } else {
            showMessage("Booking could not be deleted. It may have changed "
                    + "status and is no longer eligible for deletion.");
        }
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
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
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
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.WINDOW_MODAL);
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
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
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
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.WINDOW_MODAL);
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
        showEvents();
    }

    // ====================================
    // VIEW EVENTS
    // ====================================

    private void showEvents() {
        EventDAO eventDAO = new EventDAO();
        TableView<Event> table = new TableView<>();
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.getColumns().add(eventColumn(
                "Event ID", "eventId", 75));
        table.getColumns().add(eventColumn(
                "Event Name", "eventName", 150));
        table.getColumns().add(eventColumn(
                "Description", "description", 220));

        TableColumn<Event, LocalDate> dateColumn =
                new TableColumn<>("Event Date");
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("eventDate"));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMM yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(formatter));
            }
        });
        table.getColumns().add(dateColumn);

        table.getColumns().add(timeEventColumn("Start Time", true));
        table.getColumns().add(timeEventColumn("End Time", false));
        table.getColumns().add(eventColumn(
                "Organizer (User ID)", "organizerId", 120));

        ObservableList<Event> tableData =
                FXCollections.observableArrayList();
        table.setItems(tableData);
        Runnable refresh = () ->
                tableData.setAll(eventDAO.getAllEvents());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refresh.run());
        Button addButton = new Button("Create Event");
        addButton.setOnAction(event -> showEventForm(null, refresh));
        Button editButton = new Button("Edit Selected");
        editButton.setOnAction(event ->
                showEventForm(table.getSelectionModel().getSelectedItem(), refresh));
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(event ->
                deleteSelectedEvent(
                        table.getSelectionModel().getSelectedItem(), refresh));
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(event -> showAdminHome(currentUser));

        Label heading = new Label("Campus Events");
        heading.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        HBox actions = new HBox(
                10, addButton, editButton, deleteButton, refreshButton, backButton);
        actions.setAlignment(Pos.CENTER_LEFT);
        VBox page = new VBox(14, heading, table, actions);
        page.setPadding(new Insets(8));
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setMinHeight(300);
        contentArea.getChildren().setAll(page);
        table.setRowFactory(view -> {
            var row = new javafx.scene.control.TableRow<Event>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showEventForm(row.getItem(), refresh);
                }
            });
            return row;
        });
        refresh.run();
    }

    private TableColumn<Event, ?> eventColumn(
            String title, String property, double width) {
        TableColumn<Event, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private TableColumn<Event, LocalTime> timeEventColumn(
            String title, boolean startTime) {
        TableColumn<Event, LocalTime> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(
                startTime ? "startTime" : "endTime"));
        column.setCellFactory(ignored -> new TableCell<>() {
            private final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("HH:mm");

            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                setText(empty || time == null ? null : time.format(formatter));
            }
        });
        return column;
    }

    private void showEventForm(Event selected, Runnable refresh) {
        boolean editing = selected != null;
        TextField name = new TextField(editing ? selected.getEventName() : "");
        TextField description = new TextField(
                editing && selected.getDescription() != null
                        ? selected.getDescription() : "");
        DatePicker date = new DatePicker(
                editing ? selected.getEventDate() : null);
        TextField start = new TextField(
                editing ? selected.getStartTime().toString() : "");
        start.setPromptText("HH:mm");
        TextField end = new TextField(
                editing ? selected.getEndTime().toString() : "");
        end.setPromptText("HH:mm");
        TextField organizer = new TextField(
                editing ? String.valueOf(selected.getOrganizerId())
                        : String.valueOf(currentUser.getUserId()));
        GridPane fields = new GridPane();
        fields.setHgap(12);
        fields.setVgap(12);
        fields.addRow(0, new Label("Event name"), name);
        fields.addRow(1, new Label("Description"), description);
        fields.addRow(2, new Label("Event date"), date);
        fields.addRow(3, new Label("Start time (HH:mm)"), start);
        fields.addRow(4, new Label("End time (HH:mm)"), end);
        fields.addRow(5, new Label("Organizer user ID"), organizer);
        GridPane.setHgrow(name, Priority.ALWAYS);
        GridPane.setHgrow(description, Priority.ALWAYS);
        GridPane.setHgrow(date, Priority.ALWAYS);
        GridPane.setHgrow(start, Priority.ALWAYS);
        GridPane.setHgrow(end, Priority.ALWAYS);
        GridPane.setHgrow(organizer, Priority.ALWAYS);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle(editing ? "Edit Event" : "Create Event");
        dialog.setHeaderText(editing
                ? "Update the selected event"
                : "Enter the event details");
        dialog.getDialogPane().setContent(fields);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL);
        if (dialog.showAndWait().filter(ButtonType.OK::equals).isEmpty()) {
            return;
        }

        try {
            String eventName = name.getText() == null
                    ? "" : name.getText().trim();
            String eventDescription = description.getText() == null
                    ? "" : description.getText().trim();
            LocalDate eventDate = date.getValue();
            LocalTime startTime = LocalTime.parse(start.getText().trim());
            LocalTime endTime = LocalTime.parse(end.getText().trim());
            int organizerId = Integer.parseInt(organizer.getText().trim());
            if (eventName.isEmpty() || eventDate == null || organizerId <= 0) {
                showMessage("Event name, date, and a valid organizer ID are required.");
                return;
            }
            if (eventName.length() > 150 || eventDescription.length() > 500) {
                showMessage("Event name must be at most 150 characters and "
                        + "description at most 500 characters.");
                return;
            }
            if (!endTime.isAfter(startTime)) {
                showMessage("End time must be later than start time.");
                return;
            }

            Event event = new Event(
                    editing ? selected.getEventId() : 0,
                    eventName,
                    eventDescription,
                    eventDate,
                    startTime,
                    endTime,
                    organizerId);
            boolean success = editing
                    ? new EventDAO().updateEvent(event)
                    : new EventDAO().addEvent(event);
            if (success) {
                showMessage(editing
                        ? "Event updated successfully."
                        : "Event created successfully.");
                refresh.run();
            } else {
                showMessage(editing
                        ? "Event could not be updated. A booking-linked event "
                                + "cannot have its date or time changed here."
                        : "Event could not be created. Check the organizer ID and database.");
            }
        } catch (DateTimeParseException e) {
            showMessage("Enter event times in 24-hour HH:mm format.");
        } catch (NumberFormatException e) {
            showMessage("Organizer ID must be a valid positive number.");
        }
    }

    private void deleteSelectedEvent(Event selected, Runnable refresh) {
        if (selected == null) {
            showMessage("Select an event before deleting.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.initOwner(primaryStage);
        confirmation.initModality(Modality.WINDOW_MODAL);
        confirmation.setTitle("Delete Event");
        confirmation.setHeaderText(
                "Delete event #" + selected.getEventId() + "?");
        confirmation.setContentText(
                "Events linked to bookings cannot be deleted.");
        if (confirmation.showAndWait().filter(ButtonType.OK::equals).isEmpty()) {
            return;
        }
        if (new EventDAO().deleteEvent(selected.getEventId())) {
            showMessage("Event deleted successfully.");
            refresh.run();
        } else {
            showMessage("Event could not be deleted. It may be linked to a booking.");
        }
    }

    // ====================================
    // RESOURCE METHODS
    // ====================================

    private void showResources() {
        ResourceDAO resourceDAO = new ResourceDAO();
        TableView<Resource> table = new TableView<>();
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        addResourceColumn(table, "Resource ID", "resourceId", 85);
        addResourceColumn(table, "Name", "resourceName", 160);
        addResourceColumn(table, "Type", "resourceType", 130);
        addResourceColumn(table, "Location", "location", 150);
        addResourceColumn(table, "Capacity", "capacity", 90);
        addResourceColumn(table, "Availability", "availabilityStatus", 120);

        ObservableList<Resource> tableData =
                FXCollections.observableArrayList();
        table.setItems(tableData);
        Runnable refresh = () ->
                tableData.setAll(resourceDAO.getAllResources());

        Button addButton = new Button("Add Resource");
        addButton.setOnAction(event -> showResourceForm(null, refresh));
        Button editButton = new Button("Edit Selected");
        editButton.setOnAction(event ->
                showResourceForm(
                        table.getSelectionModel().getSelectedItem(), refresh));
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(event ->
                deleteSelectedResource(
                        table.getSelectionModel().getSelectedItem(), refresh));
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refresh.run());
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(event -> showAdminHome(currentUser));

        Label heading = new Label("Campus Resources");
        heading.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        HBox actions = new HBox(
                10, addButton, editButton, deleteButton, refreshButton, backButton);
        actions.setAlignment(Pos.CENTER_LEFT);
        VBox page = new VBox(14, heading, table, actions);
        page.setPadding(new Insets(8));
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setMinHeight(300);
        contentArea.getChildren().setAll(page);
        table.setRowFactory(view -> {
            var row = new javafx.scene.control.TableRow<Resource>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showResourceForm(row.getItem(), refresh);
                }
            });
            return row;
        });
        refresh.run();
    }

    private void addResourceColumn(
            TableView<Resource> table,
            String title,
            String property,
            double width) {
        TableColumn<Resource, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        table.getColumns().add(column);
    }

    private void showResourceForm(Resource selected, Runnable refresh) {
        boolean editing = selected != null;
        TextField name = new TextField(
                editing ? selected.getResourceName() : "");
        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll(
                "CLASSROOM", "LAB", "SEMINAR_HALL", "SPORTS_FACILITY");
        type.setMaxWidth(Double.MAX_VALUE);
        if (editing) {
            type.setValue(selected.getResourceType());
        }
        TextField location = new TextField(
                editing ? selected.getLocation() : "");
        TextField capacity = new TextField(
                editing ? String.valueOf(selected.getCapacity()) : "");
        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll("AVAILABLE", "MAINTENANCE");
        status.setMaxWidth(Double.MAX_VALUE);
        status.setValue(editing
                ? selected.getAvailabilityStatus() : "AVAILABLE");

        GridPane fields = new GridPane();
        fields.setHgap(12);
        fields.setVgap(12);
        fields.addRow(0, new Label("Resource name"), name);
        fields.addRow(1, new Label("Resource type"), type);
        fields.addRow(2, new Label("Location"), location);
        fields.addRow(3, new Label("Capacity"), capacity);
        fields.addRow(4, new Label("Availability"), status);
        GridPane.setHgrow(name, Priority.ALWAYS);
        GridPane.setHgrow(type, Priority.ALWAYS);
        GridPane.setHgrow(location, Priority.ALWAYS);
        GridPane.setHgrow(capacity, Priority.ALWAYS);
        GridPane.setHgrow(status, Priority.ALWAYS);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle(editing ? "Edit Resource" : "Add Resource");
        dialog.setHeaderText(editing
                ? "Update the selected resource"
                : "Enter the resource details");
        dialog.getDialogPane().setContent(fields);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL);
        if (dialog.showAndWait().filter(ButtonType.OK::equals).isEmpty()) {
            return;
        }

        String resourceName = name.getText() == null
                ? "" : name.getText().trim();
        String resourceLocation = location.getText() == null
                ? "" : location.getText().trim();
        if (resourceName.isEmpty() || resourceLocation.isEmpty()
                || type.getValue() == null || status.getValue() == null) {
            showMessage("Name, type, location, and availability are required.");
            return;
        }
        if (!isValidResourceType(type.getValue())) {
            showMessage("Select a valid resource type.");
            return;
        }
        if (resourceName.length() > 100 || resourceLocation.length() > 150) {
            showMessage("Resource name must be at most 100 characters and "
                    + "location at most 150 characters.");
            return;
        }
        try {
            int capacityValue = Integer.parseInt(capacity.getText().trim());
            if (capacityValue <= 0) {
                showMessage("Capacity must be greater than zero.");
                return;
            }
            Resource resource = new Resource(
                    editing ? selected.getResourceId() : 0,
                    resourceName,
                    type.getValue(),
                    resourceLocation,
                    capacityValue,
                    status.getValue());
            boolean success = editing
                    ? new ResourceDAO().updateResource(resource)
                    : new ResourceDAO().addResource(resource);
            if (success) {
                showMessage(editing
                        ? "Resource updated successfully."
                        : "Resource added successfully.");
                refresh.run();
            } else {
                showMessage(editing
                        ? "Resource could not be updated."
                        : "Resource could not be added.");
            }
        } catch (NumberFormatException e) {
            showMessage("Capacity must be a valid positive number.");
        }
    }

    private void deleteSelectedResource(
            Resource selected, Runnable refresh) {
        if (selected == null) {
            showMessage("Select a resource before deleting.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.initOwner(primaryStage);
        confirmation.initModality(Modality.WINDOW_MODAL);
        confirmation.setTitle("Delete Resource");
        confirmation.setHeaderText(
                "Delete resource #" + selected.getResourceId() + "?");
        confirmation.setContentText(
                "Resources linked to bookings cannot be deleted.");
        if (confirmation.showAndWait().filter(ButtonType.OK::equals).isEmpty()) {
            return;
        }
        if (new ResourceDAO().deleteResource(selected.getResourceId())) {
            showMessage("Resource deleted successfully.");
            refresh.run();
        } else {
            showMessage("Resource could not be deleted. It may be linked to a booking.");
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
        if (primaryStage != null) {
            alert.initOwner(primaryStage);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}

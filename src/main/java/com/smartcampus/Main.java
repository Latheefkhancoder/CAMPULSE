package com.smartcampus;

import com.smartcampus.dao.UserDAO;
import com.smartcampus.model.User;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label(
                "Smart Campus"
        );

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
                "Resource & Event Management System"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;"
        );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Email"
        );

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Password"
        );

        Button loginButton =
                new Button("Login");

        loginButton.setPrefWidth(200);

        Label message =
                new Label();

        loginButton.setOnAction(event -> {

            String email =
                    emailField.getText().trim();

            String password =
                    passwordField.getText();

            if (email.isEmpty() ||
                password.isEmpty()) {

                message.setText(
                        "Please enter email and password."
                );

                return;
            }

            UserDAO userDAO =
                    new UserDAO();

            try {
                User user =
                        userDAO.login(
                                email,
                                password
                        );

                if (user != null) {

                    String role = user.getRole();

                    if ("ADMIN".equalsIgnoreCase(role)) {

                        AdminDashboard adminDashboard =
                                new AdminDashboard();

                        adminDashboard.show(user, stage);

                    } else if ("FACULTY".equalsIgnoreCase(role)) {

                        Dashboard dashboard =
                                new Dashboard();

                        dashboard.show(user, stage);

                    } else {
                        message.setText(
                                "Access denied. Only administrators and faculty can sign in."
                        );
                    }

                } else {

                    message.setText(
                            "Invalid email or password."
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.setText(
                        "Database connection failed. Check your MySQL configuration and try again."
                );
            }
        });

        VBox root =
                new VBox(
                        15,
                        title,
                        subtitle,
                        emailField,
                        passwordField,
                        loginButton,
                        message
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

        Scene scene =
                new Scene(
                        root,
                        500,
                        400
                );

        stage.setTitle(
                "Smart Campus - Login"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}
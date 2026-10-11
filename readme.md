Smart Campus Resource & Event Management System

A JavaFX desktop application designed to manage campus resources, events, bookings, and equipment inventory. The application provides separate dashboards for administrators and faculty members, with MySQL database integration for storing and managing campus information.

Features

User Authentication: Login using registered email credentials and passwords.

Role-Based Access: Separate dashboards for administrators and faculty members.

Admin Dashboard: Manage campus resources, events, bookings, and equipment inventory.

Faculty Dashboard: View available resources, create booking requests, and manage personal bookings.

Booking Management: Submit booking requests and manage booking statuses.

Booking Validation: Validate booking dates, time ranges, resource availability, and scheduling conflicts.

Database Integration: Store and retrieve application data using MySQL and JDBC.

Structured Architecture: Organize application functionality using models, data access objects (DAOs), and service classes.

Technology Stack

Technology

Purpose

Java 17

Core programming language

JavaFX 21

Desktop graphical user interface

MySQL 8.x

Relational database

MySQL Connector/J 9.4.0

Java-to-MySQL connectivity

JDBC

Database operations

Maven 3.10+

Dependency management and project build

Project Structure

JAVA-DBMS-PROJECT/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── smartcampus/
│                   ├── Main.java
│                   ├── dao/
│                   ├── model/
│                   ├── service/
│                   └── util/
├── database/
│   ├── smart_campus.sql
│   └── sample_data.sql
├── pom.xml
├── .gitignore
└── README.md

Directory descriptions:

com.smartcampus — Main application class and dashboard screens.

dao/ — Database access operations.

model/ — Application entities, including users, resources, events, bookings, and equipment.

service/ — Business logic and booking validation.

util/ — Database connection utilities.

database/ — SQL scripts for database setup and sample data.

pom.xml — Maven dependencies and build configuration.

Keep only the directories and files that actually exist in your repository.

Application Architecture

The application follows a layered architecture:

          JavaFX User Interface
                   |
          Login and Dashboards
                   |
          Service / Business Logic
                   |
                 DAO Layer
                   |
                JDBC Driver
                   |
              MySQL Database

The JavaFX interface handles user interaction. Service classes perform business validations, DAO classes handle database operations, and JDBC connects the application to MySQL.

Database Setup

1. Install MySQL

Install MySQL Server and ensure the database service is running.

2. Create the Database

Create the database named smart_campus:

CREATE DATABASE smart_campus;

If your SQL schema script already creates this database, you can skip this step.

3. Create the Tables

From the project root, run the schema script:

mysql -u root -p < database/smart_campus.sql

Enter your MySQL password when prompted.

4. Load Sample Data

If you have a separate sample data script, run:

mysql -u root -p < database/sample_data.sql

This step is optional. Ensure the sample data is compatible with your schema before running it.

Database Configuration

The application requires a valid MySQL connection.

Configure the database connection using the mechanism supported by your implementation.

For example, if your application reads configuration from a db.properties file, the configuration could contain:

SMART_CAMPUS_DB_URL=jdbc:mysql://localhost:3306/smart_campus
SMART_CAMPUS_DB_USER=root
SMART_CAMPUS_DB_PASSWORD=your_mysql_password_here

These property names must match the configuration-reading code in your application. If your current DBConnection.java uses hardcoded connection settings, update the code to read the configuration before relying on this example.

Security: Never commit actual database passwords or other secrets to GitHub.

Build and Run

Prerequisites

JDK 17 or later.

Apache Maven.

MySQL Server.

Git, if you want to clone the repository.

Build the Project

Open a terminal in the project root and run:

mvn clean compile

Run the Application

mvn javafx:run

The application entry point is:

com.smartcampus.Main

Ensure MySQL is running and the database connection is correctly configured before launching the application.

Authentication and User Roles

The application uses login credentials to identify users and determine which dashboard they can access.

Role

Access

ADMIN

Administrator dashboard and management functions

FACULTY

Faculty dashboard and faculty booking functions

Only authorized administrator and faculty accounts should be used to access the application.

Demo Accounts

For security, obtain demonstration credentials from your local development database or the project's sample data script.

Do not assume that example credentials are available in every database installation. Change demonstration passwords before using the application outside a local development environment.

Testing

Run the following commands to check compilation and execute the configured Maven tests:

mvn clean compile
mvn test

Compilation checks whether the Java source code builds successfully. The test command executes the tests configured in the project.

The following application workflows should also be tested manually:

Successful and unsuccessful login attempts.

Correct dashboard access for Admin and Faculty roles.

Resource and event management.

Booking creation and status updates.

Booking conflict validation.

Database connection failures.

Input validation for dates and times.

Testing note: Successful compilation alone does not prove that every GUI workflow or security control works correctly.

Troubleshooting

Database Connection Failure

Ensure MySQL Server is running.

Verify the database name, connection URL, username, and password.

Confirm that the required tables have been created.

JavaFX Application Fails to Launch

Verify the installed Java version.

Ensure Maven dependencies are downloaded.

Check the Maven output for dependency or JavaFX configuration errors.

Login Fails

Confirm that the user exists in the users table.

Verify that the credentials match the application's authentication implementation.

Ensure the account has the appropriate ADMIN or FACULTY role.

Booking Request Fails

Check that the selected resource exists and is available.

Verify that the date and time inputs are valid.

Check for conflicting bookings and database constraint violations.

Team Members:

Mohamed Rihan

Jeenathul Rila

Latheef Khan

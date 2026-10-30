# SMART CAMPUS RESOURCE & EVENT MANAGEMENT SYSTEM

**Technology Stack:** Java 17, JavaFX 21, MySQL 8.x, JDBC and Maven 3.10+

**Team Members:**

1. Mohamed Rihan
2. Jeenathul Rila
3. Latheef Khan

---

# CHAPTER 1: INTRODUCTION

## 1.1 Project Overview

The Smart Campus Resource & Event Management System is a JavaFX-based desktop application developed to simplify and automate the management of campus resources, events, bookings, and equipment inventory.

Educational institutions frequently manage multiple resources, including classrooms, laboratories, seminar halls, projectors, and other equipment. Managing these resources manually can lead to scheduling conflicts, inefficient resource utilization, booking errors, and difficulties in maintaining records.

To address these challenges, the proposed system provides a centralized platform where administrators and faculty members can manage and access campus resources efficiently.

The application provides separate dashboards for administrators and faculty members through a role-based authentication system. Administrators can manage campus resources, events, bookings, and equipment inventory, while faculty members can view available resources, submit booking requests, and manage their personal bookings.

The system uses JavaFX to provide a graphical user interface and MySQL to store and manage application data. JDBC is used to establish communication between the Java application and the MySQL database.

## 1.2 Problem Statement

Traditional campus resource management often involves manual record maintenance, spreadsheets, or disconnected communication between faculty members and administrators.

These approaches can result in:

* Double booking of classrooms and other resources.
* Difficulty in tracking resource availability.
* Delays in approving booking requests.
* Inefficient equipment inventory management.
* Lack of centralized event information.
* Difficulty maintaining accurate booking records.

Therefore, a centralized and automated system is required to manage campus resources, events, and booking activities while minimizing scheduling conflicts and improving operational efficiency.

## 1.3 Objectives

The primary objectives of the Smart Campus Resource & Event Management System are:

1. To develop a centralized platform for managing campus resources and events.
2. To provide secure login functionality for administrators and faculty members.
3. To implement role-based access with separate dashboards.
4. To enable faculty members to view available resources and submit booking requests.
5. To allow administrators to manage resources, events, bookings, and equipment inventory.
6. To validate booking dates, time ranges, and resource availability.
7. To prevent conflicting bookings for the same resource.
8. To integrate JavaFX with MySQL using JDBC.
9. To maintain organized and consistent records of campus activities.

## 1.4 Scope of the Project

The scope of the project includes the following modules:

**User Authentication:**

* Login using registered email credentials and passwords.
* Identify users based on their assigned roles.
* Provide access to the appropriate dashboard.

**Administrator Module:**

* Manage campus resources.
* Manage campus events.
* View and manage booking requests.
* Update booking statuses.
* Maintain equipment inventory.

**Faculty Module:**

* View available campus resources.
* Submit resource booking requests.
* View personal booking records.
* Track booking statuses.

**Booking Management Module:**

* Validate booking dates and time ranges.
* Check resource availability.
* Identify scheduling conflicts.
* Manage booking requests and statuses.

**Database Module:**

* Store user information.
* Maintain resource and equipment records.
* Store event and booking information.
* Retrieve and update records using JDBC.

## 1.5 Existing System

In a traditional campus environment, resource booking and event management may depend on manual registers, spreadsheets, or direct communication with administrative staff.

### Disadvantages of the Existing System

* Time-consuming manual operations.
* Increased possibility of human errors.
* Difficulty identifying scheduling conflicts.
* Lack of centralized information.
* Difficulty tracking booking status.
* Inefficient management of equipment records.

## 1.6 Proposed System

The proposed Smart Campus Resource & Event Management System provides a centralized desktop application for managing campus resources, events, equipment, and bookings.

It introduces role-based dashboards, automated booking validation, and database integration to simplify resource management.

### Advantages of the Proposed System

* Centralized resource management.
* Separate dashboards for administrators and faculty.
* Improved booking management.
* Reduced scheduling conflicts through validation.
* Organized equipment inventory.
* Persistent data storage using MySQL.
* Improved accessibility of campus information.

---

# CHAPTER 2: REQUIREMENTS AND FEASIBILITY

## 2.1 Software Requirements

The software requirements define the technologies and tools required to develop and execute the application.

| Software                      | Purpose                                    |
| ----------------------------- | ------------------------------------------ |
| Java 17                       | Core programming language                  |
| JavaFX 21                     | Graphical user interface development       |
| MySQL 8.x                     | Relational database management             |
| MySQL Connector/J 9.4.0       | Java-to-MySQL connectivity                 |
| JDBC                          | Database communication                     |
| Apache Maven 3.10+            | Dependency management and project build    |
| IDE (IntelliJ IDEA / Eclipse) | Application development                    |
| Git and GitHub                | Version control and source code management |

## 2.2 Hardware Requirements

The minimum hardware requirements for running the application are:

| Component        | Requirement                                                |
| ---------------- | ---------------------------------------------------------- |
| Processor        | Intel Core i3 or equivalent                                |
| RAM              | 4 GB minimum                                               |
| Storage          | 500 MB of available space for application and dependencies |
| Operating System | Windows 10/11 or compatible OS                             |
| Display          | Standard graphical display                                 |
| Network          | Required if the MySQL database is hosted on another system |

These are suggested development and execution requirements and may vary depending on the operating environment.

## 2.3 Functional Requirements

Functional requirements describe the operations that the system must perform.

**FR1 – User Authentication**

* The system must allow registered users to log in.
* The system must validate user credentials.
* The system must identify the user's role.

**FR2 – Role-Based Access**

* Administrators must access the administrator dashboard.
* Faculty members must access the faculty dashboard.
* Users must not access functions outside their assigned roles.

**FR3 – Resource Management**

* Administrators must be able to manage campus resources.
* Faculty members must be able to view available resources.

**FR4 – Event Management**

* Administrators must be able to manage campus events.
* Event information must be stored in the database.

**FR5 – Booking Management**

* Faculty members must be able to submit booking requests.
* Administrators must be able to review booking requests.
* Booking statuses must be maintained.

**FR6 – Booking Validation**

* The system must validate booking dates.
* The system must validate start and end times.
* The system must check resource availability.
* The system must identify conflicting bookings.

**FR7 – Equipment Inventory**

* Administrators must be able to manage equipment inventory.
* Equipment information must be maintained in the database.

## 2.4 Non-Functional Requirements

Non-functional requirements define the quality attributes of the application.

**Performance:**
The application should retrieve and display database records efficiently.

**Reliability:**
The system should maintain consistent records and handle database errors appropriately.

**Security:**
The system should authenticate users and restrict access based on assigned roles.

**Usability:**
The JavaFX interface should provide simple navigation and understandable controls.

**Maintainability:**
The application should follow a structured architecture using models, DAO classes, and service classes.

**Scalability:**
The system should support future enhancements, including additional resource categories and user roles.

## 2.5 Feasibility Study

A feasibility study evaluates whether the proposed system can be developed and maintained successfully.

### 2.5.1 Technical Feasibility

The project is technically feasible because it uses established technologies such as Java, JavaFX, MySQL, JDBC, and Maven.

JavaFX provides graphical interface development capabilities, while MySQL supports structured data storage. JDBC enables communication between the application and the database.

The selected technology stack supports the required application functionality.

### 2.5.2 Economic Feasibility

The project is economically feasible because the primary development technologies, including Java, JavaFX, MySQL Community Edition, and Maven, are available without requiring commercial software licenses for basic development.

The application can be developed using existing computer systems, reducing additional infrastructure costs.

### 2.5.3 Operational Feasibility

The system is operationally feasible because it provides a user-friendly graphical interface and separate dashboards for administrators and faculty members.

Users can perform their respective operations through the application without requiring direct interaction with database commands.

### 2.5.4 Schedule Feasibility

The project can be developed incrementally by dividing the work into modules such as authentication, resource management, booking management, database integration, and testing.

This modular approach makes development and testing easier to organize.

---

# CHAPTER 3: SYSTEM ANALYSIS AND DESIGN

## 3.1 System Architecture

The Smart Campus Resource & Event Management System follows a layered architecture to separate user interface operations, business logic, and database operations.

### Architectural Layers

**1. Presentation Layer – JavaFX**

This layer provides the graphical user interface through which administrators and faculty members interact with the application.

It includes:

* Login screen.
* Administrator dashboard.
* Faculty dashboard.
* Resource management screens.
* Event management screens.
* Booking management screens.

**2. Service Layer – Business Logic**

This layer contains the application's business rules and validation operations.

Responsibilities include:

* Authentication-related processing.
* Booking validation.
* Resource availability checking.
* Scheduling conflict detection.
* Booking status management.

**3. Data Access Layer – DAO**

The Data Access Object layer handles database-related operations.

Responsibilities include:

* Inserting records.
* Retrieving records.
* Updating records.
* Deleting records.
* Executing database queries.

**4. Database Connectivity Layer – JDBC**

JDBC provides communication between Java application components and the MySQL database.

**5. Database Layer – MySQL**

MySQL stores the application's persistent information, including users, resources, events, bookings, and equipment records.

### Architecture Diagram

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

## 3.2 System Modules

The system is divided into the following major modules.

### 3.2.1 Authentication Module

The authentication module verifies registered user credentials.

After successful authentication, the system identifies the user's role and redirects the user to the corresponding dashboard.

### 3.2.2 Administrator Module

The administrator module provides centralized control over campus management activities.

Functions include:

* Resource management.
* Event management.
* Booking request management.
* Equipment inventory management.
* Booking status updates.

### 3.2.3 Faculty Module

The faculty module allows faculty members to interact with campus resources.

Functions include:

* Viewing available resources.
* Creating booking requests.
* Viewing personal bookings.
* Tracking booking statuses.

### 3.2.4 Resource Management Module

This module maintains information about campus facilities such as classrooms, laboratories, seminar halls, and other available resources.

Administrators can manage resource information, while faculty members can view resource availability.

### 3.2.5 Event Management Module

This module maintains campus event information.

Administrators can manage event details to ensure that campus activities are organized and accessible through the application.

### 3.2.6 Booking Management Module

This module manages resource booking requests.

It supports request submission, booking validation, status management, and conflict detection.

### 3.2.7 Equipment Inventory Module

This module maintains equipment-related information such as equipment names, quantities, and availability.

Administrators can manage inventory records through the application.

## 3.3 Use Case Design

The system consists of two primary actors.

**Actor 1: Administrator**

The administrator can:

* Log in.
* Access the administrator dashboard.
* Manage resources.
* Manage events.
* View booking requests.
* Update booking statuses.
* Manage equipment inventory.
* Log out.

**Actor 2: Faculty Member**

The faculty member can:

* Log in.
* Access the faculty dashboard.
* View available resources.
* Submit booking requests.
* View personal bookings.
* Track booking statuses.
* Log out.

## 3.4 Booking Workflow

The booking workflow describes how faculty members request campus resources.

1. The faculty member logs into the application.
2. The faculty member opens the resource availability section.
3. The faculty member selects the required resource.
4. The faculty member enters the booking date and time.
5. The system validates the entered information.
6. The system checks for scheduling conflicts.
7. If the requested time is available, the booking request is submitted.
8. The administrator reviews the request.
9. The administrator updates the booking status.
10. The updated status is stored in the database.

## 3.5 Booking Validation Logic

Booking validation is an important part of the system.

The application must verify:

**Date Validation:**
The requested booking date must satisfy the application's booking rules.

**Time Validation:**
The start time must be earlier than the end time.

**Resource Validation:**
The selected resource must exist and be available for booking.

**Conflict Validation:**
The system must check whether another confirmed booking exists for the same resource during an overlapping time period.

A general time-overlap condition is:

`ExistingStart < RequestedEnd AND ExistingEnd > RequestedStart`

If this condition is true for an applicable existing booking, the requested time overlaps with that booking.

The application must apply the appropriate booking-status rules when checking conflicts.

---

# CHAPTER 4: DATABASE DESIGN

## 4.1 Introduction

Database design defines how application data is organized, stored, and maintained.

The Smart Campus Resource & Event Management System uses MySQL 8.x as its relational database management system.

The database is named `smart_campus` and stores information related to users, resources, events, bookings, and equipment inventory.

JDBC is used to perform database operations from the Java application.

## 4.2 Database Objectives

The primary objectives of database design are:

* To maintain centralized application data.
* To minimize unnecessary data duplication.
* To maintain relationships between entities.
* To support efficient data retrieval.
* To maintain data consistency.
* To support booking and resource management operations.

## 4.3 Database Entities

The major entities involved in the system are:

**1. User**

Stores information about registered administrators and faculty members.

Possible attributes:

* User ID
* Name
* Email
* Password Hash
* Role

**2. Resource**

Stores information about campus resources.

Possible attributes:

* Resource ID
* Resource Name
* Resource Type
* Location
* Capacity
* Availability Status

**3. Event**

Stores campus event information.

Possible attributes:

* Event ID
* Event Name
* Event Description
* Event Date
* Start Time
* End Time

**4. Booking**

Stores resource booking requests and their statuses.

Possible attributes:

* Booking ID
* User ID
* Resource ID
* Booking Date
* Start Time
* End Time
* Booking Status

**5. Equipment**

Stores equipment inventory information.

Possible attributes:

* Equipment ID
* Equipment Name
* Quantity
* Availability Status

*Note: The above attributes represent a proposed logical database design. The actual table names and columns should match the SQL schema implemented in the repository.*

## 4.4 Entity Relationships

The major relationships between the entities are:

* A user can submit multiple booking requests.
* A resource can have multiple booking records.
* Each booking request is associated with a user.
* Each booking request is associated with a resource.
* Events are maintained as separate campus activity records.
* Equipment inventory is maintained for campus equipment management.

### Relationship Overview

USER (1) -------- (M) BOOKING

RESOURCE (1) ----- (M) BOOKING

The relationships indicate that a single user or resource can be associated with multiple booking records.

## 4.5 Primary Keys and Foreign Keys

**Primary Key:**

A primary key uniquely identifies each record in a database table.

Examples:

* User ID in the User table.
* Resource ID in the Resource table.
* Event ID in the Event table.
* Booking ID in the Booking table.
* Equipment ID in the Equipment table.

**Foreign Key:**

A foreign key establishes a relationship between two database tables.

Examples:

* User ID in the Booking table references the User table.
* Resource ID in the Booking table references the Resource table.

These constraints help maintain referential integrity.

## 4.6 Database Normalization

Normalization is the process of organizing database tables to reduce redundancy and improve data consistency.

The database design can follow these normal forms:

**First Normal Form (1NF):**
Each column should contain atomic values, and repeating groups should be avoided.

**Second Normal Form (2NF):**
The table should satisfy 1NF, and every non-key attribute should depend on the entire candidate key.

**Third Normal Form (3NF):**
The table should satisfy 2NF and should not contain inappropriate transitive dependencies of non-key attributes on candidate keys.

Applying normalization principles helps maintain organized and consistent application data.

## 4.7 Database Creation and Setup

Create the database using the following SQL command:

```sql
CREATE DATABASE smart_campus;
```

Select the database:

```sql
USE smart_campus;
```

Execute the schema file from the project root:

```bash
mysql -u root -p < database/smart_campus.sql
```

If a separate sample data script exists, execute:

```bash
mysql -u root -p < database/sample_data.sql
```

Sample data is optional and should be loaded only after verifying its compatibility with the database schema.

---

# CHAPTER 5: IMPLEMENTATION

## 5.1 Introduction

Implementation is the process of converting the system design into a working software application.

The Smart Campus Resource & Event Management System is implemented using Java 17, JavaFX 21, MySQL 8.x, JDBC, and Maven.

The application follows a structured architecture to separate interface development, business logic, and database operations.

## 5.2 Technology Stack

| Technology              | Implementation Purpose                            |
| ----------------------- | ------------------------------------------------- |
| Java 17                 | Application logic and object-oriented programming |
| JavaFX 21               | Desktop graphical interface                       |
| MySQL 8.x               | Persistent data storage                           |
| JDBC                    | Database connectivity                             |
| MySQL Connector/J 9.4.0 | MySQL JDBC driver                                 |
| Maven 3.10+             | Dependency management and application build       |

## 5.3 Project Structure

The application follows the following logical project organization:

```text
JAVA-DBMS-PROJECT/
│
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
│
├── database/
│   ├── smart_campus.sql
│   └── sample_data.sql
│
├── pom.xml
├── .gitignore
└── README.md
```

*The structure above is illustrative. Retain only directories and files that actually exist in the repository.*

## 5.4 Description of Packages

**Main Package:**

Contains the main application entry point and JavaFX application initialization.

**DAO Package:**

Contains Data Access Object classes responsible for communicating with the MySQL database.

**Model Package:**

Contains application entity classes representing users, resources, events, bookings, and equipment.

**Service Package:**

Contains business logic, validation operations, and application service methods.

**Util Package:**

Contains utility classes, including database connection management.

## 5.5 User Authentication Implementation

The authentication module allows registered users to log in using their email addresses and passwords.

The system validates the supplied credentials against the stored user information.

After successful authentication, the application identifies the user's role.

* ADMIN users are redirected to the administrator dashboard.
* FACULTY users are redirected to the faculty dashboard.

Invalid credentials are rejected, and an appropriate error message is displayed.

## 5.6 Administrator Dashboard Implementation

The administrator dashboard provides access to the major campus management functions.

The administrator can perform the following operations:

* Add, view, update, and manage campus resources.
* Manage campus events.
* Review faculty booking requests.
* Update booking statuses.
* Manage equipment inventory.

These operations interact with the database through the DAO and service layers.

## 5.7 Faculty Dashboard Implementation

The faculty dashboard provides access to resource booking functionality.

Faculty members can:

* View available campus resources.
* Select a resource for booking.
* Enter booking dates and time ranges.
* Submit booking requests.
* View their personal booking records.
* Track booking statuses.

The system validates booking information before processing the request.

## 5.8 Booking Management Implementation

The booking module manages the complete booking request workflow.

It validates the requested date and time, checks resource availability, and identifies overlapping bookings.

The application processes the request according to the booking rules implemented in the service layer.

Booking information is stored in MySQL through JDBC.

## 5.9 Database Connectivity Implementation

The application uses JDBC to connect Java classes with the MySQL database.

The general JDBC connection process involves:

1. Loading or registering the MySQL JDBC driver as required.
2. Establishing a database connection.
3. Creating prepared statements.
4. Executing SQL queries.
5. Processing query results.
6. Closing database resources.

A database connection URL generally follows this format:

```text
jdbc:mysql://localhost:3306/smart_campus
```

Database credentials should be configured securely and should not be exposed in public source code.

## 5.10 Build and Execution

The project uses Maven for dependency management and execution.

**Compile the Project:**

```bash
mvn clean compile
```

**Run the Application:**

```bash
mvn javafx:run
```

The application entry point is:

```text
com.smartcampus.Main
```

Before launching the application, ensure that MySQL Server is running and the database connection is correctly configured.

---

# CHAPTER 6: VERIFICATION AND TEST PLAN

## 6.1 Introduction

Verification and testing are essential activities performed to ensure that the application behaves according to its requirements.

The testing process checks application functionality, input validation, database connectivity, role-based access, and booking operations.

## 6.2 Testing Objectives

The main objectives of testing are:

* To verify successful and unsuccessful login operations.
* To verify role-based dashboard access.
* To validate resource and event management operations.
* To verify booking request submission.
* To identify scheduling conflicts.
* To verify database connectivity.
* To check input validation.
* To identify and correct application errors.

## 6.3 Types of Testing

### 6.3.1 Unit Testing

Unit testing verifies individual components independently.

Examples:

* Authentication validation methods.
* Booking date validation.
* Time range validation.
* Booking conflict detection.
* DAO operations.

### 6.3.2 Integration Testing

Integration testing verifies the interaction between different application layers.

Examples:

* JavaFX interface and service layer.
* Service layer and DAO layer.
* DAO layer and MySQL database.
* JDBC connection and database operations.

### 6.3.3 System Testing

System testing evaluates the complete application.

It verifies whether the major modules work together as expected.

### 6.3.4 User Interface Testing

UI testing verifies the graphical interface.

It includes:

* Login screen validation.
* Dashboard navigation.
* Form input validation.
* Error message display.
* Table and record display.

## 6.4 Test Cases

The following test cases define the expected behavior of the application.

| Test ID | Test Scenario                                      | Expected Result                          |
| ------- | -------------------------------------------------- | ---------------------------------------- |
| TC01    | Login with valid administrator credentials         | Administrator dashboard should open      |
| TC02    | Login with valid faculty credentials               | Faculty dashboard should open            |
| TC03    | Login with invalid credentials                     | Error message should be displayed        |
| TC04    | Faculty attempts to access administrator functions | Access should be denied                  |
| TC05    | View available resources                           | Available resources should be displayed  |
| TC06    | Submit a valid booking request                     | Booking request should be processed      |
| TC07    | Enter an invalid date                              | Validation error should be displayed     |
| TC08    | Enter an end time earlier than start time          | Booking should be rejected               |
| TC09    | Request an overlapping booking                     | Scheduling conflict should be identified |
| TC10    | Administrator updates booking status               | Updated status should be stored          |
| TC11    | Administrator manages equipment                    | Inventory information should be updated  |
| TC12    | Database connection failure                        | Appropriate error handling should occur  |

## 6.5 Expected Testing Outcomes

The application should satisfy the following conditions:

* Registered users should be able to authenticate successfully.
* Users should be redirected according to their assigned roles.
* Unauthorized operations should be restricted.
* Resource and event information should be managed correctly.
* Booking requests should undergo validation.
* Scheduling conflicts should be identified.
* Database records should be stored and retrieved correctly.
* Invalid inputs should be handled appropriately.

## 6.6 Maven Testing Commands

Compile the application:

```bash
mvn clean compile
```

Execute configured test cases:

```bash
mvn test
```

The Maven test command executes the automated tests configured in the project.

Manual testing should also be performed to verify the complete JavaFX interface and application workflows.

*Note: Test cases in this chapter represent the verification plan. Actual test results should be recorded after executing each test case.*

---

# CHAPTER 7: SECURITY, LIMITATIONS AND MAINTENANCE

## 7.1 Security Considerations

Security is important because the application manages user credentials, campus resources, and booking information.

The following security practices should be considered during implementation.

### 7.1.1 User Authentication

The application verifies registered user credentials before granting access.

### 7.1.2 Role-Based Access Control

The system separates administrator and faculty operations.

Each user should access only the functions permitted by their assigned role.

### 7.1.3 Password Protection

Passwords should be stored using secure password hashing techniques rather than plain-text storage.

### 7.1.4 SQL Injection Prevention

Prepared statements should be used for database operations involving user input.

This helps prevent SQL injection vulnerabilities.

### 7.1.5 Database Credential Protection

Database usernames and passwords should be maintained outside publicly accessible source code.

Sensitive configuration files should be excluded from version control using `.gitignore` where appropriate.

### 7.1.6 Input Validation

The application should validate user inputs before processing them.

Date, time, email, and other required fields should be checked to prevent invalid data from entering the database.

### 7.1.7 Transaction Management

Related database operations should use appropriate transaction handling to maintain data consistency.

For booking operations, concurrency and transaction isolation should be considered to prevent simultaneous requests from creating conflicting confirmed bookings.

## 7.2 Limitations

The current system has certain limitations:

1. The application is designed primarily as a desktop-based solution.
2. It requires a properly configured MySQL database.
3. Its functionality depends on the availability of the database server.
4. It does not inherently provide mobile application support.
5. Real-time notifications are not included unless separately implemented.
6. The system's scalability depends on database configuration and application architecture.
7. Multi-campus management is outside the current project scope.

## 7.3 System Maintenance

Maintenance ensures that the application continues to operate correctly after deployment.

The following maintenance activities are recommended.

**Corrective Maintenance:**

Identify and fix errors discovered during application usage.

**Adaptive Maintenance:**

Update the application when operating systems, Java versions, database versions, or dependencies change.

**Perfective Maintenance:**

Improve the user interface, application performance, and existing functionalities.

**Preventive Maintenance:**

Perform database backups, review application logs, update dependencies, and identify potential problems before they affect users.

## 7.4 Future Maintenance Recommendations

* Regular database backups.
* Periodic security reviews.
* Dependency updates.
* Performance monitoring.
* Database optimization.
* Validation of user permissions.
* Maintenance of project documentation.

---

# CHAPTER 8: CONCLUSION AND FUTURE WORK

## 8.1 Conclusion

The Smart Campus Resource & Event Management System is designed to provide a centralized and structured solution for managing campus resources, events, bookings, and equipment inventory.

The application uses Java 17 and JavaFX 21 to provide a graphical desktop interface, while MySQL 8.x is used for persistent data storage. JDBC establishes communication between the Java application and the database.

The system provides separate dashboards for administrators and faculty members through role-based authentication.

Administrators can manage campus resources, events, booking requests, and equipment inventory. Faculty members can view available resources, submit booking requests, and manage their personal bookings.

The booking validation mechanism checks dates, time ranges, resource availability, and scheduling conflicts to improve the reliability of the booking process.

The layered architecture separates the presentation layer, business logic, and database operations, making the application easier to understand, maintain, and extend.

Overall, the project demonstrates the practical application of Java object-oriented programming concepts, JavaFX interface development, relational database design, SQL operations, and JDBC integration in solving a real-world campus management problem.

## 8.2 Future Enhancements

The system can be enhanced with additional features in future versions.

**1. Web-Based Application**

Develop a web-based version to allow users to access campus services through web browsers.

**2. Mobile Application**

Introduce Android and iOS applications for faculty members and administrators.

**3. Email Notifications**

Implement automated email notifications for booking confirmations, rejections, and event updates.

**4. QR Code Integration**

Introduce QR code-based equipment identification and resource check-in/check-out functionality.

**5. Advanced Analytics Dashboard**

Integrate analytical dashboards to monitor resource utilization, booking trends, equipment usage, and campus activity statistics.

**6. Calendar Integration**

Provide calendar-based resource booking and event visualization.

**7. Automated Booking Approval**

Introduce configurable approval workflows for different categories of campus resources.

**8. Multi-Campus Support**

Extend the application to support multiple departments, buildings, and campus locations.

**9. Audit Logs**

Maintain activity logs for important administrative operations, booking updates, and resource modifications.

**10. Cloud Database Integration**

Explore cloud-based database hosting to support centralized access from multiple systems.

## 8.3 Final Remarks

The Smart Campus Resource & Event Management System provides a foundation for digital transformation in campus administration.

By combining JavaFX, Java, MySQL, and JDBC, the project demonstrates how software engineering and database management concepts can be applied to develop a practical campus management solution.

The modular design and proposed future enhancements provide opportunities to expand the system into a more comprehensive campus management platform.

---

# PROJECT TEAM

**Project Title:** Smart Campus Resource & Event Management System

**Team Members:**

1. Mohamed Rihan
2. Jeenathul Rila
3. Latheef Khan

**Technologies Used:**

* Java 17
* JavaFX 21
* MySQL 8.x
* JDBC
* MySQL Connector/J 9.4.0
* Apache Maven 3.10+

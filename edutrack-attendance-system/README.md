# EduTrack System (Java, JSP, MySQL, Tomcat)

## Overview

EduTrack is a Smart Classroom Attendance and Analytics System designed to automate attendance tracking and provide real-time insights for academic management.

The system replaces manual attendance methods with a digital platform using QR code scanning, geolocation verification, and centralized data processing.

## Technologies Used

* Java (Servlets, JSP)
* Apache Tomcat
* MySQL
* JDBC
* HTML / CSS

## System Architecture

The system follows an N-tier architecture:

* Presentation Layer (JSP UI)
* Application Layer (Servlets & Business Logic)
* Service Layer (Authentication, Attendance Logic)
* Persistence Layer (MySQL Database)

## Features

* QR code and geolocation-based attendance
* Role-based access control (Admin, Lecturer, Student, Coordinator)
* Real-time attendance tracking
* Analytics dashboard and reporting
* Notification system

---

## My Contribution (Admin Module)

I was responsible for designing and implementing the full Admin subsystem.

### 1. Admin Dashboard

* Display system statistics:

  * Total users
  * Total students and lecturers
  * Active courses
* View recent system activity logs
* Real-time data retrieval from database

### 2. User Management

* Add, update, and delete user accounts
* Search and filter users
* Role assignment (Admin / Lecturer / Student / Coordinator)
* Email validation and status control

### 3. Course Management

* Create, update, and delete course records
* Assign lecturers to courses
* Search and sort courses dynamically
* Maintain course data integrity

### 4. System Configuration

* Configure system settings:

  * Late attendance threshold
  * Attendance alert rules
  * Session timeout
* Store system-wide configuration
* Log administrative actions into activity logs

### 5. Database Integration

* Implemented JDBC-based database connection
* Designed SQL queries for CRUD operations
* Ensured data consistency and validation

---

## How to Run

### 1. Start Apache Tomcat

Deploy the project to Tomcat server:

```id="s6shf6"
http://localhost:8080/
```

### 2. Database Setup

* Create database:

```sql id="pdcwht"
CREATE DATABASE edutrackdb;
```

* Import SQL schema (if provided)

### 3. Access System

Open browser:

```id="7cd2kp"
http://localhost:8080/EduTrack
```


---

## Notes

* Developed as a team-based project
* Demonstrates full-stack web development with database integration

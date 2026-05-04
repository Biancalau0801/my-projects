# Parking Lot Management System (Java + SQLite)

## Overview

This project is a parking lot management system developed using Java and SQLite.

The system simulates a real-world parking kiosk with features such as vehicle entry, exit payment, spot management, and administrative control. It supports multiple parking spot types, fine calculation, and transaction tracking.

---

## Technologies Used

* Java (OOP)
* Java Swing (GUI)
* SQLite (Database)
* JDBC

---

## Features

* Vehicle parking (entry system)
* Parking spot allocation and visualization
* Search vehicle by plate number
* Payment and exit system
* Fine calculation:

  * Fixed fine
  * Progressive fine
  * Hourly fine 
* Transaction recording and history tracking
* Admin dashboard for system control
* Real-time parking status updates

---

## System Design

The system follows a layered structure:

* **GUI Layer** → User interface (Java Swing) 
* **Logic Layer** → Core system operations (parking, payment, validation) 
* **Database Layer** → Data persistence using SQLite 

Key entities include:

* ParkingSpot (spot information & status) 
* Ticket (parking session tracking) 
* Vehicle (vehicle details) 

---

## My Contribution

* Implemented parking **insert functionality**:

  * Assign vehicle to available parking spot
  * Generate parking ticket and store entry details
* Implemented **search functionality**:

  * Retrieve vehicle and parking information using plate number
  * Display parking status and related data
* Assisted in **debugging and system improvement**:

  * Fixed logical and runtime issues
  * Improved system stability and data consistency
* Collaborated on integrating database operations with system logic

---

## Project Structure

```text
parking-lot-management-system/
│
├── src/
│   ├── db/
│   ├── logic/
│   ├── model/
│   └── gui/
│
├── database/
│   └── parking_system.db
│
├── lib/
│   └── sqlite-jdbc.jar
│
└── README.md
```

---

## How to Run

1. Make sure Java is installed
2. Include SQLite JDBC library (`sqlite-jdbc.jar`)
3. Compile the project:

   ```
   javac *.java
   ```
4. Run the system:

   ```
   java Main
   ```

---

## Notes

* Developed as a team-based academic project
* Focus on real-world system simulation and database integration
* Demonstrates practical use of OOP, GUI design, and SQL database operations

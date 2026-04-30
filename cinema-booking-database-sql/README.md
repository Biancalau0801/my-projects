# Cinema Booking Database System (SQL)

## Overview

This project is a relational database system designed for a cinema ticket booking platform.
It models real-world booking operations including movies, showtimes, seats, customers, and transactions.

## Technologies Used

* SQL (Relational Database Design)
* ERD Modeling
* Data Normalization

## Database Design

The system consists of multiple related entities:

* Cinema and Hall management
* Movie and Showtime scheduling
* Seat allocation system
* Customer and booking records
* Ticket and food ordering system

## ERD

![ERD](./erd/erd.png)

## Key Features

* Prevents double booking of seats
* Supports multiple tickets per booking
* Tracks payment status and methods
* Handles food ordering linked to bookings
* Maintains data integrity using foreign keys

## My Contribution

* Designed relational database schema and entity relationships
* Created ERD and normalized tables
* Implemented SQL scripts:

  * Table creation (DDL)
  * Data insertion (DML)
* Defined constraints and relationships between entities

## How to Run

1. Open any SQL database system (MySQL / PostgreSQL)
2. Run the SQL script:

```sql
SOURCE sql/database.sql;
```

## Notes

* This project focuses on database design and data integrity.
* The schema follows normalization principles to reduce redundancy.

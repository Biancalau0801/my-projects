# Seminar Management System (Java, Swing, OOP)

## Overview

This project is a role-based Seminar Management System developed using Java and Swing.
It centralizes the management of academic seminar sessions, allowing coordinators, evaluators, and students to interact within a single platform.

## Technologies Used

* Java
* Java Swing (GUI)
* Object-Oriented Programming (OOP)
* File Handling (Local Storage)

## Features

* Role-based system (Student, Evaluator, Coordinator)
* Seminar session scheduling and assignment
* File submission and management system
* Evaluation with rubric scoring
* Automated report generation (schedule, evaluation, awards)
* Real-time status tracking for students

## My Contribution (Student Module)

* Designed and implemented student dashboard:

  * View assigned session details (date, time, venue, evaluator)
  * Display project information (title, supervisor, abstract)

* Developed file submission system:

  * Upload presentation files with timestamp tracking
  * Maintain submission history
  * Allow viewing and accessing uploaded files

* Implemented student data management:

  * Track submission status (Pending / Submitted / Evaluated)
  * Display scores and evaluator remarks
  * Store rubric scores and feedback

* Built user interaction flow:

  * Student login and registration interface
  * Real-time updates of session and evaluation data

## How to Run

```bash
javac *.java
java Main
```

## Project Structure

```text
src/
  Main.java
  LoginFrame.java
  StudentMenu.java
  Student.java
  Submission.java
  Evaluator.java
  Coordinator.java
  Session.java
  ReportGenerator.java
```

## Notes

* The system supports multiple roles with different access levels.
* Student module focuses on submission, tracking, and viewing evaluation results.
* The application uses local file storage for uploaded documents.

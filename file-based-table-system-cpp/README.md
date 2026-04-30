# File-based Table System (C++)

## Overview

This project is a command-line table management system developed in C++.
It allows users to create, read, update, and delete (CRUD) structured data stored in text files.

The system simulates basic database operations without using a database engine, providing a lightweight data management solution.

## Technologies Used

* C++
* File Handling (ifstream / ofstream)
* Standard Template Library (STL)
* Data Structures (vector)

## Features

* Create new table files (.txt)
* Load existing data from .mdb files
* Display table content in CSV format
* Delete specific rows
* Update specific fields
* Insert new rows
* Count number of records

## My Contribution (Update Function)

* Implemented record update functionality:

  * Locate specific record using row ID
  * Display selected record for reference
  * Allow user to choose target column
  * Validate column input
  * Replace existing value with new input

* Designed data update workflow:

  * Modify in-memory data structure (vector)
  * Rewrite updated data back to file
  * Ensure consistency between memory and file storage

* Handled input validation and error checking:

  * Invalid row or column detection
  * Empty table handling
  * Safe update operations

## How to Run

```bash id="9qk12a"
g++ main.cpp -o app
./app
```

## System Workflow

* Main menu:

  1. Create new file
  2. Open existing file
  3. Exit

* Secondary menu:

  1. Delete row
  2. Update field
  3. Count rows
  4. Insert row
  5. Exit

## Project Structure

```text id="1m8n2q"
main.cpp
Flowchart.pdf
User_Documentation.pdf
```

## Notes

* The system uses CSV-style formatting for data storage.
* All operations are performed without external database systems.
* Designed as a lightweight alternative to basic table management.

# Monopoly OS (C, IPC, Concurrency)

## Overview

This project is a multiplayer Monopoly-style terminal game implemented in C. It uses a client-server architecture with shared memory and concurrency control to support real-time gameplay between multiple players.

## Technologies Used

* C Programming
* TCP Socket Programming
* POSIX Shared Memory
* Multithreading (pthread)
* Process Synchronization (mutex, semaphore, condition variable)

## Features

* Multiplayer support (3–5 players)
* Client-server communication using TCP sockets
* Round Robin scheduling for turn management
* Real-time game updates
* Property system (buying, renting, ownership)
* Random events (Community Chest)
* Bankruptcy detection and player elimination
* Persistent score tracking

## My Contribution

* Implemented core game logic:

  * Dice rolling mechanism
  * Property purchasing and rent system
  * Random event handling (Community Chest)
  * Player money updates and bankruptcy detection

* Designed and implemented game state management:

  * Shared memory structure for cross-process communication
  * Player state tracking (position, money, active status)
  * Board initialization and property configuration

* Developed synchronization mechanisms:

  * Mutex and condition variables for turn coordination
  * Ensured safe concurrent access to shared game state

* Contributed to client-side interaction:

  * Handling server messages
  * Processing user input during gameplay

---

## How to Compile and Run

### Compile

```bash
make clean
make
```

### Start Server

```bash
./monopoly_server
```

### Start Clients (3–5 players required)

```bash
./monopoly_client
```

Game starts automatically when at least 3 players connect.

### Logs & Scores (Optional)

```bash
tail -f game.log      # View live game logs
cat scores.txt        # View persistent scores
```

---

## Game Rules Summary

### Objective

Be the last player remaining (all other players go bankrupt).

### Setup

* 20 board spaces (properties + special tiles)
* Each player starts with $500
* Starting position: 0 (“Go”)

### Gameplay

* Turn-based (Round Robin scheduling)
* Press `r` to roll dice (1–6)
* Move forward accordingly

### Property Rules

* Unowned → Buy property
* Owned by others → Pay rent
* Owned by you → No action

### Special Spaces

* Community Chest → Random events
* Tax → Pay fixed amount

### Winning / Losing

* Player is eliminated when money ≤ 0
* Last remaining player wins
* Results stored in `scores.txt`

---

## System Architecture

### Multiplayer Mode

* 3–5 players supported
* TCP/IP socket communication (IPv4, port 8080)

### Concurrency Model

* Hybrid multiprocessing + multithreading
* Server forks process per client
* Scheduler thread → turn control
* Logger thread → event logging
* Synchronization:

  * mutex
  * condition variables
  * semaphores

### Design Notes

* Server enforces all game rules
* Clients handle input/output only
* Thread-safe logging system
* Handles player disconnections automatically

---

## Project Structure

```text
src/
  server.c
  client.c
  game_logic.c
  game_state.c
  scheduler.c
  shared_memory.c
  logger.c
```

## Notes

* Developed as a team-based project
* Focus on concurrency, IPC, and real-time system design

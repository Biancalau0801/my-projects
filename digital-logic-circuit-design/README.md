# Digital Logic Circuit Design (FSM Counter)

## Overview

This project designs a 4-bit counter circuit with one external input using sequential logic.

The circuit follows different state sequences based on the input value:

* When `Input = 0`: `15 → 11 → 2 → 3 → repeat`
* When `Input = 1`: `15 → 1 → 7 → 4 → repeat`

All undesired states are redirected to a fixed recovery state:

* `Input = 0` → undesired states go to `11`
* `Input = 1` → undesired states go to `1`

## Technologies Used

* CircuitVerse
* Digital Logic Design
* Flip-Flops
* Karnaugh Map (K-map)
* Finite State Machine (FSM)

## Features

* 4-bit sequential counter circuit
* External input-controlled state transition
* State recovery for undesired states
* Mixed flip-flop implementation
* Logic simplification using K-map
* Circuit construction and testing in CircuitVerse

## Flip-Flop Design

The circuit uses different flip-flop types:

* MSB Flip-Flop: T Flip-Flop
* LSB Flip-Flop: JK Flip-Flop

## My Contribution

* Worked on the T flip-flop section of the design
* Prepared the T flip-flop excitation logic
* Created the K-map simplification for the T flip-flop output
* Contributed to the state transition table
* Participated in constructing and checking the final CircuitVerse circuit

## Project Files

```text
digital-logic-circuit-design/
│
├── README.md
├── src/
│   └── CAO_Assignment.cv
├── docs/
│   └── CAO_Assignment.pdf
└── design/
    └── circuit.png
```

## Circuit Preview

![Circuit Diagram](./design/circuit.png)

## Notes

* Developed as a team-based academic project
* The `.cv` file can be opened using CircuitVerse
* The circuit screenshot is included for easier viewing on GitHub

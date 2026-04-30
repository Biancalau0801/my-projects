# Petrol Station Simulation (MATLAB / FreeMat)

## Overview

This project is a petrol station simulation model developed using MATLAB (FreeMat).
It simulates real-world vehicle flow and refueling operations under peak and non-peak conditions using probabilistic modeling.

The system analyzes queue behavior, pump utilization, and service efficiency based on different traffic scenarios.

## Technologies Used

* MATLAB / FreeMat
* Simulation Modeling
* Probability & Random Number Generation
* Queueing System Logic

## Features

* Simulation of vehicle arrivals (peak vs non-peak hours)
* Randomized vehicle generation (motorbike, car, lorry)
* Fuel type assignment with probability distribution
* Multi-lane pump allocation system (4 pumps, 2 lanes)
* Dynamic pump selection based on availability
* Event logging for vehicle flow tracking
* Detailed simulation tables and statistics output

## My Contribution (Core Simulation Logic)

* Designed and implemented the full simulation engine:

  * Vehicle arrival modeling using random distributions
  * Peak vs non-peak traffic behavior simulation

* Developed queue and resource allocation logic:

  * Multi-lane system with 4 pumps
  * Selection of earliest available pump
  * Waiting time and queue handling mechanism

* Implemented probabilistic modeling:

  * Inter-arrival time distribution
  * Vehicle type classification
  * Fuel type selection and pricing

* Built performance analysis calculations:

  * Average waiting time
  * Probability of waiting
  * Average service time per pump
  * Total time spent in system

* Generated structured outputs:

  * Event logs (arrival and departure)
  * Simulation tables
  * Summary statistics for analysis

## How to Run

```matlab
simulator(n, p)
```

Example:

```matlab
simulator(50, 1)  % 50 vehicles, peak hour
simulator(50, 0)  % 50 vehicles, non-peak
```

## Parameters

* `n` = number of vehicles
* `p` = traffic condition

  * `0` → Non-peak hour
  * `1` → Peak hour

## Key Concepts

* Discrete-event simulation
* Queueing system modeling
* Random number generation
* Resource allocation optimization

## Notes

* This project focuses on simulation logic and system modeling.
* Documentation and analysis were completed collaboratively as a team.

# ARM Date Validation System (Assembly)

## Overview

This project implements a date validation system using ARM Assembly language.

The program processes multiple 32-bit values stored in memory, where each value represents a date in BCD format (DDMMYYYY). It validates whether each date is correctly formatted and logically valid.

---

## Features

* Store multiple BCD date values in memory
* Validate BCD format (each digit must be between 0–9)
* Extract day, month, and year components
* Validate:

  * Month range (1–12)
  * Day range (1–31)
  * Special cases for 30-day months
  * February rules with leap year handling
* Output validation result:

  * `0x1111` → valid date
  * `0x2222` → invalid date

---

## Key Concepts

* ARM Assembly Programming
* Bit manipulation (LSR, AND)
* Memory addressing
* Loop control
* Conditional branching
* BCD (Binary-Coded Decimal) processing

---

## How It Works

1. Load BCD date values from memory
2. Check each 4-bit digit for BCD validity
3. Extract:

   * Day (DD)
   * Month (MM)
   * Year (YYYY)
4. Validate:

   * Month and day range
   * Leap year rules:

     * Divisible by 400 → leap year
     * Divisible by 100 → not leap year
     * Divisible by 4 → leap year
5. Store validation result back into memory

---

## My Contribution

* Contributed to the implementation of the ARM Assembly program
* Participated in designing the validation algorithm:

  * BCD format checking using bit manipulation
  * Date validation logic (day, month, year)
  * Leap year detection rules
* Assisted in debugging and validating memory outputs
* Contributed to report preparation and explanation

---

## Project Structure

```text
src/
  date_validation.asm
docs/
  report.pdf
```

---

## Notes

* Developed as a team-based project
* Focus on low-level programming and data validation logic
* Demonstrates understanding of bit-level operations and algorithm design

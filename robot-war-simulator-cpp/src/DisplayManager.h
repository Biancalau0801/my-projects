/**********|**********|**********|
 Program: DisplayManager.h 
 Course: OOPDS
 Trimester: 2510
 Name: Abikirthikka
 ID: 242UC24420
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: ABIKIRTHIKKA.TAMIL.ARASAN@student.mmu.edu.my
 Phone: 014-9350432
 **********|**********|**********/
#ifndef DISPLAYMANAGER_H
#define DISPLAYMANAGER_H

#include "battlefield.h"
#include "Constants.h"
#include <iostream>
#include <fstream>
#include <iomanip>
#include <ctime>

class DisplayManager {
private:
    ofstream logFile;
    string logFilename;
    
public:
    DisplayManager() {
        time_t now = time(0);
        tm* ltm = localtime(&now);
        logFilename = Constants::LOG_DIRECTORY + 
                     "battle_log_" + 
                     to_string(1900 + ltm->tm_year) + "_" +
                     to_string(1 + ltm->tm_mon) + "_" +
                     to_string(ltm->tm_mday) + "_" +
                     to_string(ltm->tm_hour) + "_" +
                     to_string(ltm->tm_min) + 
                     Constants::LOG_EXTENSION;
        logFile.open(logFilename);
    }

    ~DisplayManager() {
        if (logFile.is_open()) {
            logFile.close();
        }
    }

    void displayBattlefield(const Battlefield& battlefield) {
        cout << Constants::Colors::CYAN << "\nCurrent Battlefield:\n  ";
        for (int x = 0; x < battlefield.getWidth(); ++x) {
            cout << setw(2) << x % 10;
        }
        cout << Constants::Colors::RESET << "\n";

        for (int y = battlefield.getHeight()-1; y >= 0; --y) {
            cout << Constants::Colors::CYAN << setw(2) << y << Constants::Colors::RESET;
            for (int x = 0; x < battlefield.getWidth(); ++x) {
                Robot* robot = battlefield.getRobotAt(x, y);
                if (robot) {
                    cout << " " << robot->getName()[0];
                } else {
                    cout << " " << Constants::EMPTY_CELL;
                }
            }
            cout << "\n";
        }
        
        logFile << "\nBattlefield State:\n";
        for (int y = battlefield.getHeight()-1; y >= 0; --y) {
            for (int x = 0; x < battlefield.getWidth(); ++x) {
                Robot* robot = battlefield.getRobotAt(x, y);
                logFile << (robot ? robot->getName()[0] : Constants::EMPTY_CELL) << " ";
            }
            logFile << "\n";
        }
    }

    void displayTurnInfo(int turn) {
        string turnHeader = "=== Turn " + to_string(turn) + " ===";
        cout << "\n" << turnHeader << "\n";
        logFile << "\n" << turnHeader << "\n";
    }

    void displayAction(const string& action) {
        cout << "  " << action << "\n";
        logFile << "  " << action << "\n";
    }

    void displayRobotStats(const Robot& robot) {
        cout << "  " << robot.getName() << " - Lives: " << robot.getLives() 
             << ", Shells: " << robot.getShells() << "\n";
        logFile << "  " << robot.getName() << " - Lives: " << robot.getLives() 
                << ", Shells: " << robot.getShells() << "\n";
    }

    void displayGameOver(const vector<Robot*>& robots) {
        int aliveCount = 0;
        string winner;
        
        for (const Robot* robot : robots) {
            if (robot->isAlive()) {
                aliveCount++;
                winner = robot->getName();
            }
        }
        
        if (aliveCount == 1) {
            string message = "Game Over! " + winner + " is the winner!";
            cout << "\n" << message << "\n";
            logFile << "\n" << message << "\n";
        } else {
            string message = "Game Over! Simulation ended with " + to_string(aliveCount) + " robots remaining.";
            cout << "\n" << message << "\n";
            logFile << "\n" << message << "\n";
        }
    }
};
#endif

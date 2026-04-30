/**********|**********|**********|
 Program:Constants.h 
 Course: OOPDS
 Trimester: 2510
 Name: Abikirthikka
 ID: 242UC24420
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: ABIKIRTHIKKA.TAMIL.ARASAN@student.mmu.edu.my
 Phone: 014-9350432
 **********|**********|**********/
 
#ifndef CONSTANTS_H
#define CONSTANTS_H

#include <string>
#include <vector>

using namespace std;

namespace Constants {
    // Battlefield display symbols
    const char EMPTY_CELL = '.';
    const char BORDER_HORIZONTAL = '-';
    const char BORDER_VERTICAL = '|';
    const char BORDER_CORNER = '+';
    
    // Game parameters
    const int DEFAULT_BATTLEFIELD_WIDTH = 80;
    const int DEFAULT_BATTLEFIELD_HEIGHT = 50;
    const int MAX_SIMULATION_STEPS = 300;
    const int INITIAL_SHELLS = 10;
    const int INITIAL_LIVES = 3;
    
    // Probability constants
    const double HIT_PROBABILITY = 0.7;
    const double UPGRADE_PROBABILITY = 0.3;
    
    // Robot type identifiers
    const vector<string> ROBOT_TYPES = {
        "GenericRobot",
        "FireRobot",
        "FastRobot",
        "ReconRobot",
        "HideBot",
        "JumpBot",
        "ScoutBot",
        "TrackBot",
        "LongShotBot",
        "SemiAutoBot",
        "ThirtyShotBot"
    };
    
    // Upgrade types
    namespace Upgrades {
        const string MOVING = "Moving";
        const string SHOOTING = "Shooting";
        const string SEEING = "Seeing";
        
        const vector<string> MOVING_UPGRADES = {"HideBot", "JumpBot"};
        const vector<string> SHOOTING_UPGRADES = {"LongShotBot", "SemiAutoBot", "ThirtyShotBot"};
        const vector<string> SEEING_UPGRADES = {"ScoutBot", "TrackBot"};
    }
    
    // Action costs
    const int MOVE_COST = 0;
    const int LOOK_COST = 0;
    const int FIRE_COST = 1;
    const int THINK_COST = 0;
    
    // Special ability limits
    const int MAX_HIDES = 3;
    const int MAX_JUMPS = 3;
    const int MAX_SCOUTS = 3;
    const int MAX_TRACKERS = 3;
    const int LONG_SHOT_RANGE = 3;
    const int SEMI_AUTO_SHOTS = 3;
    const int THIRTY_SHOT_COUNT = 30;
    
    // Display colors
    namespace Colors {
        const string RESET = "\033[0m";
        const string RED = "\033[31m";
        const string GREEN = "\033[32m";
        const string YELLOW = "\033[33m";
        const string BLUE = "\033[34m";
        const string MAGENTA = "\033[35m";
        const string CYAN = "\033[36m";
        const string WHITE = "\033[37m";
    }
    
    // Log file settings
    const string LOG_DIRECTORY = "logs/";
    const string LOG_EXTENSION = ".txt";
};
#endif

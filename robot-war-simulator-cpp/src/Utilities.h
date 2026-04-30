/**********|**********|**********|
 Program: Utilities.h 
 Course: OOPDS
 Trimester: 2510
 Name: Abikirthikka
 ID: 242UC24420
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: ABIKIRTHIKKA.TAMIL.ARASAN@student.mmu.edu.my
 Phone: 014-9350432
 **********|**********|**********/
#ifndef UTILITIES_H
#define UTILITIES_H

#include "Constants.h"
#include "battlefield.h"
#include <iostream>
#include <vector>
#include <string>
#include <cstdlib>
#include <ctime>
#include <sstream>
#include <fstream>


using namespace std;

namespace Utilities {
    inline void initRandom() {
        srand(static_cast<unsigned>(time(nullptr)));
    }
    
    inline int randomInt(int min, int max) {
        return min + (rand() % (max - min + 1));
    }
    
    inline bool isInBounds(int x, int y, const Battlefield& battlefield) {
        return x >= 0 && x < battlefield.getWidth() && 
               y >= 0 && y < battlefield.getHeight();
    }
    
    inline int calculateDistance(const Position& pos1, const Position& pos2) {
        int dx = pos1.robotPositionX - pos2.robotPositionX;
        int dy = pos1.robotPositionY - pos2.robotPositionY;
        return abs(dx) + abs(dy);
    }
    
    enum Direction {
        UP, UP_LEFT, UP_RIGHT, 
        LEFT, RIGHT, 
        DOWN, DOWN_LEFT, DOWN_RIGHT
    };
    
    inline Position directionToOffset(Direction dir) {
        switch(dir) {
            case UP: return {0, 1};
            case UP_LEFT: return {-1, 1};
            case UP_RIGHT: return {1, 1};
            case LEFT: return {-1, 0};
            case RIGHT: return {1, 0};
            case DOWN: return {0, -1};
            case DOWN_LEFT: return {-1, -1};
            case DOWN_RIGHT: return {1, -1};
            default: return {0, 0};
        }
    }
    
    inline bool parseInputFile(const string& filename, 
                             int& width, int& height,
                             int& maxSteps,
                             vector<pair<string, Position>>& robotInitials) {
        ifstream file(filename);
        if (!file.is_open()) {
            cerr << "Error: Could not open input file " << filename << endl;
            return false;
        }
        
        string line;
        while (getline(file, line)) {
            if (line.find("M by N") != string::npos) {
                string dummy;
                istringstream iss(line);
                iss >> dummy >> dummy >> dummy >> width >> height;
            } 
            else if (line.find("steps:") != string::npos) {
                istringstream iss(line);
                string dummy;
                iss >> dummy >> maxSteps;
            }
            else if (line.find("robots:") != string::npos) {
                continue;
            }
            else if (!line.empty() && isalpha(line[0])) {
                istringstream iss(line);
                string type, name, xStr, yStr;
                iss >> type >> name >> xStr >> yStr;
                
                Position pos;
                if (xStr == "random") {
                    pos.robotPositionX = randomInt(0, width-1);
                } else {
                    pos.robotPositionX = stoi(xStr);
                }
                
                if (yStr == "random") {
                    pos.robotPositionY = randomInt(0, height-1);
                } else {
                    pos.robotPositionY = stoi(yStr);
                }
                
                robotInitials.emplace_back(type + " " + name, pos);
            }
        }
        
        file.close();
        return true;
    }
};
#endif

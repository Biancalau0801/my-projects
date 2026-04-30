#ifndef BATTLEFIELD_H
#define BATTLEFIELD_H

#include "position.h"
#include "robot.h"
#include <vector>
#include <string>

class Robot; // Forward declaration

// This class represents the battlefield grid where robots will be placed and move around
class Battlefield {
public:
    // Constructor: Initializes battlefield with given width and height
    Battlefield(int width, int height);

    ~Battlefield();

    // Displays the battlefield grid to the console
    void display() const;

    // Checks if a given (x, y) coordinate is within the battlefield bounds
    bool isInBounds(int x, int y) const {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    // Places a robot character on the battlefield at a specific coordinate
    void placeRobot(Robot* robot, int x, int y);

    void placeRobotRandomly(Robot* robot);

    // Update robot move position
    void moveRobot(Robot* robot, int newX, int newY);

    // Logs the results or final state of the battlefield to a file
    void logResults(const std::string& filename) const;

    // Getter for battlefield width
    int getWidth() const { return width; }

    // Getter for battlefield height
    int getHeight() const { return height; }

    //for upgrades
    Robot* getRobotAt(int x, int y) const {
       if(!isInBounds(x, y))
          return nullptr;

       return grid[x][y];
    }

    bool isValidMove(const Position& pos);

    void revealAllToRobot(Robot* robot);

    Robot* getRobotByName(const std::string& name);

    //To get all robots
    const std::vector<Robot*>& getRobots() const { return robots; }

    Position findRandomEmptyPosition() const;

    bool isEmpty(int x, int y) const;

    void removeRobot(Robot* robot);

private:
    int width;  // Width of the battlefield grid (number of columns)
    int height; // Height of the battlefield grid (number of rows)

    // 2D robot pointer table to indicate which robot object is placed in the grid on the battlefield.
    std::vector<std::vector<Robot*>> grid;

    std::vector<Robot*> robots;
};

#endif





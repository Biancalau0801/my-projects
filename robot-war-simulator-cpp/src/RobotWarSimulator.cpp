/**********|**********|**********|
Program: RobotWarSimulator.cpp
Course: OOPDS
Trimester: 2510
Name: Sharveeny
ID: 242UC241DD
Lecture Section: TC1L
Tutorial Section: TT2L
Email: SHARVEENY.KALAI.KUMAR@student.mmu.edu.my
Phone: 0174081106
**********|**********|**********/

#include <iostream>
#include <vector>
#include <string>
#include <cstdlib>
#include <ctime>
#include <fstream>
#include <sstream>

#include "robot.h"
#include "firerobot.h"
#include "reconrobot.h"
#include "fastrobot.h"

#include "JumpBot.h"
#include "HideBot.h"
#include "scoutbot.h"
#include "trackbot.h"
#include "LongShotBot.h"
#include "SemiAutoBot.h"
#include "thirtyshotbot.h"

#include "battlefield.h"
#include "Constants.h"
#include "genericrobot.h"
#include "Utilities.h"
using namespace std;

int M, N, MAX_STEPS;
class Robot; // Forward declaration
vector<vector<char>> battlefield;
vector<Robot*> robots;

Position findRandomEmptyPosition(int rows, int cols, const vector<vector<char>>& battlefield) {
    Position pos;
    do {
        pos.robotPositionX = rand() % rows;
        pos.robotPositionY = rand() % cols;
    } while (battlefield[pos.robotPositionX][pos.robotPositionY] != '.');
    return pos;
}

const vector<string> GenericRobot::firstUpgrades = {
    "TrackBot", "ScoutBot", "SemiAutoBot", "ThirtyShotBot", "LongShotBot", "JumpBot", "HideBot"
};

const vector<string> GenericRobot::secondUpgrades = {
    // Moving → Shooting
    "HideLongShotBot", "HideSemiAutoBot", "HideThirtyShotBot", "JumpLongShotBot", "JumpSemiAutoBot", "JumpThirtyShotBot",
    // Moving → Seeing
    "HideScoutBot", "HideTrackBot", "JumpScoutBot", "JumpTrackBot",
    // Shooting → Moving
    "LongShotHideBot", "LongShotJumpBot", "SemiAutoHideBot", "SemiAutoJumpBot", "ThirtyShotHideBot", "ThirtyShotJumpBot",
    // Shooting → Seeing
    "LongShotScoutBot", "LongShotTrackBot", "SemiAutoScoutBot", "SemiAutoTrackBot", "ThirtyShotScoutBot", "ThirtyShotTrackBot",
    // Seeing → Moving
    "ScoutHideBot", "ScoutJumpBot", "TrackHideBot", "TrackJumpBot",
    // Seeing → Shooting
    "ScoutLongShotBot", "ScoutSemiAutoBot", "ScoutThirtyShotBot", "TrackLongShotBot", "TrackSemiAutoBot", "TrackThirtyShotBot"
};

const vector<string> GenericRobot::thirdUpgrades = {
    "HideLongShotScoutBot", "HideLongShotTrackBot", "HideSemiAutoScoutBot", "HideSemiAutoTrackBot", "HideThirtyShotScoutBot", "HideThirtyShotTrackBot",
    "JumpLongShotScoutBot", "JumpLongShotTrackBot", "JumpSemiAutoScoutBot", "JumpSemiAutoTrackBot", "JumpThirtyShotScoutBot", "JumpThirtyShotTrackBot",
    "HideScoutLongShotBot", "HideScoutSemiAutoBot", "HideScoutThirtyShotBot", "HideTrackLongShotBot", "HideTrackSemiAutoBot", "HideTrackThirtyShotBot",
    "JumpScoutLongShotBot", "JumpScoutSemiAutoBot", "JumpScoutThirtyShotBot", "JumpTrackLongShotBot", "JumpTrackSemiAutoBot", "JumpTrackThirtyShotBot",
    "LongShotHideScoutBot", "LongShotHideTrackBot", "LongShotJumpScoutBot", "LongShotJumpTrackBot", "SemiAutoHideScoutBot", "SemiAutoHideTrackBot",
    "SemiAutoJumpScoutBot", "SemiAutoJumpTrackBot", "ThirtyShotHideScoutBot", "ThirtyShotHideTrackBot", "ThirtyShotJumpScoutBot", "ThirtyShotJumpTrackBot",
    "LongShotScoutHideBot", "LongShotScoutJumpBot", "LongShotTrackHideBot", "LongShotTrackJumpBot", "SemiAutoScoutHideBot", "SemiAutoScoutJumpBot",
    "SemiAutoTrackHideBot", "SemiAutoTrackJumpBot", "ThirtyShotScoutHideBot", "ThirtyShotScoutJumpBot", "ThirtyShotTrackHideBot", "ThirtyShotTrackJumpBot",
    "ScoutHideLongShotBot", "ScoutHideSemiAutoBot", "ScoutHideThirtyShotBot", "ScoutJumpLongShotBot", "ScoutJumpSemiAutoBot", "ScoutJumpThirtyShotBot",
    "TrackHideLongShotBot", "TrackHideSemiAutoBot", "TrackHideThirtyShotBot", "TrackJumpLongShotBot", "TrackJumpSemiAutoBot", "TrackJumpThirtyShotBot",
    "TrackLongShotHideBot", "TrackLongShotJumpBot", "TrackSemiAutoHideBot", "TrackSemiAutoJumpBot", "TrackThirtyShotHideBot", "TrackThirtyShotJumpBot",
    "ScoutLongShotHideBot", "ScoutLongShotJumpBot", "ScoutSemiAutoHideBot", "ScoutSemiAutoJumpBot", "ScoutThirtyShotHideBot", "ScoutThirtyShotJumpBot"
};

void displayBattlefield() {
    cout << "\nBattlefield:\n";
    for (int i = 0; i < M; ++i) {
        for (int j = 0; j < N; ++j) {
            cout << battlefield[i][j] << ' ';
        }
        cout << endl;
    }
}

void simulate() {
    for (int step = 0; step < MAX_STEPS; ++step) {
        cout << "\n--- Turn " << step + 1 << " ---\n";
        displayBattlefield();

        for (Robot* r : robots) {
            if (r->isAlive()) {
                // Try to call performTurn() if available
                if (auto gr = dynamic_cast<GenericRobot*>(r)) {
                    gr->performTurn();
                
                }
            }
        }

        int aliveCount = 0;
        for (Robot* r : robots) if (r->isAlive()) aliveCount++;
        if (aliveCount <= 1) break;
    }
    cout << "\nSimulation Ended.\n";
}

int main() {
    srand(time(0));
    ifstream configFile("input.txt");
    if (!configFile) {
        cerr << "Failed to open input.txt. Please make sure the file exists.\n";
        return 1;
    }

    string line;
    int robotCount = 0;
    Battlefield* bf = nullptr;

    while (getline(configFile, line)) {
        istringstream iss(line);
        string label;

        if (line.find("M by N") != string::npos) {
            string dummy;
            iss >> dummy >> dummy >> M >> N;
        } else if (line.find("steps:") != string::npos) {
            iss >> label >> MAX_STEPS;
        } else if (line.find("robots:") != string::npos) {
            iss >> label >> robotCount;
        } else if (!line.empty() && isalpha(line[0])) {
            string type, name, xStr, yStr;
            iss >> type >> name >> xStr >> yStr;

            int x = (xStr == "random") ? rand() % M : stoi(xStr);
            int y = (yStr == "random") ? rand() % N : stoi(yStr);

            if (type == "GenericRobot") {
                robots.push_back(new GenericRobot(name, x, y, N, M, bf));
            } else if (type == "FireRobot") {
                robots.push_back(new FireRobot(name, x, y, N, M, bf));
            } else if (type == "ScoutBot") {
                robots.push_back(new ScoutBot(name, x, y, N, M, bf));
            } else if (type == "ReconRobot") {
                robots.push_back(new ReconRobot(name, x, y, N, M, bf));
            } else if (type == "FastRobot") {
                robots.push_back(new FastRobot(name, x, y, N, M, bf));
            } else {
                cerr << "Unknown robot type: " << type << endl;
            }

            cout << "[DEBUG] Added robot: " << name << " (" << type << ") at (" << x << ", " << y << ")\n";
        }
    }

    battlefield = vector<vector<char>>(M, vector<char>(N, '.'));

    for (Robot* r : robots) {
        Position p = r->getPosition();
        battlefield[p.robotPositionX][p.robotPositionY] = r->getName()[0];
    }

    simulate();

    for (Robot* r : robots) delete r;
    return 0;
}

// ---- Battlefield method implementations ----

Battlefield::Battlefield(int width, int height)
    : width(width), height(height)
{
    grid.resize(width, std::vector<Robot*>(height, nullptr));
}

Battlefield::~Battlefield()
{
    for (Robot* robot : robots)
    {
        delete robot;
    }
}

void Battlefield::display() const
{
    for (int y = 0; y < height; y++)
    {
        for (int x = 0; x < width; x++)
        {
            if (grid[x][y] == nullptr)
                cout << ". ";
            else
                cout << grid[x][y]->getName()[0] << " ";
        }
        cout << endl;
    }
}

void Battlefield::placeRobot(Robot* robot, int x, int y)
{
    grid[x][y] = robot;
    robots.push_back(robot);
    robot->setPosition(x, y);
}

void Battlefield::placeRobotRandomly(Robot* robot)
{
    Position pos = findRandomEmptyPosition();
    placeRobot(robot, pos.robotPositionX, pos.robotPositionY);
}

void Battlefield::moveRobot(Robot* robot, int newX, int newY)
{
    Position currentPos = robot->getPosition();
    grid[currentPos.robotPositionX][currentPos.robotPositionY] = nullptr;
    grid[newX][newY] = robot;
    robot->setPosition(newX, newY);
}

void Battlefield::logResults(const std::string& filename) const
{
    ofstream file(filename);
    if (!file.is_open())
    {
        cerr << "Cannot open file " << filename << " for writing!" << endl;
        return;
    }

    for (int y = 0; y < height; y++)
    {
        for (int x = 0; x < width; x++)
        {
            if (grid[x][y] == nullptr)
                file << ". ";
            else
                file << "R ";
        }
        file << endl;
    }
    file.close();
}

Robot* Battlefield::getRobotAt(int x, int y) const
{
    if (!isInBounds(x, y))
        return nullptr;
    return grid[x][y];
}

Robot* Battlefield::getRobotAt(int x, int y)
{
    if (!isInBounds(x, y))
        return nullptr;
    return grid[x][y];
}

bool Battlefield::isValidMove(const Position& pos)
{
    return isInBounds(pos.robotPositionX, pos.robotPositionY) && isEmpty(pos.robotPositionX, pos.robotPositionY);
}

void Battlefield::revealAllToRobot(Robot* robot)
{
    cout << "Revealing all robots to " << robot->getName() << ":" << endl;
    for (Robot* r : robots)
    {
        cout << r->getName() << " at (" << r->getX() << ", " << r->getY() << ")" << endl;
    }
}

Robot* Battlefield::getRobotByName(const std::string& name)
{
    for (Robot* r : robots)
    {
        if (r->getName() == name)
            return r;
    }
    return nullptr;
}

Position Battlefield::findRandomEmptyPosition() const
{
    Position pos;
    do
    {
        pos.robotPositionX = rand() % width;
        pos.robotPositionY = rand() % height;
    } while (!isEmpty(pos.robotPositionX, pos.robotPositionY));
    return pos;
}

bool Battlefield::isEmpty(int x, int y) const
{
    if (!isInBounds(x, y)) return false;
    return grid[x][y] == nullptr;
}

void Battlefield::removeRobot(Robot* robot)
{
    Position pos = robot->getPosition();
    if (isInBounds(pos.robotPositionX, pos.robotPositionY))
        grid[pos.robotPositionX][pos.robotPositionY] = nullptr;
}

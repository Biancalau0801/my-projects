/**********|**********|**********|
 Program: robot.h
 Course: OOPDS
 Trimester: 2510
 Name: Bianca Lau Ying Xuan
 ID: 242UC2426R
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: BIANCA.LAU.YING@student.mmu.edu.my
 Phone: 010-2752246
  **********|**********|**********/

#ifndef ROBOT_H
#define ROBOT_H

#include "Constants.h"
#include "position.h"
#include "battlefield.h"
#include <iostream>
#include <cstdlib>
#include <ctime>
#include <vector>
#include <string>
using namespace std;

class Battlefield; // Forward declaration

// base class Robot
class Robot
{
    protected:
       Position pos;       //current position of the robot
       std::string name;   // name of the robot
       int lives;          // number of lives
       bool alive;         // is the robot alive
       int shells;         // number of shells
       int upgradeLevel;   // upgrade level of the robot
       int battlefieldWidth;
       int battlefieldHeight;

    public:
       // Default constructor
       Robot()
           : name("Default"), pos{0, 0}, lives(3), shells(10), upgradeLevel(0),
             battlefieldWidth(1), battlefieldHeight(1), alive(true) {}

       //constructor for set initial values
       Robot(const std::string& name, int startX, int startY, int w, int h)
    : name(name),
      pos{startX, startY},
      lives(3),
      shells(10),
      upgradeLevel(0),
      battlefieldWidth(w),
      battlefieldHeight(h),
      alive(true){}


      // Set the robot's position for battlefield control
      void setPosition(int x, int y)
      {
        pos.robotPositionX = x;
        pos.robotPositionY = y;
      }


       Position getPosition() const
       {
           return pos;
       }

       int getX() const
       {
           return pos.robotPositionX;
       }

       int getY() const
       {
           return pos.robotPositionY;
       }

       std::string getName() const
       {
           return name;
       }

       int getLives() const
       {
           return lives;
       }

       bool isAlive() const
       {
           return alive;
       }

       int getShells() const
       {
           return shells;
       }

       int getUpgradeLevel() const
       {
           return upgradeLevel;
       }

       //reduce lives when damage
       void takeDamage(const std::string& attackerName, Battlefield* battlefield)
       {
            lives--;            // reduce lives by 1 if health < 0
            if (lives > 0)
            {
                std::cout << attackerName << "hit" << name << "." << std::endl
                << name << "has" << lives << "lives left." << std::endl ;
                respawnRandom(battlefield);
            }
            else
            {
                alive = false;
                std::cout << attackerName << "killed" << name << "!" << std::endl ;
            }

       }

       // respawn robot randomly
       void respawnRandom(Battlefield*battlefield)
       {
           alive= true;
           shells = 10;
           battlefield -> removeRobot(this);   //remove robot from old position
           Position newPos= battlefield -> findRandomEmptyPosition();   // find empty position From battlefield.h
           battlefield -> placeRobot(this,newPos.robotPositionX,newPos.robotPositionY); //put robot to new position
           setPosition (newPos.robotPositionX, newPos.robotPositionY);  //update position

           std:: cout <<name << "has respawned at (" << pos.robotPositionX
           << ", " << pos.robotPositionY << "). " << endl;
       }
       virtual void think() = 0; // pure virtual function for thinking logic
       virtual ~Robot() = default; // virtual destructor
};

class MovingRobot : virtual public Robot {
protected:
    int moveRange = 1;   // Default move range
    int jumpCount = 0;   // Number of jumps available
    int hideCount = 0;   // Number of hides available

public:
    // Move the robot to a new position (x, y) if within moveRange
    virtual void move(Battlefield& battlefield, int x, int y) = 0;

    // Check if the robot can jump
    virtual bool canJump() const { return jumpCount > 0; }

    // Perform a jump to a new position if allowed
    virtual void jump(Battlefield& battlefield, int x, int y) {
        if (canJump()) {
            setPosition(x, y);
            jumpCount--;
        }
    }

    // Check if the robot can hide
    virtual bool canHide() const { return hideCount > 0; }

    virtual ~MovingRobot() {}
};

class ShootingRobot: virtual public Robot {
protected:
    int shells = 10;
    int fireRange = 1;
    bool semiAuto = false;
public:
    // Check if the robot can fire
    virtual bool canFire() const { return shells > 0; }
    // Fire at a target position (x, y) on the battlefield
    virtual void fire(Battlefield& battlefield, int x, int y) = 0;
    // Get the number of shells left
    int getShells() const { return shells; }
    virtual ~ShootingRobot() {}
};

class SeeingRobot : virtual public Robot {
protected:
    int visionRange = 1;
    int scoutCount = 0;
    int trackers = 0;
    std::vector<std::pair<int, int>> trackedRobots;
public:
    // Look at a position offset from the robot's current position
    virtual void look(Battlefield& battlefield, int offsetX, int offsetY) = 0;
    
    // Check if the robot can scout
    virtual bool canScout() const { return scoutCount > 0; }
    // Perform a scout action
    virtual void scout(Battlefield& battlefield) {}
    // Check if the robot can track
    virtual bool canTrack() const { return trackers > 0; }
    // Track another robot by name
    virtual void track(Battlefield& battlefield, const std::string& targetName) {}
    virtual ~SeeingRobot() {}
};

class ThinkingRobot : virtual public Robot {
protected:
    vector<string> upgrades;
public:
    // Analyse the battlefield
    virtual void analyse(Battlefield& battlefield) {}
    // Decide the next action
    virtual void decideAction(Battlefield& battlefield) {}
    // Get available upgrades
    virtual vector<string> getAvailableUpgrades() const { return upgrades; }
    virtual void think() = 0;
    virtual ~ThinkingRobot() {}
};


#endif

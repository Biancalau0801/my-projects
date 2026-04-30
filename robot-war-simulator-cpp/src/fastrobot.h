#ifndef FASTROBOT_H
#define FASTROBOT_H

#include "robot.h"
#include "battlefield.h"
#include "ScoutBot.h"
#include "Constants.h"

#include <iostream>
#include <cstdlib>
#include <ctime>
#include <vector>
#include <string>
using namespace std;

// can look bigger (5x5) and only move 2 direction (left and right )
class FastRobot: public virtual Robot, public SeeingRobot, public MovingRobot
{
    private:
        Battlefield* battlefield;   // to access robot position

        bool hasLooked;
        bool hasMoved;

    public:
        // constructor
        FastRobot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : Robot(name, startX, startY, w, h), battlefield(battlefield)
        {
            battlefield -> placeRobot(this, startX, startY);// put inside battlefield and set initial position
            resetTurnFlags();
        }

        void resetTurnFlags()
        {
            hasLooked = false;
            hasMoved = false;
        }

        // each round(step) action execution, cannot repeat, follow look-move
        void performTurn() {
            resetTurnFlags();

            // 1. look
            if (!hasLooked) {
                int lookX = (rand()%3) -1;
                int lookY = (rand()%3) -1;
                look(*battlefield, lookX, lookY);
                hasLooked = true;
            }

            // 2. Move
            if (!hasMoved) {
                int direction = rand()%2;
                Position newPos = calNewPosition(direction);
                move(*battlefield, newPos.robotPositionX, newPos.robotPositionY);
                hasMoved = true;
            }
        }



       void look(Battlefield& battlefield, int offsetX, int offsetY) override {
           int centerX = pos.robotPositionX + offsetX;
           int centerY = pos.robotPositionY + offsetY;
           cout << name << " is looking at area centered at (" << centerX << ", " << centerY << ")." << endl;
           for (int i = -2; i <= 2; ++i) {
               for (int j = -2; j <= 2; ++j) {
                   int lookX = centerX + i;
                   int lookY = centerY + j;
                   if (lookX >= 0 && lookX < battlefield.getWidth() &&
                       lookY >= 0 && lookY < battlefield.getHeight()) {
                       Robot* r = battlefield.getRobotAt(lookX, lookY);
                       if (r != nullptr) {
                           cout << " - Robot " << r->getName() << " at (" << lookX << ", " << lookY << ")" << endl;
                       }
                   }
               }
           }
       }

       Position calNewPosition(int direction)   // calculate new position based on direction
       {
           Position newPos = pos;      //start with current position
           switch (direction)
           {
               case 0: // Left
                   newPos.robotPositionX-=1;
                   break;
               case 1: // Right
                   newPos.robotPositionX+=1;
                   break;
               default:
                   break;
           }
           return newPos;
       }

       // Check if the new position is inside battlefield
       bool isValidMove(Position p)
       {
           return (p.robotPositionX >= 0 && p.robotPositionX < battlefield ->getWidth() &&
                   p.robotPositionY >= 0 && p.robotPositionY < battlefield ->getHeight());
       }

       virtual void move(Battlefield& battlefield, int x, int y) override
       {
           Position newPos = {x, y};
           if (isValidMove(newPos))
           {
               battlefield.removeRobot(this); //remove the old position
               battlefield.placeRobot(this, newPos.robotPositionX, newPos.robotPositionY); //place the new position
               setPosition(newPos.robotPositionX, newPos.robotPositionY);
               cout << name << " moved to (" << getX() << ", " << getY() << ")." << endl;
           }
           else
           {
               cout << name << " invalid move, keep position (" << getX() << ", " << getY() << ")." << endl;
           }
       }

       void displayStats() const {
        cout << "FastRobot: " << name << " at (" << pos.robotPositionX << ", " << pos.robotPositionY << ")\n";
    }
       void think() override {
        // FastRobot does not use advanced thinking logic
        // Could be used for future upgrades
    }

    void fire(Battlefield& battlefield, int x, int y) {
        // FastRobot does not shoot, so this is a no-op
    }
       };

#endif

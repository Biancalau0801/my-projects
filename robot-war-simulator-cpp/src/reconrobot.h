/**********|**********|**********|
 Program: reconrobot.h
 Course: OOPDS
 Trimester: 2510
 Name: Bianca Lau Ying Xuan
 ID: 242UC2426R
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: BIANCA.LAU.YING@student.mmu.edu.my
 Phone: 010-2752246
  **********|**********|**********/

#ifndef RECONROBOT_H
#define RECONROBOT_H

#include "robot.h"
#include "battlefield.h"
#include "Constants.h"

#include <iostream>
#include <cstdlib>
#include <ctime>
#include <vector>
#include <string>

// look remain, 50% sucess to hit and only can move 4 directions (up, down, left, right)
class ReconRobot: public Robot, public SeeingRobot, public ShootingRobot, public MovingRobot
{
    private:
        Battlefield* battlefield;   // to access robot position

        bool hasLooked;
        bool hasFired;
        bool hasMoved;

    public:
        // constructor
        ReconRobot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : Robot(name, startX, startY, w, h), battlefield(battlefield)
        {
            battlefield -> placeRobot(this, startX, startY);// put inside battlefield and set initial position
            resetTurnFlags();
        }

        void resetTurnFlags()
        {
            hasLooked = false;
            hasFired = false;
            hasMoved = false;
        }

        // each round(step) action execution, cannot repeat, follow look-fire-move
        void performTurn()
        {
            resetTurnFlags();

            // 1. look
            if (!hasLooked)
            {
                int lookX = (rand()%3) -1;
                int lookY = (rand()%3) -1;
                look(lookX, lookY);
                hasLooked = true;
            }

            // 2. Fire
            if (!hasFired)
            {
                int fireX, fireY;
                do
                {
                    fireX = (rand()%3) -1;
                    fireY = (rand()%3) -1;
                }
                while (fireX == 0 && fireY == 0);
                fire(fireX, fireY);
                hasFired = true;
            }

            // 3. Move
            if (!hasMoved)
            {
                int direction = rand()%4;
                move(direction);
                hasMoved = true;
            }
        }


       //look around in the center position
       virtual void look(int x, int y)
       {
           int centerX = pos.robotPositionX + x;   // center x and y
           int centerY = pos.robotPositionY + y;

           std::cout << name << " is looking at area centered at (" << centerX << ", " << centerY << ")."<< endl;

           for (int i = -1; i <= 1; ++i)   // check surrounding position
           {
               for (int j = -1; j <= 1; ++j)
               {
                   int lookX = centerX + i;
                   int lookY = centerY + j;

                   if (lookX >=0 && lookX < battlefield ->getWidth() &&     //// check if the position is inside battlefield
                       lookY >=0 && lookY < battlefield ->getWidth())
                   {
                       Robot* r = battlefield -> getRobotAt(lookX, lookY);
                       if (r!= nullptr)
                       {
                           std::cout << " - Robot " << r -> getName() << " at (" << lookX << ", " << lookY << ")" << endl;
                       }
                   }
               }
           }
       }

        // shoot the robot in the direction of x, y
        virtual void fire(int x, int y) override
       {
           if (shells <=0)
           {
               return;      // no shells left, cannot shoot
           }

          if (x < -1 || x > 1 || y < -1 || y > 1 || (x == 0 && y == 0))
          {
              return;                 //cannot shoot at self and out of range
          }

           shells--;                  // reduce shells by 1 when used

           // calculate target position
           int targetX = pos.robotPositionX + x;
           int targetY = pos.robotPositionY + y;

           // check if target position is inside battlefield
           if (targetX < 0 || targetX >= battlefield -> getWidth() ||
               targetY < 0 || targetY >= battlefield -> getHeight())
               {
                   return;
               }

            Robot* target = battlefield -> getRobotAt(targetX,targetY);
            if (target != nullptr && target -> isAlive())        // robot not null and alive
            {
                if(rand() % 100 < 50)
                {
                    std::cout << name << " successfully hit " << target -> getName() << "." << endl;
                    target -> takeDamage(name, battlefield);
                }
                else
                {
                    std::cout << name << " missed the shot at " << target -> getName() << "." << endl;
                }
            } else
            {
                std::cout << name << " fired at (" << targetX << ", " << targetY << ") but no target." << endl;    //when the target is null and the target robot not alive
            }
        }


       Position calNewPosition(int direction)   // calculate new position based on direction
       {
           Position newPos = pos;      //start with current position
           switch (direction)
           {
               case 0: // Up
                   newPos.robotPositionY+= 1;
                   break;
               case 1: // Left
                   newPos.robotPositionX-=1;
                   break;
               case 2: // Right
                   newPos.robotPositionX+=1;
                   break;
               case 3: // Down
                  newPos.robotPositionY-=1;
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
                   p.robotPositionY >= 0 && p.robotPositionY < battlefield->getHeight());
       }

       virtual void move(int direction) override
       {
           Position newPos = calNewPosition(direction);
           if(isValidMove(newPos))
           {
               battlefield -> removeRobot(this); //remove the old position
               battlefield -> placeRobot(this, newPos.robotPositionX, newPos.robotPositionY); //place the new position
               setPosition(newPos.robotPositionX, newPos.robotPositionY);
               std::cout << name << " moved to (" << getX() << ", " << getY() << ")." << endl;
           }
           else
           {
               std::cout << name << " invalid move, keep position (" << getX() << ", " << getY() << ")." << endl;
           }
           }
       };



#endif

/**********|**********|**********|
 Program: jumpBot.h
 Course: OOPDS
 Trimester: 2510
 Name: Lim E Jac
 ID: 242UC245BG
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: LIM.E.JAC@student.mmu.edu.my
 Phone: 017-7026113
 **********|**********|**********/

#pragma once
class TrackBot;
class ThirtyShotBot;
class SemiAutoBot;
class HideBot;
class LongShotBot;
class ScoutBot;
class GenericRobot;

#include "genericrobot.h"
#include "battlefield.h"
#include "Constants.h"
#include "trackbot.h"
#include "thirtyshotbot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "scoutbot.h"
#include "LongShotBot.h"

using namespace std;

class JumpBot : public virtual GenericRobot {
private:
    int jumpCount;
    
public:
    JumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield), jumpCount(3) {
        chooseMovingUpgrade("JumpBot");
    }

    // Add a virtual fire method to allow overrides in derived classes
    virtual void fire(Battlefield* battlefield, int x, int y) {}

    void jump(Battlefield& battlefield, int targetX, int targetY) {
        if (jumpCount > 0) {
            if (targetX >= 0 && targetX < battlefield.getWidth() && 
                targetY >= 0 && targetY < battlefield.getHeight()) {
                Position newPos;
                newPos.robotPositionX = targetX;
                newPos.robotPositionY = targetY;
                
                if (battlefield.isValidMove(newPos)) {
                    pos = newPos;
                    jumpCount--;
                    cout << name << " jumped to (" << targetX << "," << targetY 
                         << ") (" << jumpCount << " jumps remaining)\n";
                }
            }
        }
    }

    void displayStats() const override {
        GenericRobot::displayStats();
        cout << "Upgrade: JumpBot | Jumps left: " << jumpCount << endl;
    }
};

// Jump + Track + ThirtyShot
// (moved to JumpTrackThirtyShotBot.h)
// Jump + Track + SemiAuto
// (moved to JumpTrackSemiAutoBot.h)
// Jump + Track + LongShot
// (moved to JumpTrackLongShotBot.h)
// Jump + Scout + LongShot
// (moved to JumpScoutLongShotBot.h)
// Jump + Scout + SemiAuto
// (moved to JumpScoutSemiAutoBot.h)
// Jump + Scout + ThirtyShot
// (moved to JumpScoutThirtyShotBot.h)
// Jump + LongShot + Scout
// (moved to JumpLongShotScoutBot.h)
// Jump + LongShot + Track
// (moved to JumpLongShotTrackBot.h)
// Jump + SemiAuto + Scout
// (moved to JumpSemiAutoScoutBot.h)
// Jump + SemiAuto + Track
// (moved to JumpSemiAutoTrackBot.h)
// Jump + ThirtyShot + Scout
// (moved to JumpThirtyShotScoutBot.h)
// Jump + ThirtyShot + Track
// (moved to JumpThirtyShotTrackBot.h)


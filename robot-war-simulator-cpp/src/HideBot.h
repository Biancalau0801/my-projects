#pragma once

/**********|**********|**********|
 Program: hideBot.h
 Course: OOPDS
 Trimester: 2510
 Name: Lim E Jac
 ID: 242UC245BG
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: LIM.E.JAC@student.mmu.edu.my
 Phone: 017-7026113
 **********|**********|**********/

#include "genericrobot.h"
#include "battlefield.h"
#include "Constants.h"
#include "TrackBot.h"
#include "ThirtyShotBot.h"
#include "SemiAutoBot.h"
#include "JumpBot.h"
#include "ScoutBot.h"
#include "LongShotBot.h"

class HideBot : public virtual GenericRobot , public virtual MovingRobot {
private:
    int hideCount;
    bool hidden = false; // Indicates if the bot is currently hidden
public:
    HideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield), hideCount(3) {
         hideCount=3;// Initialize hide ability
        chooseMovingUpgrade("HideBot");
    }

    void hide()  {
        if (canHide ()) {
            hidden = true;
            hideCount--;
            cout << name << " is now hidden! (" << hideCount << " hides remaining)\n";
        }
    }

    virtual void displayStats() const override {
        GenericRobot::displayStats();
        cout << "Upgrade: HideBot | Hides left: " << hideCount << endl;
    }
};

// Hide + LongShot + Scout
// (moved to HideLongShotScoutBot.h)
// Hide + LongShot + Track
// (moved to HideLongShotTrackBot.h)
// Hide + Track + ThirtyShot
// (moved to HideTrackThirtyShotBot.h)
// Hide + Track + SemiAuto
// (moved to HideTrackSemiAutoBot.h)
// Hide + Track + LongShot
// (moved to HideTrackLongShotBot.h)
// Hide + SemiAuto + Scout
// (moved to HideSemiAutoScoutBot.h)
// Hide + SemiAuto + Track
// (moved to HideSemiAutoTrackBot.h)
// Hide + ThirtyShot + Scout
// (moved to HideThirtyShotScoutBot.h)
// Hide + ThirtyShot + Track
// (moved to HideThirtyShotTrackBot.h)
// Hide + Scout + ThirtyShot
// (moved to HideScoutThirtyShotBot.h)
// Hide + Scout + SemiAuto
// (moved to HideScoutSemiAutoBot.h)
// Hide + Scout + LongShot
// (moved to HideScoutLongShotBot.h)

#pragma once
#include "TrackBot.h"
#include "JumpBot.h"
#include "LongShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackJumpLongShotBot : public virtual TrackBot, public virtual JumpBot, public virtual LongShotBot {
public:
    TrackJumpLongShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump + LongShot" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
        LongShotBot::displayStats();
    }
};

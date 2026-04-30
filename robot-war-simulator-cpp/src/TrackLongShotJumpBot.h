#pragma once
#include "TrackBot.h"
#include "LongShotBot.h"
#include "JumpBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackLongShotJumpBot : public virtual TrackBot, public virtual LongShotBot, public virtual JumpBot {
public:
    TrackLongShotJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + LongShot + Jump" << endl;
        TrackBot::displayStats();
        LongShotBot::displayStats();
        JumpBot::displayStats();
    }
};

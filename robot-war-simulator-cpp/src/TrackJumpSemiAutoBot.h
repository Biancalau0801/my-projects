#pragma once
#include "TrackBot.h"
#include "JumpBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackJumpSemiAutoBot : public virtual TrackBot, public virtual JumpBot, public virtual SemiAutoBot {
public:
    TrackJumpSemiAutoBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump + SemiAuto" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

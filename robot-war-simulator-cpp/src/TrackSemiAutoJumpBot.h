#pragma once
#include "TrackBot.h"
#include "SemiAutoBot.h"
#include "JumpBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackSemiAutoJumpBot : public virtual TrackBot, public virtual SemiAutoBot, public virtual JumpBot {
public:
    TrackSemiAutoJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + SemiAuto + Jump" << endl;
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
    }
};

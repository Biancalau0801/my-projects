#pragma once
#include "JumpBot.h"
#include "TrackBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpTrackSemiAutoBot : public virtual JumpBot, public virtual TrackBot, public virtual SemiAutoBot {
public:
    JumpTrackSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + Track + SemiAuto" << endl;
        JumpBot::displayStats();
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

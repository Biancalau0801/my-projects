#pragma once
#include "JumpBot.h"
#include "SemiAutoBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpSemiAutoTrackBot : public virtual JumpBot, public virtual SemiAutoBot, public virtual TrackBot {
public:
    JumpSemiAutoTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + SemiAuto + Track" << endl;
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
        TrackBot::displayStats();
    }
};

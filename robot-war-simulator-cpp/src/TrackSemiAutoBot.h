#pragma once
#include "TrackBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;

class TrackSemiAutoBot : public virtual TrackBot, public virtual SemiAutoBot {
public:
    TrackSemiAutoBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + SemiAuto" << endl;
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

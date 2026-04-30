#pragma once
#include "TrackBot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackSemiAutoHideBot : public virtual TrackBot, public virtual SemiAutoBot, public virtual HideBot {
public:
    TrackSemiAutoHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + SemiAuto + Hide" << endl;
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
        HideBot::displayStats();
    }
};

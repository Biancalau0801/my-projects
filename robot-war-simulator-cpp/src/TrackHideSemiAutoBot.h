#pragma once
#include "TrackBot.h"
#include "HideBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackHideSemiAutoBot : public virtual TrackBot, public virtual HideBot, public virtual SemiAutoBot {
public:
    TrackHideSemiAutoBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide + SemiAuto" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

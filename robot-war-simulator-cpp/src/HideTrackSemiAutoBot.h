#pragma once
#include "HideBot.h"
#include "TrackBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideTrackSemiAutoBot : public virtual HideBot, public virtual TrackBot, public virtual SemiAutoBot {
public:
    HideTrackSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + Track + SemiAuto" << endl;
        HideBot::displayStats();
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

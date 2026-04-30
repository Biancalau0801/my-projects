#pragma once
#include "HideBot.h"
#include "SemiAutoBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideSemiAutoTrackBot : public virtual HideBot, public virtual SemiAutoBot, public virtual TrackBot {
public:
    HideSemiAutoTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + SemiAuto + Track" << endl;
        HideBot::displayStats();
        SemiAutoBot::displayStats();
        TrackBot::displayStats();
    }
};

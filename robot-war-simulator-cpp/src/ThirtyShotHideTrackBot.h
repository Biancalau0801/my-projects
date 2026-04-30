#pragma once
#include "ThirtyShotBot.h"
#include "HideBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotHideTrackBot : public virtual ThirtyShotBot, public virtual HideBot, public virtual TrackBot {
public:
    ThirtyShotHideTrackBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Hide + Track" << endl;
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
        TrackBot::displayStats();
    }
};

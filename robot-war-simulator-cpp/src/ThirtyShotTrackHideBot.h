#pragma once
#include "ThirtyShotBot.h"
#include "TrackBot.h"
#include "HideBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotTrackHideBot : public virtual ThirtyShotBot, public virtual TrackBot, public virtual HideBot {
public:
    ThirtyShotTrackHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Track + Hide" << endl;
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
        HideBot::displayStats();
    }
};

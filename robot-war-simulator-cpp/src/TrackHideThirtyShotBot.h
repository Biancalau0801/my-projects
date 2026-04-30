#pragma once
#include "TrackBot.h"
#include "HideBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackHideThirtyShotBot : public virtual TrackBot, public virtual HideBot, public virtual ThirtyShotBot {
public:
    TrackHideThirtyShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide + ThirtyShot" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

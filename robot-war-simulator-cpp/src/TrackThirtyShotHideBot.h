#pragma once
#include "TrackBot.h"
#include "ThirtyShotBot.h"
#include "HideBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackThirtyShotHideBot : public virtual TrackBot, public virtual ThirtyShotBot, public virtual HideBot {
public:
    TrackThirtyShotHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + ThirtyShot + Hide" << endl;
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
    }
};

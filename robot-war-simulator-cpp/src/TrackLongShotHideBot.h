#pragma once
#include "TrackBot.h"
#include "LongShotBot.h"
#include "HideBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackLongShotHideBot : public virtual TrackBot, public virtual LongShotBot, public virtual HideBot {
public:
    TrackLongShotHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + LongShot + Hide" << endl;
        TrackBot::displayStats();
        LongShotBot::displayStats();
        HideBot::displayStats();
    }
};

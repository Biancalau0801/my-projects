#pragma once
#include "TrackBot.h"
#include "LongShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;

class TrackLongShotBot : public virtual TrackBot, public virtual LongShotBot {
public:
    TrackLongShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + LongShot" << endl;
        TrackBot::displayStats();
        LongShotBot::displayStats();
    }
};

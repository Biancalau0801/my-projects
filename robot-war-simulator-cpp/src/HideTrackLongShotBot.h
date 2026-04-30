#pragma once
#include "HideBot.h"
#include "TrackBot.h"
#include "LongShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideTrackLongShotBot : public virtual HideBot, public virtual TrackBot, public virtual LongShotBot {
public:
    HideTrackLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + Track + LongShot" << endl;
        HideBot::displayStats();
        TrackBot::displayStats();
        LongShotBot::displayStats();
    }
};

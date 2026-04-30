#pragma once
#include "JumpBot.h"
#include "TrackBot.h"
#include "LongShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpTrackLongShotBot : public virtual JumpBot, public virtual TrackBot, public virtual LongShotBot {
public:
    JumpTrackLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + Track + LongShot" << endl;
        JumpBot::displayStats();
        TrackBot::displayStats();
        LongShotBot::displayStats();
    }
};

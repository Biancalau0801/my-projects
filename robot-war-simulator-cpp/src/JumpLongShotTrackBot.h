#pragma once
#include "JumpBot.h"
#include "LongShotBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpLongShotTrackBot : public virtual JumpBot, public virtual LongShotBot, public virtual TrackBot {
public:
    JumpLongShotTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + LongShot + Track" << endl;
        JumpBot::displayStats();
        LongShotBot::displayStats();
        TrackBot::displayStats();
    }
};

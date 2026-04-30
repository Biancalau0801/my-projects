#pragma once
#include "HideBot.h"
#include "LongShotBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideLongShotTrackBot : public HideBot, public LongShotBot, public TrackBot {
public:
    HideLongShotTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + LongShot + Track" << endl;
        HideBot::displayStats();
        LongShotBot::displayStats();
        TrackBot::displayStats();
    }
};

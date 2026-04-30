#pragma once
#include "ThirtyShotBot.h"
#include "JumpBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotJumpTrackBot : public virtual ThirtyShotBot, public virtual JumpBot, public virtual TrackBot {
public:
    ThirtyShotJumpTrackBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Jump + Track" << endl;
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
        TrackBot::displayStats();
    }
};

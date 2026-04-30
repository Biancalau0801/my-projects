#pragma once
#include "ThirtyShotBot.h"
#include "TrackBot.h"
#include "JumpBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotTrackJumpBot : public virtual ThirtyShotBot, public virtual TrackBot, public virtual JumpBot {
public:
    ThirtyShotTrackJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Track + Jump" << endl;
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
        JumpBot::displayStats();
    }
};

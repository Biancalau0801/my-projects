#pragma once
#include "TrackBot.h"
#include "JumpBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackJumpThirtyShotBot : public virtual TrackBot, public virtual JumpBot, public virtual ThirtyShotBot {
public:
    TrackJumpThirtyShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump + ThirtyShot" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

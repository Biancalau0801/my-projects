#pragma once
#include "TrackBot.h"
#include "ThirtyShotBot.h"
#include "JumpBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class TrackThirtyShotJumpBot : public virtual TrackBot, public virtual ThirtyShotBot, public virtual JumpBot {
public:
    TrackThirtyShotJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + ThirtyShot + Jump" << endl;
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
    }
};

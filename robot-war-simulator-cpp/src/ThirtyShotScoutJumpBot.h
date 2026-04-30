#pragma once
#include "ThirtyShotBot.h"
#include "ScoutBot.h"
#include "JumpBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotScoutJumpBot : public virtual ThirtyShotBot, public virtual ScoutBot, public virtual JumpBot {
public:
    ThirtyShotScoutJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Scout + Jump" << endl;
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
        JumpBot::displayStats();
    }
};

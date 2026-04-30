#pragma once
#include "ThirtyShotBot.h"
#include "JumpBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotJumpScoutBot : public virtual ThirtyShotBot, public virtual JumpBot, public virtual ScoutBot {
public:
    ThirtyShotJumpScoutBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Jump + Scout" << endl;
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
        ScoutBot::displayStats();
    }
};

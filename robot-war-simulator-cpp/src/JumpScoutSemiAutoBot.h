#pragma once
#include "JumpBot.h"
#include "ScoutBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpScoutSemiAutoBot : public virtual JumpBot, public virtual ScoutBot, public virtual SemiAutoBot {
public:
    JumpScoutSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + Scout + SemiAuto" << endl;
        JumpBot::displayStats();
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

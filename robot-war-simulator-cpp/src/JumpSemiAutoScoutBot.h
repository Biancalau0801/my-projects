#pragma once
#include "JumpBot.h"
#include "SemiAutoBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpSemiAutoScoutBot : public virtual JumpBot, public virtual SemiAutoBot, public virtual ScoutBot {
public:
    JumpSemiAutoScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + SemiAuto + Scout" << endl;
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
        ScoutBot::displayStats();
    }
};

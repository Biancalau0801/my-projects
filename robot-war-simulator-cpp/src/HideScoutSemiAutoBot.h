#pragma once
#include "HideBot.h"
#include "ScoutBot.h"
#include "SemiAutoBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideScoutSemiAutoBot : public virtual HideBot, public virtual ScoutBot, public virtual SemiAutoBot {
public:
    HideScoutSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + Scout + SemiAuto" << endl;
        HideBot::displayStats();
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

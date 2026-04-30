#pragma once
#include "HideBot.h"
#include "SemiAutoBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideSemiAutoScoutBot : public virtual HideBot, public virtual SemiAutoBot, public virtual ScoutBot {
public:
    HideSemiAutoScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + SemiAuto + Scout" << endl;
        HideBot::displayStats();
        SemiAutoBot::displayStats();
        ScoutBot::displayStats();
    }
};

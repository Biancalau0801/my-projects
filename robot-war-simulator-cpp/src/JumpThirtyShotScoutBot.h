#pragma once
#include "JumpBot.h"
#include "ThirtyShotBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpThirtyShotScoutBot : public virtual JumpBot, public virtual ThirtyShotBot, public virtual ScoutBot {
public:
    JumpThirtyShotScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + ThirtyShot + Scout" << endl;
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

#pragma once
#include "JumpBot.h"
#include "ScoutBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpScoutThirtyShotBot : public virtual JumpBot, public virtual ScoutBot, public virtual ThirtyShotBot {
public:
    JumpScoutThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + Scout + ThirtyShot" << endl;
        JumpBot::displayStats();
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

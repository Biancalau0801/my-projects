#pragma once
#include "JumpBot.h"
#include "LongShotBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpLongShotScoutBot : public virtual JumpBot, public virtual LongShotBot, public virtual ScoutBot {
public:
    JumpLongShotScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + LongShot + Scout" << endl;
        JumpBot::displayStats();
        LongShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

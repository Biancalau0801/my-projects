#pragma once
#include "JumpBot.h"
#include "ScoutBot.h"
#include "LongShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpScoutLongShotBot : public virtual JumpBot, public virtual ScoutBot, public virtual LongShotBot {
public:
    JumpScoutLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + Scout + LongShot" << endl;
        JumpBot::displayStats();
        ScoutBot::displayStats();
        LongShotBot::displayStats();
    }
};

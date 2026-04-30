#pragma once
#include "HideBot.h"
#include "ScoutBot.h"
#include "LongShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideScoutLongShotBot : public virtual HideBot, public virtual ScoutBot, public virtual LongShotBot {
public:
    HideScoutLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + Scout + LongShot" << endl;
        HideBot::displayStats();
        ScoutBot::displayStats();
        LongShotBot::displayStats();
    }
};

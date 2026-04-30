#pragma once
#include "HideBot.h"
#include "LongShotBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideLongShotScoutBot : public HideBot, public LongShotBot, public ScoutBot {
public:
    HideLongShotScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + LongShot + Scout" << endl;
        HideBot::displayStats();
        LongShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

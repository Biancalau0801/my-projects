#pragma once
#include "HideBot.h"
#include "ThirtyShotBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideThirtyShotScoutBot : public virtual HideBot, public virtual ThirtyShotBot, public virtual ScoutBot {
public:
    HideThirtyShotScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + ThirtyShot + Scout" << endl;
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

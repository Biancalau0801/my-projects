#pragma once
#include "ThirtyShotBot.h"
#include "HideBot.h"
#include "ScoutBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotHideScoutBot : public virtual ThirtyShotBot, public virtual HideBot, public virtual ScoutBot {
public:
    ThirtyShotHideScoutBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Hide + Scout" << endl;
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
        ScoutBot::displayStats();
    }
};

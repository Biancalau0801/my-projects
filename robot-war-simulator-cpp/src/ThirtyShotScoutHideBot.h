#pragma once
#include "ThirtyShotBot.h"
#include "ScoutBot.h"
#include "HideBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class ThirtyShotScoutHideBot : public virtual ThirtyShotBot, public virtual ScoutBot, public virtual HideBot {
public:
    ThirtyShotScoutHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Scout + Hide" << endl;
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
        HideBot::displayStats();
    }
};

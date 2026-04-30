#pragma once
#include "HideBot.h"
#include "ScoutBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideScoutThirtyShotBot : public virtual HideBot, public virtual ScoutBot, public virtual ThirtyShotBot {
public:
    HideScoutThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + Scout + ThirtyShot" << endl;
        HideBot::displayStats();
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

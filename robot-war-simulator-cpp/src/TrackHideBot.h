#pragma once
#include "TrackBot.h"
#include "HideBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;

class TrackHideBot : public virtual TrackBot, public virtual HideBot {
public:
    TrackHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
    }
};

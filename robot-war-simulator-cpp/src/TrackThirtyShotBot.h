#pragma once
#include "TrackBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;

class TrackThirtyShotBot : public virtual TrackBot, public virtual ThirtyShotBot {
public:
    TrackThirtyShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + ThirtyShot" << endl;
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

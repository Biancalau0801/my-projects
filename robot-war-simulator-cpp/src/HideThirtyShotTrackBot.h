#pragma once
#include "HideBot.h"
#include "ThirtyShotBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideThirtyShotTrackBot : public virtual HideBot, public virtual ThirtyShotBot, public virtual TrackBot {
public:
    HideThirtyShotTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + ThirtyShot + Track" << endl;
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
    }
};

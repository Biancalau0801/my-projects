#pragma once
#include "HideBot.h"
#include "TrackBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class HideTrackThirtyShotBot : public virtual HideBot, public virtual TrackBot, public virtual ThirtyShotBot {
public:
    HideTrackThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Hide + Track + ThirtyShot" << endl;
        HideBot::displayStats();
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

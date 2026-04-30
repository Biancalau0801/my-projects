#pragma once
#include "JumpBot.h"
#include "ThirtyShotBot.h"
#include "TrackBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpThirtyShotTrackBot : public virtual JumpBot, public virtual ThirtyShotBot, public virtual TrackBot {
public:
    JumpThirtyShotTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + ThirtyShot + Track" << endl;
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
    }
};

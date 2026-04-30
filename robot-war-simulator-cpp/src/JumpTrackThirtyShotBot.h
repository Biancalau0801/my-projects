#pragma once
#include "JumpBot.h"
#include "TrackBot.h"
#include "ThirtyShotBot.h"
#include "genericrobot.h"
#include "battlefield.h"
#include <iostream>
using namespace std;
class JumpTrackThirtyShotBot : public virtual JumpBot, public virtual TrackBot, public virtual ThirtyShotBot {
public:
    JumpTrackThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Jump + Track + ThirtyShot" << endl;
        JumpBot::displayStats();
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

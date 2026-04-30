#pragma once
#include "ThirtyShotBot.h"
#include "TrackBot.h"

class ThirtyShotTrackBot : public virtual ThirtyShotBot, public virtual TrackBot {
public:
    ThirtyShotTrackBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: ThirtyShot + Track" << std::endl;
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
    }
};

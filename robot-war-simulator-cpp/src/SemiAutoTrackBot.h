#pragma once
#include "SemiAutoBot.h"
#include "TrackBot.h"

class SemiAutoTrackBot : public virtual SemiAutoBot, public virtual TrackBot {
public:
    SemiAutoTrackBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Track" << std::endl;
        SemiAutoBot::displayStats();
        TrackBot::displayStats();
    }
};

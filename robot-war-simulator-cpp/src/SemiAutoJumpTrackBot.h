#pragma once
#include "SemiAutoBot.h"
#include "JumpBot.h"
#include "TrackBot.h"

class SemiAutoJumpTrackBot : public virtual SemiAutoBot, public virtual JumpBot, public virtual TrackBot {
public:
    SemiAutoJumpTrackBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Jump + Track" << std::endl;
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
        TrackBot::displayStats();
    }
};

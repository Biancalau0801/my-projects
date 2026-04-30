#pragma once
#include "SemiAutoBot.h"
#include "TrackBot.h"
#include "JumpBot.h"

class SemiAutoTrackJumpBot : public virtual SemiAutoBot, public virtual TrackBot, public virtual JumpBot {
public:
    SemiAutoTrackJumpBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Track + Jump" << std::endl;
        SemiAutoBot::displayStats();
        TrackBot::displayStats();
        JumpBot::displayStats();
    }
};

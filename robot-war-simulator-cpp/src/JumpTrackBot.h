#pragma once
#include "JumpBot.h"
#include "TrackBot.h"

class JumpTrackBot : public virtual JumpBot, public virtual TrackBot {
public:
    JumpTrackBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Jump + Track" << std::endl;
        JumpBot::displayStats();
        TrackBot::displayStats();
    }
};

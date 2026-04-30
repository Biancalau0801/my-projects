#pragma once
#include "JumpBot.h"
#include "SemiAutoBot.h"

class JumpSemiAutoBot : public virtual JumpBot, public virtual SemiAutoBot {
public:
    JumpSemiAutoBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Jump + SemiAuto" << std::endl;
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

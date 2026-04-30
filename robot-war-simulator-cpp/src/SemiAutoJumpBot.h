#pragma once
#include "SemiAutoBot.h"
#include "JumpBot.h"

class SemiAutoJumpBot : public virtual SemiAutoBot, public virtual JumpBot {
public:
    SemiAutoJumpBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Jump" << std::endl;
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
    }
};

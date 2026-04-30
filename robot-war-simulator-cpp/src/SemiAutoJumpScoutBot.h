#pragma once
#include "SemiAutoBot.h"
#include "JumpBot.h"
#include "ScoutBot.h"

class SemiAutoJumpScoutBot : public virtual SemiAutoBot, public virtual JumpBot, public virtual ScoutBot {
public:
    SemiAutoJumpScoutBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Jump + Scout" << std::endl;
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
        ScoutBot::displayStats();
    }
};

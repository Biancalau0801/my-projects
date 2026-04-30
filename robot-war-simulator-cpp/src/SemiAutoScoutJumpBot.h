#pragma once
#include "SemiAutoBot.h"
#include "ScoutBot.h"
#include "JumpBot.h"

class SemiAutoScoutJumpBot : public virtual SemiAutoBot, public virtual ScoutBot, public virtual JumpBot {
public:
    SemiAutoScoutJumpBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Scout + Jump" << std::endl;
        SemiAutoBot::displayStats();
        ScoutBot::displayStats();
        JumpBot::displayStats();
    }
};

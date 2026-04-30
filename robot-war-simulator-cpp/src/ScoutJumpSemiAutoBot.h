#pragma once
#include "ScoutBot.h"
#include "JumpBot.h"
#include "SemiAutoBot.h"

class ScoutJumpSemiAutoBot : public virtual ScoutBot, public virtual JumpBot, public virtual SemiAutoBot {
public:
    ScoutJumpSemiAutoBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Jump + SemiAuto" << std::endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

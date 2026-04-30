#pragma once
#include "ScoutBot.h"
#include "SemiAutoBot.h"
#include "JumpBot.h"

class ScoutSemiAutoJumpBot : public virtual ScoutBot, public virtual SemiAutoBot, public virtual JumpBot {
public:
    ScoutSemiAutoJumpBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + SemiAuto + Jump" << std::endl;
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
    }
};

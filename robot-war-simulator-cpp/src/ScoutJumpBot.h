#pragma once
#include "ScoutBot.h"
#include "JumpBot.h"

class ScoutJumpBot : public virtual ScoutBot, public virtual JumpBot {
public:
    ScoutJumpBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Jump" << std::endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
    }
};

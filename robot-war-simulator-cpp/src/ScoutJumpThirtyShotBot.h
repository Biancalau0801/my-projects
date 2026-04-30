#pragma once
#include "ScoutBot.h"
#include "JumpBot.h"
#include "ThirtyShotBot.h"

class ScoutJumpThirtyShotBot : public virtual ScoutBot, public virtual JumpBot, public virtual ThirtyShotBot {
public:
    ScoutJumpThirtyShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Jump + ThirtyShot" << std::endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

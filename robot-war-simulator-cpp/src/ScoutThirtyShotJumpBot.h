#pragma once
#include "ScoutBot.h"
#include "ThirtyShotBot.h"
#include "JumpBot.h"

class ScoutThirtyShotJumpBot : public virtual ScoutBot, public virtual ThirtyShotBot, public virtual JumpBot {
public:
    ScoutThirtyShotJumpBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + ThirtyShot + Jump" << std::endl;
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
    }
};

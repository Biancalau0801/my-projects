#pragma once
#include "ScoutBot.h"
#include "SemiAutoBot.h"

class ScoutSemiAutoBot : public virtual ScoutBot, public virtual SemiAutoBot {
public:
    ScoutSemiAutoBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + SemiAuto" << std::endl;
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

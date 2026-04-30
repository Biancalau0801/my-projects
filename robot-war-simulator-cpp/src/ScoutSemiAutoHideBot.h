#pragma once
#include "ScoutBot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"

class ScoutSemiAutoHideBot : public virtual ScoutBot, public virtual SemiAutoBot, public virtual HideBot {
public:
    ScoutSemiAutoHideBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + SemiAuto + Hide" << std::endl;
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
        HideBot::displayStats();
    }
};

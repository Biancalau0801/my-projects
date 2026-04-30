#pragma once
#include "ScoutBot.h"
#include "HideBot.h"
#include "SemiAutoBot.h"

class ScoutHideSemiAutoBot : public virtual ScoutBot, public virtual HideBot, public virtual SemiAutoBot {
public:
    ScoutHideSemiAutoBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Hide + SemiAuto" << std::endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

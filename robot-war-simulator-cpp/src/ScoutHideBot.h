#pragma once
#include "ScoutBot.h"
#include "HideBot.h"

class ScoutHideBot : public virtual ScoutBot, public virtual HideBot {
public:
    ScoutHideBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Hide" << std::endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
    }
};

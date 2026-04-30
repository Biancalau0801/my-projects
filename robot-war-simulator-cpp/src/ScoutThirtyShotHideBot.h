#pragma once
#include "ScoutBot.h"
#include "ThirtyShotBot.h"
#include "HideBot.h"

class ScoutThirtyShotHideBot : public virtual ScoutBot, public virtual ThirtyShotBot, public virtual HideBot {
public:
    ScoutThirtyShotHideBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + ThirtyShot + Hide" << std::endl;
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
    }
};

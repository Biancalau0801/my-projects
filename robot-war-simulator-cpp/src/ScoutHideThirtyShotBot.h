#pragma once
#include "ScoutBot.h"
#include "HideBot.h"
#include "ThirtyShotBot.h"

class ScoutHideThirtyShotBot : public virtual ScoutBot, public virtual HideBot, public virtual ThirtyShotBot {
public:
    ScoutHideThirtyShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Hide + ThirtyShot" << std::endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

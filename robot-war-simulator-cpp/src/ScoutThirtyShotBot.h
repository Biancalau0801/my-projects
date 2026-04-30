#pragma once
#include "ScoutBot.h"
#include "ThirtyShotBot.h"

class ScoutThirtyShotBot : public virtual ScoutBot, public virtual ThirtyShotBot {
public:
    ScoutThirtyShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + ThirtyShot" << std::endl;
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

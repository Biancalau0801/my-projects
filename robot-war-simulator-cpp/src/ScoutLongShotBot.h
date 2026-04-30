#pragma once
#include "ScoutBot.h"
#include "LongShotBot.h"

class ScoutLongShotBot : public virtual ScoutBot, public virtual LongShotBot {
public:
    ScoutLongShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + LongShot" << std::endl;
        ScoutBot::displayStats();
        LongShotBot::displayStats();
    }
};

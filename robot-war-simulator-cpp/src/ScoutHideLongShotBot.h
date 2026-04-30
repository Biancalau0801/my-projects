#pragma once
#include "ScoutBot.h"
#include "HideBot.h"
#include "LongShotBot.h"

class ScoutHideLongShotBot : public virtual ScoutBot, public virtual HideBot, public virtual LongShotBot {
public:
    ScoutHideLongShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Hide + LongShot" << std::endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
        LongShotBot::displayStats();
    }
};

#pragma once
#include "ScoutBot.h"
#include "LongShotBot.h"
#include "HideBot.h"

class ScoutLongShotHideBot : public virtual ScoutBot, public virtual LongShotBot, public virtual HideBot {
public:
    ScoutLongShotHideBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + LongShot + Hide" << std::endl;
        ScoutBot::displayStats();
        LongShotBot::displayStats();
        HideBot::displayStats();
    }
};

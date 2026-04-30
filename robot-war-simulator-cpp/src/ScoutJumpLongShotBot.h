#pragma once
#include "ScoutBot.h"
#include "JumpBot.h"
#include "LongShotBot.h"

class ScoutJumpLongShotBot : public virtual ScoutBot, public virtual JumpBot, public virtual LongShotBot {
public:
    ScoutJumpLongShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + Jump + LongShot" << std::endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
        LongShotBot::displayStats();
    }
};

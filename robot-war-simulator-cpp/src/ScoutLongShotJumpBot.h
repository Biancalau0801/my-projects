#pragma once
#include "ScoutBot.h"
#include "LongShotBot.h"
#include "JumpBot.h"

class ScoutLongShotJumpBot : public virtual ScoutBot, public virtual LongShotBot, public virtual JumpBot {
public:
    ScoutLongShotJumpBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Scout + LongShot + Jump" << std::endl;
        ScoutBot::displayStats();
        LongShotBot::displayStats();
        JumpBot::displayStats();
    }
};

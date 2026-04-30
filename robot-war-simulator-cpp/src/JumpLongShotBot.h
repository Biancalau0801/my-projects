#pragma once
#include "JumpBot.h"
#include "LongShotBot.h"

class JumpLongShotBot : public virtual JumpBot, public virtual LongShotBot {
public:
    JumpLongShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Jump + LongShot" << std::endl;
        JumpBot::displayStats();
        LongShotBot::displayStats();
    }
};

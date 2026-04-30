#pragma once
#include "HideBot.h"
#include "LongShotBot.h"

class HideLongShotBot : public virtual HideBot, public virtual LongShotBot {
public:
    HideLongShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        std::cout << "Upgrade: Hide + LongShot" << std::endl;
        HideBot::displayStats();
        LongShotBot::displayStats();
    }
};

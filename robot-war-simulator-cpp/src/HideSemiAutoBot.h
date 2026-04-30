#pragma once
#include "HideBot.h"
#include "SemiAutoBot.h"

class HideSemiAutoBot : public virtual HideBot, public virtual SemiAutoBot {
public:
    HideSemiAutoBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        std::cout << "Upgrade: Hide + SemiAuto" << std::endl;
        HideBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

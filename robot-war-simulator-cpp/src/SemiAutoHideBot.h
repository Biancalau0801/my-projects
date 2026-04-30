#pragma once
#include "SemiAutoBot.h"
#include "HideBot.h"

class SemiAutoHideBot : public virtual SemiAutoBot, public virtual HideBot {
public:
    SemiAutoHideBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Hide" << std::endl;
        SemiAutoBot::displayStats();
        HideBot::displayStats();
    }
};

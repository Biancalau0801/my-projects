#pragma once
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "ScoutBot.h"

class SemiAutoHideScoutBot : public virtual SemiAutoBot, public virtual HideBot, public virtual ScoutBot {
public:
    SemiAutoHideScoutBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Hide + Scout" << std::endl;
        SemiAutoBot::displayStats();
        HideBot::displayStats();
        ScoutBot::displayStats();
    }
};

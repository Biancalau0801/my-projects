#pragma once
#include "SemiAutoBot.h"
#include "ScoutBot.h"

class SemiAutoScoutBot : public virtual SemiAutoBot, public virtual ScoutBot {
public:
    SemiAutoScoutBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Scout" << std::endl;
        SemiAutoBot::displayStats();
        ScoutBot::displayStats();
    }
};

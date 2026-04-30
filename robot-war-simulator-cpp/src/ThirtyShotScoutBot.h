#pragma once
#include "ThirtyShotBot.h"
#include "ScoutBot.h"

class ThirtyShotScoutBot : public virtual ThirtyShotBot, public virtual ScoutBot {
public:
    ThirtyShotScoutBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: ThirtyShot + Scout" << std::endl;
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

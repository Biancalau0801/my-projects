#pragma once
#include "HideBot.h"
#include "ScoutBot.h"

class HideScoutBot : public virtual HideBot, public virtual ScoutBot {
public:
    HideScoutBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        std::cout << "Upgrade: Hide + Scout" << std::endl;
        HideBot::displayStats();
        ScoutBot::displayStats();
    }
};

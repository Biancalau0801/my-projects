#pragma once
#include "JumpBot.h"
#include "ScoutBot.h"

class JumpScoutBot : public virtual JumpBot, public virtual ScoutBot {
public:
    JumpScoutBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Jump + Scout" << std::endl;
        JumpBot::displayStats();
        ScoutBot::displayStats();
    }
};

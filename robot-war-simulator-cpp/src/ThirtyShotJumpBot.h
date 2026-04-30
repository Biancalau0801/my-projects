#pragma once
#include "ThirtyShotBot.h"
#include "JumpBot.h"

class ThirtyShotJumpBot : public virtual ThirtyShotBot, public virtual JumpBot {
public:
    ThirtyShotJumpBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: ThirtyShot + Jump" << std::endl;
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
    }
};

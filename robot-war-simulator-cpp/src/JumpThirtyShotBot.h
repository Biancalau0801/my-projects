#pragma once
#include "JumpBot.h"
#include "ThirtyShotBot.h"

class JumpThirtyShotBot : public virtual JumpBot, public virtual ThirtyShotBot {
public:
    JumpThirtyShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: Jump + ThirtyShot" << std::endl;
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

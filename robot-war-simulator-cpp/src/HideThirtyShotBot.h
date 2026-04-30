#pragma once
#include "HideBot.h"
#include "ThirtyShotBot.h"

class HideThirtyShotBot : public virtual HideBot, public virtual ThirtyShotBot {
public:
    HideThirtyShotBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        std::cout << "Upgrade: Hide + ThirtyShot" << std::endl;
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

#pragma once
#include "ThirtyShotBot.h"
#include "HideBot.h"

class ThirtyShotHideBot : public virtual ThirtyShotBot, public virtual HideBot {
public:
    ThirtyShotHideBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: ThirtyShot + Hide" << std::endl;
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
    }
};

#pragma once
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "TrackBot.h"

class SemiAutoHideTrackBot : public virtual SemiAutoBot, public virtual HideBot, public virtual TrackBot {
public:
    SemiAutoHideTrackBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Hide + Track" << std::endl;
        SemiAutoBot::displayStats();
        HideBot::displayStats();
        TrackBot::displayStats();
    }
};

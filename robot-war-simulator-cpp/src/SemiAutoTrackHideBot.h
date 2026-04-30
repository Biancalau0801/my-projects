#pragma once
#include "SemiAutoBot.h"
#include "TrackBot.h"
#include "HideBot.h"

class SemiAutoTrackHideBot : public virtual SemiAutoBot, public virtual TrackBot, public virtual HideBot {
public:
    SemiAutoTrackHideBot(const std::string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        std::cout << "Upgrade: SemiAuto + Track + Hide" << std::endl;
        SemiAutoBot::displayStats();
        TrackBot::displayStats();
        HideBot::displayStats();
    }
};

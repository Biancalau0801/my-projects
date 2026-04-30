#pragma once
#include "HideBot.h"
#include "TrackBot.h"

class HideTrackBot : public virtual HideBot, public virtual TrackBot {
public:
    HideTrackBot(const std::string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        std::cout << "Upgrade: Hide + Track" << std::endl;
        HideBot::displayStats();
        TrackBot::displayStats();
    }
};

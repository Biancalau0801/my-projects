#ifndef LONGSHOTSCOUTBOT_H
#define LONGSHOTSCOUTBOT_H

#include "LongShotBot.h"
#include "ScoutBot.h"

class LongShotScoutBot : public virtual LongShotBot, public virtual ScoutBot {
public:
    LongShotScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Scout" << endl;
        LongShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

#endif

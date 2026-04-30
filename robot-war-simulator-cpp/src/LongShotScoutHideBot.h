#ifndef LONGSHOTSCOUTHIDEBOT_H
#define LONGSHOTSCOUTHIDEBOT_H

#include "LongShotBot.h"
#include "ScoutBot.h"
#include "HideBot.h"

class LongShotScoutHideBot : public virtual LongShotBot, public virtual ScoutBot, public virtual HideBot {
public:
    LongShotScoutHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Scout + Hide" << endl;
        LongShotBot::displayStats();
        ScoutBot::displayStats();
        HideBot::displayStats();
    }
};

#endif

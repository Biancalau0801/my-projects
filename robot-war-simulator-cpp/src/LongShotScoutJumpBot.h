#ifndef LONGSHOTSCOUTJUMPBOT_H
#define LONGSHOTSCOUTJUMPBOT_H

#include "LongShotBot.h"
#include "ScoutBot.h"
#include "JumpBot.h"

class LongShotScoutJumpBot : public virtual LongShotBot, public virtual ScoutBot, public virtual JumpBot {
public:
    LongShotScoutJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Scout + Jump" << endl;
        LongShotBot::displayStats();
        ScoutBot::displayStats();
        JumpBot::displayStats();
    }
};

#endif

#ifndef LONGSHOTJUMPSCOUTBOT_H
#define LONGSHOTJUMPSCOUTBOT_H

#include "LongShotBot.h"
#include "JumpBot.h"
#include "ScoutBot.h"

class LongShotJumpScoutBot : public virtual LongShotBot, public virtual JumpBot, public virtual ScoutBot {
public:
    LongShotJumpScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Jump + Scout" << endl;
        LongShotBot::displayStats();
        JumpBot::displayStats();
        ScoutBot::displayStats();
    }
};

#endif

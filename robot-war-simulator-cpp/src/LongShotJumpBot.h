#ifndef LONGSHOTJUMPBOT_H
#define LONGSHOTJUMPBOT_H

#include "LongShotBot.h"
#include "JumpBot.h"

class LongShotJumpBot : public virtual LongShotBot, public virtual JumpBot {
public:
    LongShotJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Jump" << endl;
        LongShotBot::displayStats();
        JumpBot::displayStats();
    }
};

#endif

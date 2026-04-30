#ifndef LONGSHOTTRACKJUMPBOT_H
#define LONGSHOTTRACKJUMPBOT_H

#include "LongShotBot.h"
#include "TrackBot.h"
#include "JumpBot.h"

class LongShotTrackJumpBot : public virtual LongShotBot, public virtual TrackBot, public virtual JumpBot {
public:
    LongShotTrackJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Track + Jump" << endl;
        LongShotBot::displayStats();
        TrackBot::displayStats();
        JumpBot::displayStats();
    }
};

#endif

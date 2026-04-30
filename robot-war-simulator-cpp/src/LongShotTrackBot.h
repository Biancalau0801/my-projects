#ifndef LONGSHOTTRACKBOT_H
#define LONGSHOTTRACKBOT_H

#include "LongShotBot.h"
#include "TrackBot.h"

class LongShotTrackBot : public virtual LongShotBot, public virtual TrackBot {
public:
    LongShotTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Track" << endl;
        LongShotBot::displayStats();
        TrackBot::displayStats();
    }
};

#endif

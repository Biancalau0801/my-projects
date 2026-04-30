#ifndef LONGSHOTTRACKHIDEBOT_H
#define LONGSHOTTRACKHIDEBOT_H

#include "LongShotBot.h"
#include "TrackBot.h"
#include "HideBot.h"

class LongShotTrackHideBot : public virtual LongShotBot, public virtual TrackBot, public virtual HideBot {
public:
    LongShotTrackHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Track + Hide" << endl;
        LongShotBot::displayStats();
        TrackBot::displayStats();
        HideBot::displayStats();
    }
};

#endif

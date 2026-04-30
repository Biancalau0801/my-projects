#ifndef LONGSHOTHIDETRACKBOT_H
#define LONGSHOTHIDETRACKBOT_H

#include "LongShotBot.h"
#include "HideBot.h"
#include "TrackBot.h"

class LongShotHideTrackBot : public virtual LongShotBot, public virtual HideBot, public virtual TrackBot {
public:
    LongShotHideTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Hide + Track" << endl;
        LongShotBot::displayStats();
        HideBot::displayStats();
        TrackBot::displayStats();
    }
};

#endif

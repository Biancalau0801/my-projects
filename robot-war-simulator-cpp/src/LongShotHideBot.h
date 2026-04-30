#ifndef LONGSHOTHIDEBOT_H
#define LONGSHOTHIDEBOT_H

#include "LongShotBot.h"
#include "HideBot.h"

class LongShotHideBot : public virtual LongShotBot, public virtual HideBot {
public:
    LongShotHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Hide" << endl;
        LongShotBot::displayStats();
        HideBot::displayStats();
    }
};

#endif

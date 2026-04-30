#ifndef LONGSHOTHIDESCOUTBOT_H
#define LONGSHOTHIDESCOUTBOT_H

#include "LongShotBot.h"
#include "HideBot.h"
#include "ScoutBot.h"

class LongShotHideScoutBot : public virtual LongShotBot, public virtual HideBot, public virtual ScoutBot {
public:
    LongShotHideScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
          
    void displayStats() const override {
        cout << "Upgrade: LongShot + Hide + Scout" << endl;
        LongShotBot::displayStats();
        HideBot::displayStats();
        ScoutBot::displayStats();
    }
};

#endif

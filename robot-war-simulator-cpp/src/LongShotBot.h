/**********|**********|**********|
 Program: longShotBot.h
 Course: OOPDS
 Trimester: 2510
 Name: Lim E Jac
 ID: 242UC245BG
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: LIM.E.JAC@student.mmu.edu.my
 Phone: 017-7026113
 **********|**********|**********/

#ifndef LONGSHOTBOT_H
#define LONGSHOTBOT_H


class TrackBot;
class ThirtyShotBot;
class SemiAutoBot;
class HideBot;
class JumpBot;
class ScoutBot;
class GenericRobot;

#include "genericrobot.h"
#include "battlefield.h"
#include "Constants.h"
#include "trackbot.h"
#include "thirtyshotbot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "scoutbot.h"
#include "JumpBot.h"

class LongShotBot : public virtual GenericRobot {
public:
    LongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield) {
        chooseShootingUpgrade("LongShotBot");
    }

    // Override to allow shooting up to range 3
    void fire(int x, int y) override {
        if (shells <= 0) {
            return; // no shells left, cannot shoot
        }

        // Allow range up to 3 
        if (abs(x) + abs(y) < 4 && (x != 0 && y != 0)) {

        shells--; // reduce shells by 1 when used

        int targetX = pos.robotPositionX + x;
        int targetY = pos.robotPositionY + y;

        // Use the battlefield pointer from GenericRobot
        Battlefield* bf = this->getBattlefield();
        if (!bf) return;

        if (targetX < 0 || targetX >= bf->getWidth() ||
            targetY < 0 || targetY >= bf->getHeight()) {
            return;
        }

        Robot* target = bf->getRobotAt(targetX, targetY);
        if (target != nullptr && target->isAlive()) {
            if (rand() % 100 < 70) {
                cout << name << " sniped and killed " << target->getName() << " from long range!" << endl;
                target->takeDamage(name, bf);
            } else {
                cout << name << " missed the long-range shot at " << target->getName() << "." << endl;
            }
        } else {
            cout << name << " fired at (" << targetX << ", " << targetY << ") but no target." << endl;
        }
    }
    }

    void displayStats() const override {
        GenericRobot::displayStats();
        cout << "Upgrade: LongShotBot | Range: 3 units" << endl;
    }
};


// LongShot + Hide
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

// LongShot + Scout
class LongShotScoutBot : public virtual LongShotBot, public virtual ScoutBot {
public:
    LongShotScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          ScoutBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: LongShot + Scout" << endl;
        LongShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

// LongShot + Jump
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

// LongShot + Track
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

// LongShot + Jump + Track
class LongShotJumpTrackBot : public virtual LongShotBot, public virtual JumpBot, public virtual TrackBot {
public:
    LongShotJumpTrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          TrackBot(name, startX, startY, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: LongShot + Jump + Track" << endl;
        LongShotBot::displayStats();
        JumpBot::displayStats();
        TrackBot::displayStats();
    }
};

// LongShot + Hide + Scout
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

// LongShot + Hide + Track
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

// LongShot + Jump + Scout
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

// LongShot + Scout + Hide
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

// LongShot + Track + Hide
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

// LongShot + Track + Jump
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

// LongShot + Scout + Jump
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

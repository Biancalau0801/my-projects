/**********|**********|**********|
 Program: TrackBot.h
 Course: OOPDS
 Trimester: 2510
 Name: Lim E Jac
 ID: 242UC245BG
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: LIM.E.JAC@student.mmu.edu.my
 Phone: 017-7026113
 **********|**********|**********/

#ifndef TRACKBOT_H
#define TRACKBOT_H

class JumpBot;
class ThirtyShotBot;
class SemiAutoBot;
class HideBot;
class LongShotBot;
class ScoutBot;
class GenericRobot;

#include "genericrobot.h"
#include "battlefield.h"
#include "Constants.h"
#include "LongShotBot.h"
#include "thirtyshotbot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "scoutbot.h"
#include "JumpBot.h"
#include <map>

class TrackBot : public virtual GenericRobot {
private:
    int trackers;
    map<string, Position> trackedRobots;
    
public:
    TrackBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield), trackers(3) {
        chooseSeeingUpgrade("TrackBot");
    }

    void track(Battlefield* battlefield, const string& targetName) {
        if (trackers > 0) {
            Robot* target = battlefield->getRobotByName(targetName);
            if (target && target != this) {
                trackedRobots[targetName] = target->getPosition();
                trackers--;
                cout << "Tracking " << targetName << "! (" 
                     << trackers << " trackers remaining)\n";
            }
        }
    }

    void updateTrackedPositions(Battlefield* battlefield) {
        for (auto& entry : trackedRobots) {
            Robot* target = battlefield->getRobotByName(entry.first);
            if (target) {
                entry.second = target->getPosition();
            }
        }
    }

    virtual void displayStats() const override {
        GenericRobot::displayStats();
        cout << "Upgrade: TrackBot | Trackers left: " << trackers << endl;
        if (!trackedRobots.empty()) {
            cout << "Currently tracking:\n";
            for (const auto& entry : trackedRobots) {
                cout << "  " << entry.first << " at (" 
                     << entry.second.robotPositionX << "," 
                     << entry.second.robotPositionY << ")\n";
            }
        }
    }
};

// TrackBot + Hide
class TrackHideBot : public virtual TrackBot, public virtual HideBot {
public:
    TrackHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
    }
};

// TrackBot + Jump
class TrackJumpBot : public virtual TrackBot, public virtual JumpBot {
public:
    TrackJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
    }
};

// TrackBot + LongShot
class TrackLongShotBot : public virtual TrackBot, public virtual LongShotBot {
public:
    TrackLongShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + LongShot" << endl;
        TrackBot::displayStats();
        LongShotBot::displayStats();
    }
};

// TrackBot + SemiAuto
class TrackSemiAutoBot : public virtual TrackBot, public virtual SemiAutoBot {
public:
    TrackSemiAutoBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + SemiAuto" << endl;
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

// TrackBot + ThirtyShot
class TrackThirtyShotBot : public virtual TrackBot, public virtual ThirtyShotBot {
public:
    TrackThirtyShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + ThirtyShot" << endl;
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};
//2nd upgrade
// TrackBot + Hide + LongShot
class TrackHideLongShotBot : public virtual TrackBot, public virtual HideBot, public virtual LongShotBot {
public:
    TrackHideLongShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide + LongShot" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
        LongShotBot::displayStats();
    }
};

// TrackBot + Hide + SemiAuto
class TrackHideSemiAutoBot : public virtual TrackBot, public virtual HideBot, public virtual SemiAutoBot {
public:
    TrackHideSemiAutoBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide + SemiAuto" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

// TrackBot + Hide + ThirtyShot
class TrackHideThirtyShotBot : public virtual TrackBot, public virtual HideBot, public virtual ThirtyShotBot {
public:
    TrackHideThirtyShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Hide + ThirtyShot" << endl;
        TrackBot::displayStats();
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

// TrackBot + Jump + LongShot
class TrackJumpLongShotBot : public virtual TrackBot, public virtual JumpBot, public virtual LongShotBot {
public:
    TrackJumpLongShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump + LongShot" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
        LongShotBot::displayStats();
    }
};

// TrackBot + Jump + SemiAuto
class TrackJumpSemiAutoBot : public virtual TrackBot, public virtual JumpBot, public virtual SemiAutoBot {
public:
    TrackJumpSemiAutoBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump + SemiAuto" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

// TrackBot + Jump + ThirtyShot
class TrackJumpThirtyShotBot : public virtual TrackBot, public virtual JumpBot, public virtual ThirtyShotBot {
public:
    TrackJumpThirtyShotBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + Jump + ThirtyShot" << endl;
        TrackBot::displayStats();
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

// TrackBot + LongShot + Hide
class TrackLongShotHideBot : public virtual TrackBot, public virtual LongShotBot, public virtual HideBot {
public:
    TrackLongShotHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + LongShot + Hide" << endl;
        TrackBot::displayStats();
        LongShotBot::displayStats();
        HideBot::displayStats();
    }
};

// TrackBot + LongShot + Jump
class TrackLongShotJumpBot : public virtual TrackBot, public virtual LongShotBot, public virtual JumpBot {
public:
    TrackLongShotJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          LongShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + LongShot + Jump" << endl;
        TrackBot::displayStats();
        LongShotBot::displayStats();
        JumpBot::displayStats();
    }
};

// TrackBot + SemiAuto + Hide
class TrackSemiAutoHideBot : public virtual TrackBot, public virtual SemiAutoBot, public virtual HideBot {
public:
    TrackSemiAutoHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + SemiAuto + Hide" << endl;
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
        HideBot::displayStats();
    }
};

// TrackBot + SemiAuto + Jump
class TrackSemiAutoJumpBot : public virtual TrackBot, public virtual SemiAutoBot, public virtual JumpBot {
public:
    TrackSemiAutoJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          SemiAutoBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + SemiAuto + Jump" << endl;
        TrackBot::displayStats();
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
    }
};

// TrackBot + ThirtyShot + Hide
class TrackThirtyShotHideBot : public virtual TrackBot, public virtual ThirtyShotBot, public virtual HideBot {
public:
    TrackThirtyShotHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + ThirtyShot + Hide" << endl;
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
    }
};

// TrackBot + ThirtyShot + Jump
class TrackThirtyShotJumpBot : public virtual TrackBot, public virtual ThirtyShotBot, public virtual JumpBot {
public:
    TrackThirtyShotJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: Track + ThirtyShot + Jump" << endl;
        TrackBot::displayStats();
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
    }
};

#endif

/**********|**********|**********|
 Program: ScoutBot.h
 Course: OOPDS
 Trimester: 2510
 Name: Lim E Jac
 ID: 242UC245BG
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: LIM.E.JAC@student.mmu.edu.my
 Phone: 017-7026113
 **********|**********|**********/

#ifndef SCOUTBOT_H
#define SCOUTBOT_H


class TrackBot;
class ThirtyShotBot;
class SemiAutoBot;
class HideBot;
class LongShotBot;
class JumpBot;
class GenericRobot;

#include "genericrobot.h"
#include "battlefield.h"
#include "Constants.h"
#include "trackbot.h"
#include "thirtyshotbot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "LongShotBot.h"
#include "JumpBot.h"

using namespace std;

class ScoutBot : public virtual GenericRobot {
private:
    int scoutCount;
    Battlefield* battlefield;

public:
    ScoutBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield), scoutCount(2) {
        this->battlefield = battlefield;
        chooseSeeingUpgrade("ScoutBot");
    }

    void scout(Battlefield& battlefield) {
        if (scoutCount > 0) {
            cout << name << " is scouting the entire battlefield!\n";
            battlefield.revealAllToRobot(this);
            scoutCount--; // Deducts scoutCount each time scout is used
            cout << "(" << scoutCount << " scouts remaining)\n";
        }
    }

    void displayStats() const override {
        GenericRobot::displayStats();
        cout << "Upgrade: ScoutBot | Scouts left: " << scoutCount << endl;
    }
};

// Scout + ThirtyShot
class ScoutThirtyShotBot : public virtual ScoutBot, public virtual ThirtyShotBot {
public:
    ScoutThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + ThirtyShot" << endl;
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

// Scout + SemiAuto
class ScoutSemiAutoBot : public virtual ScoutBot, public virtual SemiAutoBot {
public:
    ScoutSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + SemiAuto" << endl;
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

// Scout + LongShot
class ScoutLongShotBot : public virtual ScoutBot, public virtual LongShotBot {
public:
    ScoutLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}

    void fire(int x, int y) {
        LongShotBot::fire(x, y);
    }

    void displayStats() const override {
        cout << "Upgrade: Scout + LongShot" << endl;
        ScoutBot::displayStats();
        LongShotBot::displayStats();
    }
};

// Scout + Jump
class ScoutJumpBot : public virtual ScoutBot, public virtual JumpBot {
public:
    ScoutJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Jump" << endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
    }
};

// Scout + Hide
class ScoutHideBot : public virtual ScoutBot, public virtual HideBot {
public:
    ScoutHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Hide" << endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
    }
};

// Scout + LongShot + Jump
class ScoutLongShotJumpBot : public virtual ScoutBot, public virtual LongShotBot, public virtual JumpBot {
public:
    ScoutLongShotJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + LongShot + Jump" << endl;
        ScoutBot::displayStats();
        LongShotBot::displayStats();
        JumpBot::displayStats();
    }
};

// Scout + SemiAuto + Jump
class ScoutSemiAutoJumpBot : public virtual ScoutBot, public virtual SemiAutoBot, public virtual JumpBot {
public:
    ScoutSemiAutoJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + SemiAuto + Jump" << endl;
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
        JumpBot::displayStats();
    }
};

// Scout + ThirtyShot + Jump
class ScoutThirtyShotJumpBot : public virtual ScoutBot, public virtual ThirtyShotBot, public virtual JumpBot {
public:
    ScoutThirtyShotJumpBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + ThirtyShot + Jump" << endl;
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
    }
};

// Scout + LongShot + Hide
class ScoutLongShotHideBot : public virtual ScoutBot, public virtual LongShotBot, public virtual HideBot {
public:
    ScoutLongShotHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + LongShot + Hide" << endl;
        ScoutBot::displayStats();
        LongShotBot::displayStats();
        HideBot::displayStats();
    }
};

// Scout + SemiAuto + Hide
class ScoutSemiAutoHideBot : public virtual ScoutBot, public virtual SemiAutoBot, public virtual HideBot {
public:
    ScoutSemiAutoHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + SemiAuto + Hide" << endl;
        ScoutBot::displayStats();
        SemiAutoBot::displayStats();
        HideBot::displayStats();
    }
};

// Scout + ThirtyShot + Hide
class ScoutThirtyShotHideBot : public virtual ScoutBot, public virtual ThirtyShotBot, public virtual HideBot {
public:
    ScoutThirtyShotHideBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + ThirtyShot + Hide" << endl;
        ScoutBot::displayStats();
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
    }
};

// Scout + Jump + LongShot
class ScoutJumpLongShotBot : public virtual ScoutBot, public virtual JumpBot, public virtual LongShotBot {
public:
    ScoutJumpLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Jump + LongShot" << endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
        LongShotBot::displayStats();
    }
};

// Scout + Jump + SemiAuto
class ScoutJumpSemiAutoBot : public virtual ScoutBot, public virtual JumpBot, public virtual SemiAutoBot {
public:
    ScoutJumpSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Jump + SemiAuto" << endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

// Scout + Jump + ThirtyShot
class ScoutJumpThirtyShotBot : public virtual ScoutBot, public virtual JumpBot, public virtual ThirtyShotBot {
public:
    ScoutJumpThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          JumpBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Jump + ThirtyShot" << endl;
        ScoutBot::displayStats();
        JumpBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};

// Scout + Hide + LongShot
class ScoutHideLongShotBot : public virtual ScoutBot, public virtual HideBot, public virtual LongShotBot {
public:
    ScoutHideLongShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          LongShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Hide + LongShot" << endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
        LongShotBot::displayStats();
    }
};

// Scout + Hide + SemiAuto
class ScoutHideSemiAutoBot : public virtual ScoutBot, public virtual HideBot, public virtual SemiAutoBot {
public:
    ScoutHideSemiAutoBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          SemiAutoBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Hide + SemiAuto" << endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
        SemiAutoBot::displayStats();
    }
};

// Scout + Hide + ThirtyShot
class ScoutHideThirtyShotBot : public virtual ScoutBot, public virtual HideBot, public virtual ThirtyShotBot {
public:
    ScoutHideThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : ScoutBot(name, startX, startY, w, h, battlefield),
          HideBot(name, startX, startY, w, h, battlefield),
          ThirtyShotBot(name, startX, startY, w, h, battlefield) {}

    void displayStats() const override {
        cout << "Upgrade: Scout + Hide + ThirtyShot" << endl;
        ScoutBot::displayStats();
        HideBot::displayStats();
        ThirtyShotBot::displayStats();
    }
};
#endif

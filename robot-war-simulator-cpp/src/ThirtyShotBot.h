/**********|**********|**********|
 Program: ThirtyShotBot.h
 Course: OOPDS
 Trimester: 2510
 Name: Lim E Jac
 ID: 242UC245BG
 Lecture Section: TC1L
 Tutorial Section: TT2L
 Email: LIM.E.JAC@student.mmu.edu.my
 Phone: 017-7026113
 **********|**********|**********/

#ifndef THIRTYSHOTBOT_H
#define THIRTYSHOTBOT_H

class TrackBot;
class JumpBot;
class SemiAutoBot;
class HideBot;
class LongShotBot;
class ScoutBot;
class GenericRobot;
#include "genericrobot.h"
#include "battlefield.h"
#include "Constants.h"
#include "trackbot.h"
#include "LongShotBot.h"
#include "SemiAutoBot.h"
#include "HideBot.h"
#include "scoutbot.h"
#include "JumpBot.h"

class ThirtyShotBot : public virtual GenericRobot {
public:
    ThirtyShotBot(const string& name, int startX, int startY, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, startX, startY, w, h, battlefield) {
        chooseShootingUpgrade("ThirtyShotBot");
        shells = 30; // Override to 30 shells
    }

    void displayStats() const override {
        GenericRobot::displayStats();
        cout << "Type: ThirtyShotBot | Shells: " << shells << "/30" << endl;
    }
};

// ThirtyShot + Hide
class ThirtyShotHideBot : public virtual ThirtyShotBot, public virtual HideBot {
public:
    ThirtyShotHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Hide" << endl;
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
    }
};

// ThirtyShot + Jump
class ThirtyShotJumpBot : public virtual ThirtyShotBot, public virtual JumpBot {
public:
    ThirtyShotJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Jump" << endl;
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
    }
};

// ThirtyShot + Scout
class ThirtyShotScoutBot : public virtual ThirtyShotBot, public virtual ScoutBot {
public:
    ThirtyShotScoutBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Scout" << endl;
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
    }
};

// ThirtyShot + Track
class ThirtyShotTrackBot : public virtual ThirtyShotBot, public virtual TrackBot {
public:
    ThirtyShotTrackBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Track" << endl;
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
    }
};

// ThirtyShot + Hide + Scout
class ThirtyShotHideScoutBot : public virtual ThirtyShotBot, public virtual HideBot, public virtual ScoutBot {
public:
    ThirtyShotHideScoutBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Hide + Scout" << endl;
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
        ScoutBot::displayStats();
    }
};

// ThirtyShot + Hide + Track
class ThirtyShotHideTrackBot : public virtual ThirtyShotBot, public virtual HideBot, public virtual TrackBot {
public:
    ThirtyShotHideTrackBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Hide + Track" << endl;
        ThirtyShotBot::displayStats();
        HideBot::displayStats();
        TrackBot::displayStats();
    }
};

// ThirtyShot + Jump + Scout
class ThirtyShotJumpScoutBot : public virtual ThirtyShotBot, public virtual JumpBot, public virtual ScoutBot {
public:
    ThirtyShotJumpScoutBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Jump + Scout" << endl;
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
        ScoutBot::displayStats();
    }
};

// ThirtyShot + Jump + Track
class ThirtyShotJumpTrackBot : public virtual ThirtyShotBot, public virtual JumpBot, public virtual TrackBot {
public:
    ThirtyShotJumpTrackBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Jump + Track" << endl;
        ThirtyShotBot::displayStats();
        JumpBot::displayStats();
        TrackBot::displayStats();
    }
};

// ThirtyShot + Track + Hide
class ThirtyShotTrackHideBot : public virtual ThirtyShotBot, public virtual TrackBot, public virtual HideBot {
public:
    ThirtyShotTrackHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Track + Hide" << endl;
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
        HideBot::displayStats();
    }
};

// ThirtyShot + Track + Jump
class ThirtyShotTrackJumpBot : public virtual ThirtyShotBot, public virtual TrackBot, public virtual JumpBot {
public:
    ThirtyShotTrackJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          TrackBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Track + Jump" << endl;
        ThirtyShotBot::displayStats();
        TrackBot::displayStats();
        JumpBot::displayStats();
    }
};

// ThirtyShot + Scout + Hide
class ThirtyShotScoutHideBot : public virtual ThirtyShotBot, public virtual ScoutBot, public virtual HideBot {
public:
    ThirtyShotScoutHideBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield),
          HideBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Scout + Hide" << endl;
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
        HideBot::displayStats();
    }
};

// ThirtyShot + Scout + Jump
class ThirtyShotScoutJumpBot : public virtual ThirtyShotBot, public virtual ScoutBot, public virtual JumpBot {
public:
    ThirtyShotScoutJumpBot(const string& name, int x, int y, int w, int h, Battlefield* battlefield)
        : GenericRobot(name, x, y, w, h, battlefield),
          ThirtyShotBot(name, x, y, w, h, battlefield),
          ScoutBot(name, x, y, w, h, battlefield),
          JumpBot(name, x, y, w, h, battlefield) {}
    void displayStats() const override {
        cout << "Upgrade: ThirtyShot + Scout + Jump" << endl;
        ThirtyShotBot::displayStats();
        ScoutBot::displayStats();
        JumpBot::displayStats();
    }
};

#endif

import java.awt.*;

public class Attacks extends Rectangle {
    public int attackType;
    int attackSpeed;
    public boolean facingLeft;
    double damage;
    int charge;

    Attacks(int x, int y, boolean facingLeft, int attackType, int playerNum, int charge) {
        this.x = x;
        this.y = y;
        this.attackType = attackType;
        this.facingLeft = facingLeft;
        this.charge=charge;

        if (attackType == 0) {
            this.width = 0;
            this.height = 0;
        } else if (attackType == 1) {
            if (facingLeft) {
                this.x -= 130;
            } else {
                this.x += 10;
            }
            this.y -= 20;
            this.width = 160;
            this.height = 80;
            attackSpeed = 0;
            damage = 2.5;
        } else if (attackType == 2) {
            this.y -= 5;
            this.width = 80;
            this.height = 50;
            attackSpeed = 15;
            damage = 6;
        } else if (attackType == 3) {
            this.y -= 40;
            this.x -= 45;
            this.width = 125;
            this.height = 40;
            attackSpeed = 35;
            damage = 6;
        } else if (attackType == 4) {
            this.width = 0;
            this.height = 0;
        } else if (attackType == 5) {
            if (facingLeft) {
                this.x -= 120;
            } else {
                this.x += 20;
            }
            this.y -= 25;
            this.x += 10;
            this.width = 125;
            this.height = 90;
            damage = (charge * 2.5) / 10;
        } else if (attackType == 6) {
            this.width = 0;
            this.height = 0;
        } else if (attackType == 7) {
            this.x -= 30;
            this.y -= 160;
            this.width = 100;
            this.height = 150;
            damage = (charge * 2.5) / 10;
        }
        else if (attackType ==8) {
            this.width = 0;
            this.height = 0;
        }
        else if (attackType ==9) {
            this.width = 0;
            this.height = 0;
        }
        else if (attackType ==10) {
            this.width = 0;
            this.height = 0;
        }
        else if (attackType ==11) {
            this.width=350;
            this.height=75;
            this.y-=20;
            if (facingLeft){
                this.x-=350;
            }
            else{
                this.x+=40;
            }
            damage=6;
        }


    }
}

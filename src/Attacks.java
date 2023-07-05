import java.awt.*;

public class Attacks extends Rectangle {
    public int attackType;
    int attackSpeed;
    public boolean facingLeft;
    Attacks(int x,int y,boolean facingLeft,int attackType,int playerNum){
        this.x=x;
        this.y=y;
        this.attackType=attackType;
        this.facingLeft=facingLeft;

        if (attackType==0){
            this.width=0;
            this.height=0;
            attackSpeed=0;
        }
        else if (attackType==1){
            if (facingLeft){
                this.x-=130;
            }
            else{
                this.x+=30;
            }
            this.y-=20;
            this.width=140;
            this.height=80;
            attackSpeed=0;

            if (playerNum==1){
                GamePanel.player1.stunFrameOn=true;
                GamePanel.player1.stunFrameDuration=GamePanel.timeTracker+15;
            }
            else{
                GamePanel.player2.stunFrameOn=true;
                GamePanel.player2.stunFrameDuration=GamePanel.timeTracker+15;
            }
        }
        else if (attackType==2){
            this.y-=5;
            this.width=80;
            this.height=50;
            attackSpeed=15;
        }
        new Animations(x,y,attackType,facingLeft,attackSpeed,playerNum);
        GamePanel.allAttacks.add(this);
    }
}

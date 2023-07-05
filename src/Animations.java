import javax.swing.*;

public class Animations extends JLabel {
    public long animationExpireTime;
    public int attackSpeed;
    public boolean facingLeft;
    public int attackType;
    public int playerNum;
    Animations(int x,int y,int attackType,boolean facingLeft,int attackSpeed,int playerNum){
        this.attackSpeed=attackSpeed;
        this.setHorizontalAlignment(JLabel.CENTER);
        this.facingLeft=facingLeft;
        this.attackType=attackType;
        this.playerNum=playerNum;

        if (attackType==0){
            this.setIcon(new ImageIcon("./resources/doubleJump.gif"));
            this.setLocation(x-35,y-15);
            this.setBounds(x-35,y-15,100,100);
            animationExpireTime=GamePanel.timeTracker+25;
        }
        else if (attackType==1){
            if (!facingLeft) {
                this.setIcon(new ImageIcon("./resources/lightAttackR.gif"));
                this.setBounds(x-100,y-130,300,300);
                this.setLocation(x-100,y-130);
            }
            else{
                this.setIcon(new ImageIcon("./resources/lightAttack.gif"));
                this.setBounds(x-180,y-130,300,300);
                this.setLocation(x-180,y-130);
            }
            animationExpireTime=GamePanel.timeTracker+20;
        }
        else if (attackType==2){
            if (facingLeft){
                this.setIcon( new ImageIcon("./resources/special1R.gif"));
                this.setLocation(x-5,y-30);
                this.setBounds(x-5,y-30,100,100);

            }
            else{
                this.setIcon( new ImageIcon("./resources/special1.gif"));
                this.setLocation(x-25,y-30);
                this.setBounds(x-25,y-30,100,100);
            }
            animationExpireTime=GamePanel.timeTracker+120;
        }
        GamePanel.animations.add(this);
        GameFrame.gamePanel.add(this);

    }

}

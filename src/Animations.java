import javax.swing.*;

public class Animations extends JLabel {
    public long animationExpireTime;
    public int attackSpeed;
    public boolean facingLeft;
    public int attackType;
    public int playerNum;

    Animations(int x, int y, boolean facingLeft, int attackType, int playerNum, int attackSpeed) {
        this.attackSpeed = attackSpeed;
        this.setHorizontalAlignment(JLabel.CENTER);
        this.facingLeft = facingLeft;
        this.attackType = attackType;
        this.playerNum = playerNum;

        if (attackType == 0) {
            this.setIcon(new ImageIcon(getClass().getResource("/doubleJump.gif")));
            this.setLocation(x - 35, y - 15);
            this.setBounds(x - 35, y - 15, 100, 100);
            animationExpireTime = GamePanel.timeTracker + 25;
        } else if (attackType == 1) {
            if (!facingLeft) {
                this.setIcon(new ImageIcon(getClass().getResource("/lightAttackR.gif")));
                this.setBounds(x - 100, y - 130, 300, 300);
                this.setLocation(x - 100, y - 130);
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/lightAttack.gif")));
                this.setBounds(x - 180, y - 130, 300, 300);
                this.setLocation(x - 180, y - 130);
            }
            animationExpireTime = GamePanel.timeTracker + 20;
        } else if (attackType == 2) {
            if (facingLeft) {
                this.setIcon(new ImageIcon(getClass().getResource("/special1R.gif")));
                this.setLocation(x - 5, y - 30);
                this.setBounds(x - 5, y - 30, 100, 100);
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/special1.gif")));
                this.setLocation(x - 25, y - 30);
                this.setBounds(x - 25, y - 30, 100, 100);
            }
            animationExpireTime = GamePanel.timeTracker + 120;
        } else if (attackType == 3) {
            this.setIcon(new ImageIcon(getClass().getResource("/aerialSpecial.gif")));
            this.setLocation(x - 85, y - 80);
            this.setBounds(x - 85, y - 80, 200, 200);
            animationExpireTime = GamePanel.timeTracker + 30;
        } else if (attackType == 4) {
            this.setIcon(new ImageIcon(getClass().getResource("/charging.gif")));
            if (facingLeft) {
                this.setLocation(x - 10, y - 30);
                this.setBounds(x - 10, y - 30, 100, 100);
            } else {
                this.setLocation(x - 50, y - 30);
                this.setBounds(x - 50, y - 30, 100, 100);
            }
            animationExpireTime = GamePanel.timeTracker + 65;
            if (playerNum == 1) {
                GamePanel.player1.chargeAnimation.add(this);
            } else {
                GamePanel.player2.chargeAnimation.add(this);
            }
        } else if (attackType == 5) {
            if (facingLeft) {
                this.setLocation(x - 150, y - 80);
                this.setBounds(x - 150, y - 80, 200, 200);
            } else {
                this.setLocation(x - 10, y - 80);
                this.setBounds(x - 10, y - 80, 200, 200);
            }
            this.setIcon(new ImageIcon(getClass().getResource("/explosion.gif")));
            animationExpireTime = GamePanel.timeTracker + 30;
        } else if (attackType == 6) {
            if (facingLeft) {
                this.setIcon(new ImageIcon(getClass().getResource("/swordL.gif")));
                this.setLocation(x - 40, y - 50);
                this.setBounds(x - 40, y - 50, 200, 200);
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/sword.gif")));
                this.setLocation(x - 115, y - 50);
                this.setBounds(x - 115, y - 50, 200, 200);
            }
            animationExpireTime = GamePanel.timeTracker + 70;
            if (playerNum == 1) {
                GamePanel.player1.chargeAnimation.add(this);
            } else {
                GamePanel.player2.chargeAnimation.add(this);
            }
        } else if (attackType == 7) {
            this.setIcon(new ImageIcon(getClass().getResource("/slash.gif")));
            this.setLocation(x - 120, y - 200);
            this.setBounds(x - 120, y - 200, 300, 300);
            animationExpireTime = GamePanel.timeTracker + 30;
        } else if (attackType == 8) {
            this.setIcon(new ImageIcon(getClass().getResource("/fireBallAfterEffect.gif")));
            this.setLocation(x - 85, y - 80);
            this.setBounds(x - 85, y - 80, 200, 200);
            animationExpireTime = GamePanel.timeTracker + 20;
        } else if (attackType == 9) {
            if (facingLeft) {
                this.setIcon(new ImageIcon(getClass().getResource("/punchFinalL.gif")));
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/punchFinal.gif")));
            }
            this.setLocation(x - 85, y - 80);
            this.setBounds(x - 85, y - 80, 200, 200);
            animationExpireTime = GamePanel.timeTracker + 20;
        }
        else if (attackType == 10) {
            if (facingLeft) {
                this.setIcon(new ImageIcon(getClass().getResource("/forwardSwordL.gif")));
                this.setLocation(x - 40, y - 100);
                this.setBounds(x - 40, y - 100, 200, 200);
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/forwardSword.gif")));
                this.setLocation(x - 115, y - 100);
                this.setBounds(x - 115, y - 100, 200, 200);
            }
            animationExpireTime = GamePanel.timeTracker + 70;
            if (playerNum == 1) {
                GamePanel.player1.chargeAnimation.add(this);
            } else {
                GamePanel.player2.chargeAnimation.add(this);
            }
        }
        else if (attackType == 11) {
            if (facingLeft){
                this.setIcon(new ImageIcon(getClass().getResource("/forwardSlashL.gif")));
                this.setLocation(x -375, y - 150);
                this.setBounds(x -375, y - 150, 400, 300);
            }
            else {
                this.setIcon(new ImageIcon(getClass().getResource("/forwardSlash.gif")));
                this.setLocation(x + 10, y - 150);
                this.setBounds(x + 10, y - 150, 400, 300);
            }
            animationExpireTime = GamePanel.timeTracker + 20;
        }

        GameFrame.gamePanel.add(this);
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.*;



public class Players extends Rectangle {
    int lives = 3;
    double damagePercent = 0;
    boolean gravityAdjustment = true;
    boolean doubleJump = true;
    boolean floorTouched = true;
    boolean goingUp = true;
    int jumpSpeed = 15;
    final int PLAYER_SPEED = 8;
    int playerNumber;
    boolean[] movement = {false, false, false, false, false, false};
    boolean[] canAct = {true, true, true, true, true, true};

    boolean[] platformPosition = {true, false, false};
    final int MAX_JUMP_SPEED = 15;
    boolean facingLeft = false;
    Map<JButton, Integer> controls;
    ArrayList<JButton> buttons;
    ArrayList<Attacks> attacks;
    int[] attackCoolDowns = {0, 0, 0, 0, 0, 0};
    int velocity = 0;
    boolean hitDirection = false;
    boolean isStunned = false;
    boolean isStunned2 = false;
    int isStunned2Timer = 0;
    boolean stunFrameOn = true;
    int stunFrameDuration = 0;
    int dropCount = 0;
    int dropCountTimer = 0;
    boolean onPlatform = true;
    int comboCount = 0;
    int comboCountTimer = 0;
    boolean isInvincible = false;
    int invincibilityExpireTime = 0;
    boolean isAttacking = false;
    Random rand;





    Players(int x, int y, int width, int height, int playerNumber, Map<JButton, Integer> controls, ArrayList<JButton> buttons) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.playerNumber = playerNumber;
        this.controls = controls;
        this.buttons = buttons;
        attacks = new ArrayList<>();
        rand=new Random();

    }

    public void drawPlayer(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        if (playerNumber == 1) {
            g2D.setPaint(Color.RED);
        } else {
            g2D.setPaint(Color.BLUE);
        }
        g2D.draw(this);
        g2D.fill(this);
//        for (Rectangle attack : attacks) {
//            g2D.draw(attack);
//            g2D.fill(attack);
//        }
    }

    public void movePlayer(int keyCode) {
        for (int i = 0; i < movement.length; i++) {
            if (keyCode == controls.get(buttons.get(i))) {
                movement[i] = true;
            }
        }
        if (!isStunned && !isStunned2) {
            if (!isAttacking) {
                getAttacks(keyCode);
            }
            if (keyCode == controls.get(buttons.get(0)) && canAct[0]) {
                if (floorTouched || doubleJump) {
                    if (floorTouched) {
                        floorTouched = false;
                    } else {
                        doubleJump = false;
                    }
                    goingUp = true;
                    jumpSpeed = MAX_JUMP_SPEED;
                    attacks.add(new Attacks(x, y, facingLeft, 0, playerNumber));
                    canAct[0] = false;
                }
            }
            if (keyCode == controls.get(buttons.get(2)) && canAct[2]) {
                canAct[2] = false;
                dropCount++;
                dropCountTimer = GamePanel.timeTracker + 20;
            }
        }
    }



    public void getAttacks(int keyCode) {

         if (keyCode == controls.get(buttons.get(4)) && GamePanel.timeTracker > attackCoolDowns[0] && canAct[4]) {
            isAttacking = true;
            comboCount++;
            comboCountTimer = GamePanel.timeTracker + 60;
            canAct[4] = false;
            if (comboCount == 3) {
                attackCoolDowns[0] = GamePanel.timeTracker + 80;
            } else {
                attackCoolDowns[0] = GamePanel.timeTracker + 15;
            }
            attacks.add(new Attacks(x, y, facingLeft, 1, playerNumber));
        } else if (keyCode == controls.get(buttons.get(5)) && GamePanel.timeTracker > attackCoolDowns[1] && canAct[5]) {
            attackCoolDowns[1] = GamePanel.timeTracker + 100;
            attacks.add(new Attacks(x, y, facingLeft, 2, playerNumber));
            canAct[5] = false;
        }
    }




    public void callAttacks() {
        for (Attacks attack : attacks) {
            if (attack.attackSpeed != 0) {
                if (attack.facingLeft) {
                    attack.x -= attack.attackSpeed;
                } else {
                    attack.x += attack.attackSpeed;
                }
            }
        }
    }


    public void releaseKey(int keyCode) {
        for (int i = 1; i < movement.length; i++) {
            if (keyCode == controls.get(buttons.get(i))) {
                movement[i] = false;
                canAct[i] = true;

            }
        }
        if (keyCode == controls.get(buttons.get(0))) {
            canAct[0]=true;
        }
    }


    public void takeKnockBack() {
        if (velocity > 0) {
            if (hitDirection) {
                x -= velocity;
            } else {
                x += velocity;
            }
            velocity--;
            y -= (int) Math.sqrt(velocity * 8);
            if (velocity < 4) {
                isStunned = false;
            }
        }
        if (comboCount > 0) {
            if (comboCountTimer < GamePanel.timeTracker) {
                comboCount--;
            }
            if (comboCount == 4) {
                comboCount = 0;
            }
        }
    }


    public void platFormDrop() {
        if (dropCountTimer < GamePanel.timeTracker) {
            dropCount = 0;
        }
        if (!this.intersects(GamePanel.platforms.get(0)) && dropCount == 2) {
            y += 60;
            dropCount = 0;
        }
    }

    public void invincibility() {
        if (isInvincible && GamePanel.timeTracker > invincibilityExpireTime) {
            isInvincible = false;
        }
    }

    public void stun() {
        if (stunFrameDuration < GamePanel.timeTracker) {
            stunFrameOn = false;
        }
        if (isStunned2Timer < GamePanel.timeTracker) {
            isStunned2 = false;
        }
    }



    public void move() {
        invincibility();
        callAttacks();
        takeKnockBack();
        stun();
        platFormDrop();

        if (floorTouched && !this.intersects(GamePanel.platforms.get(0)) && !this.intersects(GamePanel.platforms.get(1)) && !this.intersects(GamePanel.platforms.get(2)) && !this.intersects(GamePanel.platforms.get(3))) {
            if (gravityAdjustment) {
                goingUp = false;
                jumpSpeed = 0;
                movement[0] = true;
                gravityAdjustment = false;
            }
        } else {
            onPlatform = true;
            if (this.intersects(GamePanel.platforms.get(0))) {
                platformPosition[0] = true;
                platformPosition[1] = false;
                platformPosition[2] = false;

            } else if (this.intersects(GamePanel.platforms.get(1)) || this.intersects(GamePanel.platforms.get(2))) {
                platformPosition[0] = false;
                platformPosition[1] = true;
                platformPosition[2] = false;
            } else if (this.intersects(GamePanel.platforms.get(3))) {
                platformPosition[0] = false;
                platformPosition[1] = false;
                platformPosition[2] = true;
            }

        }
        if (movement[0]) {
            jump();
        }
        if (!isStunned && !isStunned2 && !stunFrameOn) {
            if (movement[1]) {
                x -= PLAYER_SPEED;
                facingLeft = true;
            }
            if (movement[3]) {
                x += PLAYER_SPEED;
                facingLeft = false;
            }
        }
    }


    public void jump() {
        if (goingUp) {
            y -= jumpSpeed;
            jumpSpeed--;
            if (jumpSpeed == 0) {
                goingUp = false;
            }
        } else {
            gravity();
        }
        onPlatform = false;
    }

    public void gravity() {
        if (!this.intersects(GamePanel.platforms.get(0)) && !this.intersects(GamePanel.platforms.get(1)) && !this.intersects(GamePanel.platforms.get(2)) && !this.intersects(GamePanel.platforms.get(3))) {
            y += jumpSpeed;
            if (jumpSpeed < 15) {
                jumpSpeed++;
            }
        } else {
            goingUp = true;
            movement[0] = false;
            floorTouched = true;
            doubleJump = true;
            jumpSpeed = MAX_JUMP_SPEED;
            if (platformPosition[0]) {
                y = GamePanel.PLATFORM1_Y;
            } else if (platformPosition[1]) {
                y = GamePanel.PLATFORM2_Y;
            } else if (platformPosition[2]) {
                y = GamePanel.PLATFORM3_Y;
            }
            gravityAdjustment = true;
            onPlatform = true;
        }
    }


    public void playerDeath() {
        if (y > GamePanel.PANEL_HEIGHT + 300 || y < -300 || x < -300 || x > GamePanel.PANEL_WIDTH + 300) {
            y = GamePanel.PLATFORM1_Y - 400;
            x = rand.nextInt(900)+200;
            lives--;
            damagePercent = 0;
            velocity = 0;
            isStunned = false;
            isStunned2=false;
            if (lives==0){
                GameFrame.gamePanel.gameOver=true;
                GameFrame.gamePanel.gameOverTimer=System.currentTimeMillis();
            }
        }
    }
}


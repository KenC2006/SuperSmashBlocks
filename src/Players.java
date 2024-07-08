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
    int[] actionInstance = {0, 0, 0, 0, 0, 0};
    boolean[] canAct = {true, true, true, true, true, true};
    boolean[] release = {true, true, true, true, true, true};
    final int MAX_JUMP_SPEED = 15;
    boolean facingLeft = false;
    Map<JButton, Integer> controls;
    ArrayList<JButton> buttons;
    ArrayList<Attacks> attacks;
    ArrayList<Animations> chargeAnimation;
    ArrayList<Players> enemies;
    ArrayList<Animations> animations;

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
    boolean onPlatform = false;
    int comboCount = 0;
    int comboCountTimer = 0;
    boolean isInvincible = false;
    int invincibilityExpireTime = 0;
    boolean isAttacking = false;
    Random rand;
    int keyCode;
    int hitType = 1;
    boolean chargeAttacking = false;
    int charge = 0;
    boolean interrupted = false;
    int chargeType = 0;
    boolean jumped = false;
    int forwardSlashTimer = 0;
    int fallSpeed = 15;
    JLabel characterExpression;
    boolean respawnShieldOn = false;
    boolean isOn=false;
    int respawnPlatformX;

    Players(int x, int y, int width, int height, int playerNumber, Map<JButton, Integer> controls, ArrayList<JButton> buttons) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.playerNumber = playerNumber;
        this.controls = controls;
        this.buttons = buttons;
        attacks = new ArrayList<>();
        chargeAnimation = new ArrayList<>();
        enemies = new ArrayList<>();
        animations = new ArrayList<>();
        rand = new Random();
        characterExpression = new JLabel();
        if (playerNumber == 1) {
            characterExpression.setIcon(new ImageIcon("/eyeR.png"));
        } else {
            characterExpression.setIcon(new ImageIcon("/eye.png"));
        }
        characterExpression.setBounds(x, y, 20, 20);
        GameFrame.gamePanel.add(characterExpression);
        characterExpression.setLayout(null);
        characterExpression.setVisible(true);

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
        if (respawnShieldOn) {
            g2D.setPaint(new Color(102, 204, 255));
            g2D.setStroke(new BasicStroke(5));
            g2D.drawOval(x - 20, y - 20, 75, 75);
        }
        if (facingLeft) {
            characterExpression.setLocation(x + 5, y + 5);
        } else {
            characterExpression.setLocation(x + 15, y + 5);
        }
    }

    public void movePlayer(int keyCode) {
        this.keyCode = keyCode;
        for (int i = 0; i < movement.length; i++) {
            if (keyCode == controls.get(buttons.get(i))) {
                movement[i] = true;
                if (release[i]) {
                    actionInstance[i] = GamePanel.timeTracker + 3;
                    release[i] = false;
                }
            }
        }
        if (!isStunned && !isStunned2 && !chargeAttacking) {
            if (keyCode == controls.get(buttons.get(0)) && canAct[0]) {
                if (floorTouched || doubleJump) {
                    if (floorTouched) {
                        floorTouched = false;
                    } else {
                        doubleJump = false;
                    }
                    goingUp = true;
                    jumpSpeed = MAX_JUMP_SPEED;
                    attacks.add(new Attacks(x, y, facingLeft, 0, playerNumber, 0));
                    animations.add(new Animations(x, y, facingLeft, 0, playerNumber, 0));
                    canAct[0] = false;
                    jumped = false;
                }
            }
            if (keyCode == controls.get(buttons.get(2)) && canAct[2]) {
                canAct[2] = false;
                dropCount++;
                dropCountTimer = GamePanel.timeTracker + 20;
            }
        }
        if (!chargeAttacking) {
            if (keyCode == controls.get(buttons.get(1))) {
                facingLeft = true;
                characterExpression.setIcon(new ImageIcon(getClass().getResource("/eye.png")));
            } else if (keyCode == controls.get(buttons.get(3))) {
                facingLeft = false;
                characterExpression.setIcon(new ImageIcon(getClass().getResource("/eyeR.png")));
            }
        }
    }


    public void attackRead() {
        if (!isAttacking && !isStunned && !isStunned2) {
            if (actionInstance[5] > GamePanel.timeTracker) {
                if (actionInstance[0] > GamePanel.timeTracker && GamePanel.timeTracker > attackCoolDowns[2]) {
                    attackCoolDowns[2] = GamePanel.timeTracker + 100;
                    canAct[5] = false;
                    floorTouched = false;
                    doubleJump = false;
                    jumpSpeed=0;
                    attacks.add(new Attacks(x, y, facingLeft, 3, playerNumber, 0));
                    animations.add(new Animations(x, y, facingLeft, 3, playerNumber, 0));
                    isAttacking = true;
                    goingUp = false;
                } else if (actionInstance[1] > GamePanel.timeTracker || actionInstance[3] > GamePanel.timeTracker && canAct[5] && GamePanel.timeTracker > attackCoolDowns[5]) {
                    chargeType = 3;
                    forwardSlashTimer = GamePanel.timeTracker + 30;
                    chargeAttacking = true;
                    isAttacking = true;
                    canAct[5] = false;
                    attacks.add(new Attacks(x, y, facingLeft, 10, playerNumber, 0));
                    animations.add(new Animations(x, y, facingLeft, 10, playerNumber, 0));

                }
            } else if (keyCode == controls.get(buttons.get(5)) && GamePanel.timeTracker > attackCoolDowns[1] && canAct[5]) {
                attackCoolDowns[1] = GamePanel.timeTracker + 100;
                attacks.add(new Attacks(x, y, facingLeft, 2, playerNumber, 0));
                animations.add(new Animations(x, y, facingLeft, 2, playerNumber, 15));
                canAct[5] = false;
            }
            if (actionInstance[4] > GamePanel.timeTracker) {
                if ((actionInstance[1] > GamePanel.timeTracker || actionInstance[3] > GamePanel.timeTracker) && canAct[4] && GamePanel.timeTracker > attackCoolDowns[3]) {
                    chargeType = 1;
                    canAct[4] = false;
                    chargeAttacking = true;
                    isAttacking = true;
                    attacks.add(new Attacks(x, y, facingLeft, 4, playerNumber, 0));
                    animations.add(new Animations(x, y, facingLeft, 4, playerNumber, 0));
                } else if (actionInstance[0] > GamePanel.timeTracker && GamePanel.timeTracker > attackCoolDowns[4]) {
                    chargeType = 2;
                    canAct[4] = false;
                    canAct[0] = false;
                    chargeAttacking = true;
                    isAttacking = true;
                    attacks.add(new Attacks(x, y, facingLeft, 6, playerNumber, 0));
                    animations.add(new Animations(x, y, facingLeft, 6, playerNumber, 0));
                }
            } else if (keyCode == controls.get(buttons.get(4)) && GamePanel.timeTracker > attackCoolDowns[0] && canAct[4]) {
                isAttacking = true;
                comboCount++;
                comboCountTimer = GamePanel.timeTracker + 60;
                canAct[4] = false;
                if (comboCount == 3) {
                    attackCoolDowns[0] = GamePanel.timeTracker + 60;
                } else {
                    attackCoolDowns[0] = GamePanel.timeTracker + 25;
                }
                attacks.add(new Attacks(x, y, facingLeft, 1, playerNumber, 0));
                animations.add(new Animations(x, y, facingLeft, 1, playerNumber, 0));
                stunFrameOn = true;
                stunFrameDuration = GamePanel.timeTracker + 15;
            }
        }
    }

    public void chargeAttacks() {
        if (chargeAttacking) {
            fallSpeed = 2;
            jumpSpeed = 2;
            if (chargeType == 1) {
                if (facingLeft) {
                    chargeAnimation.get(0).setLocation(x - 10, y - 30);
                } else {
                    chargeAnimation.get(0).setLocation(x - 50, y - 30);
                }
            } else if (chargeType == 2) {
                if (facingLeft) {
                    chargeAnimation.get(0).setLocation(x - 40, y - 50);
                } else {
                    chargeAnimation.get(0).setLocation(x - 115, y - 50);
                }
            } else if (chargeType == 3) {
                if (facingLeft) {
                    chargeAnimation.get(0).setLocation(x - 40, y - 100);
                } else {
                    chargeAnimation.get(0).setLocation(x - 115, y - 100);
                }
            }
            if (chargeType != 3) {
                if (!canAct[4]) {
                    if (charge < 75) {
                        charge++;
                    } else {
                        chargeRelease();
                    }
                } else {
                    if (charge > 30) {
                        chargeRelease();
                    } else {
                        charge++;
                    }
                }
            } else {
                if (forwardSlashTimer < GamePanel.timeTracker) {
                    chargeRelease();
                }
            }
        }
    }


    public void chargeRelease() {
        fallSpeed = 15;
        chargeAnimation.get(0).animationExpireTime = -1;
        chargeAnimation.remove(0);
        if (!interrupted) {
            if (chargeType == 1) {
                attacks.add(new Attacks(x, y, facingLeft, 5, playerNumber, charge));
                animations.add(new Animations(x, y, facingLeft, 5, playerNumber, 0));
                attackCoolDowns[3] = GamePanel.timeTracker + 100;
            } else if (chargeType == 2) {
                attacks.add(new Attacks(x, y, facingLeft, 7, playerNumber, charge));
                animations.add(new Animations(x, y, facingLeft, 7, playerNumber, 0));
                attackCoolDowns[4] = GamePanel.timeTracker + 100;
            } else if (chargeType == 3) {
                attacks.add(new Attacks(x, y, facingLeft, 11, playerNumber, charge));
                animations.add(new Animations(x, y, facingLeft, 11, playerNumber, 0));
                attackCoolDowns[5] = GamePanel.timeTracker + 100;
                if (facingLeft) {
                    x -= 350;
                } else {
                    x += 350;
                }
                stunFrameOn = true;
                stunFrameDuration = GamePanel.timeTracker + 15;
            }

        } else {
            interrupted = false;
            isAttacking = false;
        }
        charge = 0;
        chargeAttacking = false;

    }

    public void controlAttacks() {
        for (int i = 0; i < attacks.size(); i++) {
            if (attacks.get(i).attackType == 2) {
                if (attacks.get(i).facingLeft) {
                    animations.get(i).setLocation(animations.get(i).getX() - animations.get(i).attackSpeed, animations.get(i).getY());
                } else {
                    animations.get(i).setLocation(animations.get(i).getX() + animations.get(i).attackSpeed, animations.get(i).getY());
                }
            } else if (attacks.get(i).attackType == 3) {
                animations.get(i).setLocation(x - 85, y - 80);
            }
            if (attacks.get(i).width != 0) {
                if (attacks.get(i).intersects(enemies.get(0))) {
                    takeDamage(attacks.get(i).attackType, attacks.get(i).facingLeft, attacks.get(i).damage);
                    if (attacks.get(i).attackType == 2) {
                        GameFrame.gamePanel.remove(animations.get(i));
                        animations.get(i).setVisible(false);
                        animations.remove(i);
                        attacks.remove(i);
                        attacks.add(new Attacks(enemies.get(0).x, enemies.get(0).y, facingLeft, 8, playerNumber, 0));
                        animations.add(new Animations(enemies.get(0).x, enemies.get(0).y, facingLeft, 8, playerNumber, 0));
                        break;
                    }
                }
            }
            if (GamePanel.timeTracker > animations.get(i).animationExpireTime) {
                GameFrame.gamePanel.remove(animations.get(i));
                if (attacks.get(i).attackType != 0 && attacks.get(i).attackType != 4 && attacks.get(i).attackType != 6) {
                    isAttacking = false;
                }
                attacks.remove(attacks.get(i));
                animations.get(i).setVisible(false);
                animations.remove(animations.get(i));

            }
        }
    }

    public void takeDamage(int attackType, boolean hitDirection, double damage) {
        if (!enemies.get(0).isInvincible) {
            enemies.get(0).hitDirection = hitDirection;
            enemies.get(0).damagePercent += damage;
            if (attackType == 1) {
                enemies.get(0).hitType = 1;
                if (comboCount == 3) {
                    enemies.get(0).velocity = (int) (15 * (1 + (enemies.get(0).damagePercent / 100.0)));
                    enemies.get(0).isStunned = true;
                    attacks.add(new Attacks(enemies.get(0).x, enemies.get(0).y, facingLeft, 9, playerNumber, 0));
                    animations.add(new Animations(enemies.get(0).x, enemies.get(0).y, facingLeft, 9, playerNumber, 0));
                    comboCount = 0;
                } else {
                    enemies.get(0).velocity = 5;
                    enemies.get(0).isStunned2 = true;
                    enemies.get(0).isStunned2Timer = GamePanel.timeTracker + 30;
                }
                enemies.get(0).isInvincible = true;
                enemies.get(0).invincibilityExpireTime = GamePanel.timeTracker + 25;
            } else if (attackType == 2) {
                enemies.get(0).hitType = 1;
                enemies.get(0).isStunned = true;
                enemies.get(0).velocity = (int) (15 * (1 + (enemies.get(0).damagePercent / 100.0)));
            } else if (attackType == 3) {
                enemies.get(0).hitType = 2;
                enemies.get(0).isStunned = true;
                enemies.get(0).velocity = (int) (12 * (1 + (enemies.get(0).damagePercent / 100.0)));
                enemies.get(0).isInvincible = true;
                enemies.get(0).invincibilityExpireTime = GamePanel.timeTracker + 20;
            } else if (attackType == 5) {
                enemies.get(0).hitType = 1;
                enemies.get(0).isStunned = true;
                enemies.get(0).velocity = (int) (20 * (1 + (enemies.get(0).damagePercent / 100.0)));
                enemies.get(0).isInvincible = true;
                enemies.get(0).invincibilityExpireTime = GamePanel.timeTracker + 20;
            } else if (attackType == 7) {
                enemies.get(0).hitType = 2;
                enemies.get(0).isStunned = true;
                enemies.get(0).velocity = (int) (15 * (1 + (enemies.get(0).damagePercent / 100.0) / 2));
                enemies.get(0).isInvincible = true;
                enemies.get(0).invincibilityExpireTime = GamePanel.timeTracker + 20;

            } else if (attackType == 11) {
                enemies.get(0).hitType = 1;
                enemies.get(0).isStunned2 = true;
                enemies.get(0).isStunned2Timer = GamePanel.timeTracker + 50;
                enemies.get(0).velocity = 15;
                enemies.get(0).isInvincible = true;
                enemies.get(0).invincibilityExpireTime = GamePanel.timeTracker + 20;
            }
            if (enemies.get(0).chargeAttacking) {
                enemies.get(0).interrupted = true;
            }
        }
    }


    public void callAttacks() {
        for (Attacks attack : attacks) {
            if (attack.attackType == 2) {
                if (attack.facingLeft) {
                    attack.x -= attack.attackSpeed;
                } else {
                    attack.x += attack.attackSpeed;
                }
            } else if (attack.attackType == 3) {
                y -= attack.attackSpeed;
                attack.y = y - 40;
                attack.x = x - 45;
                if (attack.attackSpeed > 0) {
                    attack.attackSpeed--;
                }
            }
        }
    }


    public void releaseKey(int keyCode) {
        for (int i = 1; i < movement.length; i++) {
            if (keyCode == controls.get(buttons.get(i))) {
                movement[i] = false;
                canAct[i] = true;
                release[i] = true;
            }
        }
        if (keyCode == controls.get(buttons.get(0))) {
            canAct[0] = true;
            release[0] = true;
        }
        if (chargeAttacking && keyCode == controls.get(buttons.get(4)) && charge > 30) {
            chargeRelease();
        }
        this.keyCode = 0;
    }


    public void takeKnockBack() {
        if (velocity > 0) {
            if (hitDirection) {
                x -= velocity;
            } else {
                x += velocity;
            }
            velocity--;
            if (hitType == 1) {
                y -= (int) Math.sqrt(velocity * 8);
            } else {
                y -= velocity * 2;
            }
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
        if (dropCount == 2) {
            for (int i=1;i<GamePanel.platforms.size();i++){
                if (intersects(GamePanel.platforms.get(i))){
                    isOn=true;
                }
            }
            if (isOn) {
                y += 80;
                dropCount = 0;
                isOn=false;
            }
        }
    }

    public void invincibility() {
        if (isInvincible && GamePanel.timeTracker > invincibilityExpireTime) {
            isInvincible = false;
            if (respawnShieldOn) {
                respawnShieldOn = false;
                for (int i=4;i<GamePanel.platforms.size();i++){
                    GamePanel.platforms.remove( GamePanel.platforms.get(i));
                }
            }
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
        if (!GameFrame.gamePanel.gameOver) {
            invincibility();
            callAttacks();
            takeKnockBack();
            stun();
            platFormDrop();
            attackRead();
            chargeAttacks();
            controlAttacks();
            playerDeath();

            if (floorTouched && !onPlatform) {
                if (gravityAdjustment) {
                    goingUp = false;
                    jumpSpeed = 0;
                    movement[0] = true;
                    gravityAdjustment = false;
                }
            }


            for (int i = 0; i < GamePanel.platforms.size(); i++) {
                if (intersects(GamePanel.platforms.get(i))) {
                    onPlatform = true;
                }
            }

            if (movement[0]) {
                jump();
                onPlatform = false;

            }

            if (!isStunned && !isStunned2 && !stunFrameOn && !chargeAttacking) {
                if (movement[1]) {
                    if (!new Rectangle(x - PLAYER_SPEED, y, 40, 40).intersects(enemies.get(0))) {
                        x -= PLAYER_SPEED;
                    }
                }
                if (movement[3]) {
                    if (!new Rectangle(x + PLAYER_SPEED, y, 40, 40).intersects(enemies.get(0))) {
                        x += PLAYER_SPEED;
                    }
                }
            }
        } else {
            for (Animations animation : animations) {
                animation.setVisible(false);
                GameFrame.gamePanel.remove(animation);
            }
            animations.clear();
            attacks.clear();
            isAttacking = false;
            movement[1] = false;
            movement[3] = false;
        }
    }


    public void jump() {
        if (goingUp) {
            if (!new Rectangle(x, y - 30, 40, 40).intersects(enemies.get(0)) && !chargeAttacking && !jumped) {
                y -= jumpSpeed;
            }
            jumpSpeed--;
            if (jumpSpeed == 0) {
                goingUp = false;
            }
        } else {
            gravity();
            jumped = true;
        }
    }

    public void gravity() {
        if (!onPlatform) {
            if (!new Rectangle(x, y + 15, 40, 40).intersects(enemies.get(0))) {
                y += jumpSpeed;
                if (jumpSpeed < fallSpeed) {
                    jumpSpeed++;
                }
            } else {
                floorTouched = true;
                doubleJump = true;
            }
        } else {
            goingUp = true;
            movement[0] = false;
            release[0] = true;
            floorTouched = true;
            doubleJump = true;
            jumpSpeed = MAX_JUMP_SPEED;
            gravityAdjustment = true;
            for (int i = 0; i < GamePanel.platforms.size(); i++) {
                if (intersects(GamePanel.platforms.get(i))) {
                    y = GamePanel.platforms.get(i).y - 39;

                }
            }

        }
    }


    public void playerDeath() {
        if (y > GamePanel.PANEL_HEIGHT + 300 || y < -300 || x < -300 || x > GamePanel.PANEL_WIDTH + 300) {
            y = GamePanel.PLATFORM1_Y - 450;
            respawnPlatformX = rand.nextInt(800) + 200;
            x=respawnPlatformX+25;
            lives--;
            damagePercent = 0;
            velocity = 0;
            isStunned = false;
            isStunned2 = false;
            isInvincible = true;
            respawnShieldOn = true;
            invincibilityExpireTime = GamePanel.timeTracker + 200;
            GamePanel.platforms.add(new Rectangle(respawnPlatformX, 50, 100, 15));
            if (lives == 0) {
                GameFrame.gamePanel.gameOver = true;
                GameFrame.gamePanel.gameOverTimer = System.currentTimeMillis();
            }
        }
    }
}


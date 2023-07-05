import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    //Timer utils
    Timer timer;
    final int GAME_TICKS = 5;
    public static int timeTracker = 0;

    //Buttons
    JButton startButton;
    boolean gameOn;
    boolean gameCreated=false;
    JButton controlsButton;
    boolean controlsOn;
    JButton movesButton;
    boolean movesOn;
    JButton playerSelector;
    boolean player1Controls = true;
    boolean keyReplacement = false;
    ArrayList<JButton> buttons;
    JButton playerJumpB;
    JButton playerDownB;
    JButton playerLeftB;
    JButton playerRightB;
    JButton playerLightAttackB;
    JButton playerHeavyAttackB;
    JButton confirmButton;
    JButton keyToBeReplaced;
    boolean buttonsCreated;

    //Character controls
    Map<JButton, Integer> player1C;
    Map<JButton, Integer> player2C;

    //Up,Left,Down,Right

    //Platform utils
    //0=main platform, 1=left most platform, 2=right most platform, 3=top platform
    public static ArrayList<Rectangle> platforms;

    //Attack animations
    public static ArrayList<Attacks> allAttacks;
    public static ArrayList<Animations> animations;

    public static final int PLATFORM1_Y = 511;
    public static final int PLATFORM2_Y = 331;
    public static final int PLATFORM3_Y = 151;

    //Player
    public static Players player1;
    public static Players player2;

    //Panel values
    public static final int PANEL_WIDTH = 1200;
    public static final int PANEL_HEIGHT = 700;
    Image gameTitle;
    boolean gameOver=false;
    long gameOverTimer;
    Image gameSet;


    GamePanel() {


        this.setLayout(null);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(GAME_TICKS, this);
        timer.start();

        player1C = new HashMap<>();
        player2C = new HashMap<>();

        buttons = new ArrayList<>();
        platforms = new ArrayList<>();

        allAttacks = new ArrayList<>();
        animations = new ArrayList<>();


        platforms.add(new Rectangle(100, 550, 1000, 15));
        platforms.add(new Rectangle(200, 370, 300, 15));
        platforms.add(new Rectangle(700, 370, 300, 15));
        platforms.add(new Rectangle(450, 190, 300, 15));

        startButton = new JButton();
        createButton(startButton, 500, 300, 200, 40, "Start Game", true);
        controlsButton = new JButton();
        createButton(controlsButton, 500, 350, 200, 40, "Controls", true);
        movesButton = new JButton();
        createButton(movesButton, 500, 400, 200, 40, "Moves/Skills", true);
        playerSelector = new JButton();
        createButton(playerSelector, 500, 125, 200, 40, "Player 1 Controls", false);
        playerJumpB = new JButton();
        createButton(playerJumpB, 500, 225, 200, 50, "w", false);
        playerDownB = new JButton();
        createButton(playerDownB, 500, 325, 200, 50, "s", false);
        playerLeftB = new JButton();
        createButton(playerLeftB, 300, 275, 200, 50, "a", false);
        playerRightB = new JButton();
        createButton(playerRightB, 700, 275, 200, 50, "d", false);
        playerLightAttackB = new JButton();
        createButton(playerLightAttackB, 375, 425, 200, 50, "f", false);
        playerHeavyAttackB = new JButton();
        createButton(playerHeavyAttackB, 625, 425, 200, 50, "g", false);
        confirmButton = new JButton();
        createButton(confirmButton, 500, 500, 200, 50, "Confirm", false);
        buttons.add(playerJumpB);
        buttons.add(playerLeftB);
        buttons.add(playerDownB);
        buttons.add(playerRightB);
        buttons.add(playerLightAttackB);
        buttons.add(playerHeavyAttackB);

        player1C.put(playerJumpB, 87);
        player1C.put(playerLeftB, 65);
        player1C.put(playerDownB, 83);
        player1C.put(playerRightB, 68);
        player1C.put(playerLightAttackB, 70);
        player1C.put(playerHeavyAttackB, 71);

        player2C.put(playerJumpB, 38);
        player2C.put(playerLeftB, 37);
        player2C.put(playerDownB, 40);
        player2C.put(playerRightB, 39);
        player2C.put(playerLightAttackB, 46);
        player2C.put(playerHeavyAttackB, 44);

        buttonsCreated = true;

        gameTitle=new ImageIcon("./resources/gameTitle.png").getImage();
        gameSet=new ImageIcon("./resources/gameSet.png").getImage();
    }

    public void createButton(JButton button, int x, int y, int width, int height, String text, Boolean visibility) {
        button.setBounds(x, y, width, height);
        button.setFont(new Font("ComicSans", Font.BOLD, 20));
        button.setText(text);
        button.addActionListener(this);
        button.setVisible(visibility);
        button.addKeyListener(this);
        this.add(button);


    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        if (gameOn) {
            g2D.setPaint(Color.white);
            for (Rectangle platform : platforms) {
                g2D.draw(platform);
                g2D.fill(platform);
            }
            player1.drawPlayer(g);
            player2.drawPlayer(g);
            g.setFont(new Font("ComicSans", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("Lives: " + player1.lives, 280, 620);
            g.drawString(player1.damagePercent + "%", 320, 670);
            g.setColor(Color.BLUE);
            g.drawString("Lives: " + player2.lives, 770, 620);
            g.drawString(player2.damagePercent + "%", 810, 670);
            if (gameOver){
                if (System.currentTimeMillis()-gameOverTimer>7000){
                    gameOn=false;
                    gameOver=false;
                    player1.lives=3;
                    player2.lives=3;
                    player1.damagePercent=0;
                    player2.damagePercent=0;
                }
                else if (System.currentTimeMillis()-gameOverTimer>3000){
                    String winner;
                    if (player1.lives==0){
                        winner="BLUE";
                        g2D.setPaint(Color.BLUE);
                    }
                    else{
                        winner="RED";
                        g2D.setPaint(Color.RED);
                    }
                    g2D.fillRect(400,200,400,200);
                    g.setColor(Color.YELLOW);
                    g.setFont(new Font("ComicSans", Font.BOLD, 35));
                    g.drawString("Winner: "+winner+" Player",425,300);
                }
                else if (System.currentTimeMillis()-gameOverTimer>0){
                    g2D.drawImage(gameSet, -100, 0, null);
                }

            }
        }
        else if (controlsOn) {
            if (player1Controls) {
                g2D.setPaint(Color.RED);
            } else {
                g2D.setPaint(Color.BLUE);
            }
            g2D.fillRect(300, 100, 600, 500);
            if (keyReplacement) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("ComicSans", Font.BOLD, 20));
                g.drawString("Press any key to replace", 485, 200);
            }
        }
        else{
            g2D.drawImage(gameTitle,300,0,null);
        }

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (keyReplacement) {
                changeKeys2(keyToBeReplaced, e.getKeyCode());
            }
            if (gameOn) {
                player1.movePlayer(e.getKeyCode());
                player2.movePlayer(e.getKeyCode());

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOn) {
            player1.releaseKey(e.getKeyCode());
            player2.releaseKey(e.getKeyCode());
        }
    }


    public void controlAnimation() {
        for (int i = 0; i < allAttacks.size(); i++) {
            if (allAttacks.get(i).attackType != 0) {
                if (allAttacks.get(i).facingLeft) {
                    animations.get(i).setLocation(animations.get(i).getX() - animations.get(i).attackSpeed, animations.get(i).getY());
                } else {
                    animations.get(i).setLocation(animations.get(i).getX() + animations.get(i).attackSpeed, animations.get(i).getY());
                }
                if (animations.get(i).playerNum == 1 && allAttacks.get(i).intersects(player2)) {
                    takeDamage(2, allAttacks.get(i).attackType, allAttacks.get(i).facingLeft);
                    if (allAttacks.get(i).attackType != 1) {
                        this.remove(animations.get(i));
                        animations.get(i).setVisible(false);
                        animations.remove(i);
                        player1.attacks.remove(allAttacks.get(i));
                        allAttacks.remove(i);
                        break;
                    }
                } else if (animations.get(i).playerNum == 2 && allAttacks.get(i).intersects(player1)) {
                    takeDamage(1, allAttacks.get(i).attackType, allAttacks.get(i).facingLeft);
                    if (allAttacks.get(i).attackType != 1) {
                        this.remove(animations.get(i));
                        animations.get(i).setVisible(false);
                        animations.remove(i);
                        player2.attacks.remove(allAttacks.get(i));
                        allAttacks.remove(i);
                        break;
                    }
                }
            }
            if (timeTracker > animations.get(i).animationExpireTime) {
                if (animations.get(i).playerNum == 1) {
                    player1.attacks.remove(allAttacks.get(i));
                    player1.isAttacking = false;

                } else if (animations.get(i).playerNum == 2) {
                    player2.attacks.remove(allAttacks.get(i));
                    player2.isAttacking = false;
                }

                this.remove(animations.get(i));
                animations.get(i).setVisible(false);
                animations.remove(animations.get(i));
                allAttacks.remove(allAttacks.get(i));
            }
        }

    }

    public void takeDamage(int playerNum, int attackType, boolean hitDirection) {
        if (attackType == 1) {
            if (playerNum == 1 && !player1.isInvincible) {
                player1.hitDirection = hitDirection;
                if (player2.comboCount == 3) {
                    player1.velocity = (int) (20 * (1 + (player1.damagePercent / 100.0)));
                    player1.isStunned = true;
                } else {
                    player1.velocity = 5;
                    player1.isStunned2 = true;
                    player1.isStunned2Timer = timeTracker + 30;
                }
                player1.damagePercent += 2.5;
                player1.isInvincible = true;
                player1.invincibilityExpireTime = timeTracker + 20;
            } else if (playerNum == 2 && !player2.isInvincible) {
                player2.hitDirection = hitDirection;
                if (player1.comboCount == 3) {
                    player2.velocity = (int) (15 * (1 + (player2.damagePercent / 100.0)));
                    player2.isStunned = true;
                } else {
                    player2.velocity = 5;
                    player2.isStunned2 = true;
                    player2.isStunned2Timer = timeTracker + 30;
                }
                player2.damagePercent += 2.5;
                player2.isInvincible = true;
                player2.invincibilityExpireTime = timeTracker + 20;
            }
        } else if (attackType == 2) {
            if (playerNum == 1) {
                player1.hitDirection = hitDirection;
                player1.isStunned = true;
                player1.velocity = (int) (15 * (1 + (player1.damagePercent / 100.0)));
                player1.damagePercent += 6;
            } else {
                player2.hitDirection = hitDirection;
                player2.isStunned = true;
                player2.velocity = (int) (15 * (1 + (player2.damagePercent / 100.0)));
                player2.damagePercent += 6;
            }
        }

    }


    public void actionPerformed(ActionEvent e) {
        if (buttonsCreated) {
            buttonSelection(e);
        }
        controls(e);
        if (gameOn) {
            timeTracker++;
            if (!gameOver) {
                player1.move();
                player2.move();
            }
            player1.playerDeath();
            player2.playerDeath();
            controlAnimation();
        }
        repaint();
    }


    public void buttonSelection(ActionEvent e) {
        if (e.getSource() == startButton) {
            gameOn = true;
            if (!gameCreated) {
                player1 = new Players(150, PLATFORM1_Y, 40, 40, 1, player1C, buttons);
                player2 = new Players(1010, PLATFORM1_Y, 40, 40, 2, player2C, buttons);
            }
            player1.x=150;
            player2.x=1010;
            player1.y=400;
            player2.y=400;
            gameCreated=true;
        } else if (e.getSource() == controlsButton) {
            controlsOn = true;

        } else if (e.getSource() == movesButton) {
            movesOn = true;
        }
        if (movesOn || gameOn || controlsOn) {
            startButton.setVisible(false);
            movesButton.setVisible(false);
            controlsButton.setVisible(false);
        } else {
            startButton.setVisible(true);
            movesButton.setVisible(true);
            controlsButton.setVisible(true);

        }

    }

    public void changeKeys1(JButton button) {
        for (JButton jButton : buttons) {
            jButton.setBackground(Color.WHITE);
        }
        button.setBackground(Color.YELLOW);
        keyReplacement = true;

    }

    public void changeKeys2(JButton button, int keyCode) {
        if (player1Controls) {
            player1C.replace(button, keyCode);
        } else {
            player2C.replace(button, keyCode);
        }
        for (JButton jButton : buttons) {
            jButton.setBackground(Color.WHITE);
        }
        keyReplacement = false;
    }

    public void controls(ActionEvent e) {
        if (controlsOn) {
            for (JButton jButton : buttons) {
                jButton.setVisible(true);
            }
            confirmButton.setVisible(true);
            playerSelector.setVisible(true);
            if (e.getSource() == playerSelector && !keyReplacement) {
                player1Controls = !player1Controls;
            }
            if (e.getSource() == confirmButton && !keyReplacement) {
                controlsOn = false;
                confirmButton.setVisible(false);
                playerSelector.setVisible(false);
                for (JButton button : buttons) {
                    button.setVisible(false);
                }
            }
            if (player1Controls) {
                playerSelector.setText("Player 1 Controls");
                for (JButton button : buttons) {
                    button.setText(KeyEvent.getKeyText(player1C.get(button)));
                }
            } else {
                playerSelector.setText("Player 2 Controls");
                for (JButton button : buttons) {
                    button.setText(KeyEvent.getKeyText(player2C.get(button)));
                }
            }
            if (e.getSource() instanceof JButton) {
                if (e.getSource() != confirmButton && e.getSource() != playerSelector && e.getSource() != controlsButton) {
                    changeKeys1((JButton) e.getSource());
                    keyToBeReplaced = (JButton) e.getSource();
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }
}

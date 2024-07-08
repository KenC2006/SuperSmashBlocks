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
    JButton playerNormalAttackB;
    JButton playerSpecialAttackB;
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

    public static final int PLATFORM1_Y = 511;

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
    int startTimer;
    boolean gameBegin=false;
    JLabel startCountDown;



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
        playerNormalAttackB = new JButton();
        createButton(playerNormalAttackB, 375, 425, 200, 50, "f", false);
        playerSpecialAttackB = new JButton();
        createButton(playerSpecialAttackB, 625, 425, 200, 50, "g", false);
        confirmButton = new JButton();
        createButton(confirmButton, 500, 500, 200, 50, "Confirm", false);
        buttons.add(playerJumpB);
        buttons.add(playerLeftB);
        buttons.add(playerDownB);
        buttons.add(playerRightB);
        buttons.add(playerNormalAttackB);
        buttons.add(playerSpecialAttackB);

        player1C.put(playerJumpB, 87);
        player1C.put(playerLeftB, 65);
        player1C.put(playerDownB, 83);
        player1C.put(playerRightB, 68);
        player1C.put(playerNormalAttackB, 70);
        player1C.put(playerSpecialAttackB, 71);

        player2C.put(playerJumpB, 38);
        player2C.put(playerLeftB, 37);
        player2C.put(playerDownB, 40);
        player2C.put(playerRightB, 39);
        player2C.put(playerNormalAttackB, 46);
        player2C.put(playerSpecialAttackB, 44);

        buttonsCreated = true;

        gameTitle=new ImageIcon(getClass().getResource("/gameTitle.png")).getImage();
        gameSet=new ImageIcon(getClass().getResource("/gameSet.png")).getImage();
        startCountDown=new JLabel();
        startCountDown.setIcon(new ImageIcon(getClass().getResource("/321GO.gif")));
        startCountDown.setBounds(0,0,1000,500);
        startCountDown.setLocation(160,100);
        startCountDown.setVisible(false);
        this.add(startCountDown);

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
                    player1.characterExpression.setVisible(false);
                    player2.characterExpression.setVisible(false);
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
                    g2D.fillRect(400,240,400,100);
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
        else if (movesOn){
            g2D.fillRect(300, 100, 600, 500);
            g.setFont(new Font("ComicSans", Font.BOLD, 20));
            g.setColor(Color.ORANGE);
            if (player1Controls){
                g.drawString(KeyEvent.getKeyText(player1C.get(playerNormalAttackB))+" for light attack",525,210);
                g.drawString(KeyEvent.getKeyText(player1C.get(playerSpecialAttackB))+" for special ranged attack",475,260);
                g.drawString(KeyEvent.getKeyText(player1C.get(playerNormalAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerRightB))+" or "+KeyEvent.getKeyText(player1C.get(playerLeftB))+" for heavy side attack",455,310);
                g.drawString(KeyEvent.getKeyText(player1C.get(playerNormalAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerJumpB))+" for heavy up attack",475,360);
                g.drawString(KeyEvent.getKeyText(player1C.get(playerSpecialAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerJumpB))+" for aerial jump attack",470,410);
                g.drawString(KeyEvent.getKeyText(player1C.get(playerSpecialAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerRightB))+" or "+KeyEvent.getKeyText(player1C.get(playerLeftB))+" for special side dash attack",420,460);
            }
            else{
                g.drawString(KeyEvent.getKeyText(player2C.get(playerNormalAttackB))+" for light attack",525,210);
                g.drawString(KeyEvent.getKeyText(player2C.get(playerSpecialAttackB))+" for special ranged attack",475,260);
                g.drawString(KeyEvent.getKeyText(player2C.get(playerNormalAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerRightB))+" or "+KeyEvent.getKeyText(player1C.get(playerLeftB))+" for heavy side attack",455,310);
                g.drawString(KeyEvent.getKeyText(player2C.get(playerNormalAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerJumpB))+" for heavy up attack",475,360);
                g.drawString(KeyEvent.getKeyText(player2C.get(playerSpecialAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerJumpB))+" for aerial jump attack",470,410);
                g.drawString(KeyEvent.getKeyText(player2C.get(playerSpecialAttackB))+" + "+KeyEvent.getKeyText(player1C.get(playerRightB))+" or "+KeyEvent.getKeyText(player1C.get(playerLeftB))+" for special side dash attack",420,460);
            }
        }
        else{
            g2D.drawImage(gameTitle,300,0,null);
        }

    }


    @Override
    public void keyPressed(KeyEvent e) {
            if (keyReplacement) {
                changeKeys2(keyToBeReplaced, e.getKeyCode());
            }
            if (gameBegin && !gameOver) {
                player1.movePlayer(e.getKeyCode());
                player2.movePlayer(e.getKeyCode());

            }
        }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOn) {
            player1.releaseKey(e.getKeyCode());
            player2.releaseKey(e.getKeyCode());
        }
    }




    public void actionPerformed(ActionEvent e) {
        if (buttonsCreated) {
            buttonSelection(e);
        }
        controls(e);
        moves(e);
        if (gameOn) {
            timeTracker++;
            player1.move();
            player2.move();
            if (timeTracker>startTimer && !gameBegin){
                gameBegin=true;
                startCountDown.setVisible(false);
            }
            else if (timeTracker<startTimer && !gameBegin){
                startCountDown.setVisible(true);
            }
        }
        repaint();
    }


    public void buttonSelection(ActionEvent e) {
        if (e.getSource() == startButton) {
            gameBegin=false;
            gameOn = true;
            startTimer=timeTracker+300;
            if (!gameCreated) {
                player1 = new Players(150, PLATFORM1_Y, 40, 40, 1, player1C, buttons);
                player2 = new Players(1010, PLATFORM1_Y, 40, 40, 2, player2C, buttons);
                player1.enemies.add(player2);
                player2.enemies.add(player1);
            }
            player1.characterExpression.setVisible(true);
            player2.characterExpression.setVisible(true);
            player1.x=150;
            player2.x=1010;
            player1.y=400;
            player2.y=400;
            player1.facingLeft=false;
            player2.facingLeft=true;
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

    public void moves(ActionEvent e){
        if (movesOn){
            confirmButton.setVisible(true);
            if (player1Controls) {
                playerSelector.setText("Player 1");
            }
            else{
                playerSelector.setText("Player 2");
            }
            playerSelector.setVisible(true);
            if (e.getSource() == playerSelector) {
                player1Controls = !player1Controls;
            }
        }
        if (e.getSource() == confirmButton) {
            movesOn = false;
            confirmButton.setVisible(false);
            playerSelector.setVisible(false);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }
}

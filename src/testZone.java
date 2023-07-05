import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class testZone extends JPanel implements KeyListener, ActionListener {

    //Timer utils
    Timer timer;
    final int GAME_TICKS = 5;

    //Buttons
    boolean gameOn;
    boolean controlsOn;
    boolean movesOn;
    boolean player1Controls = true;
    boolean keyReplacement = false;
    ArrayList<JButton>buttons;
    JButton keyToBeReplaced;

    boolean buttonsCreated;

    //Character controls
    Map<JButton, Integer> player1C;
    Map<JButton, Integer> player2C;

    //Up,Left,Down,Right

    //Platform utils
    //0=main platform, 1=left most platform, 2=right most platform, 3=top platform
    public static ArrayList<Rectangle>platforms;

    public static final int PLATFORM1_Y=511;
    public static final int PLATFORM2_Y=331;
    public static final int PLATFORM3_Y=151;

    //Player
    Players player1;
    Players player2;

    //Panel values
    public static final int PANEL_WIDTH=1200;
    public static final int PANEL_HEIGHT=700;

    testZone() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(GAME_TICKS, this);
        timer.start();

        player1C = new HashMap<>();
        player2C = new HashMap<>();

        buttons= new ArrayList<>();
        platforms= new ArrayList<>();

        platforms.add(new Rectangle(100, 550, 1000, 15));
        platforms.add(new Rectangle(200, 370, 300, 15));
        platforms.add(new Rectangle(700, 370, 300, 15));
        platforms.add(new Rectangle(450, 190, 300, 15));

        createButton( 500, 300, 200, 40, "Start Game", true); //start button - 0
        createButton( 500, 350, 200, 40, "Controls", true); //controls button - 1
        createButton( 500, 400, 200, 40, "Moves/Skills", true); //move button - 2
        createButton( 500, 125, 200, 40, "Player 1 Controls", false); //player selector button - 3
        createButton( 500, 500, 200, 50, "Confirm", false); //controls confirmation button - 4

        createButton( 500, 225, 200, 50, "w", false); //jump button - 5
        createButton( 300, 275, 200, 50, "a", false); //player left button - 6
        createButton( 500, 325, 200, 50, "s", false); //player down button - 7
        createButton( 700, 275, 200, 50, "d", false); //player right button - 8
        createButton( 375, 425, 200, 50, "f", false); //player light attack button - 9
        createButton( 625, 425, 200, 50, "g", false); //player heavy attack button - 10

        player1C.put(buttons.get(5), 87);
        player1C.put(buttons.get(6), 65);
        player1C.put(buttons.get(7), 83);
        player1C.put(buttons.get(8), 68);
        player1C.put(buttons.get(9), 70);
        player1C.put(buttons.get(10), 71);

        player2C.put(buttons.get(5), 38);
        player2C.put(buttons.get(6), 37);
        player2C.put(buttons.get(7), 40);
        player2C.put(buttons.get(8), 39);
        player2C.put(buttons.get(9), 46);
        player2C.put(buttons.get(10), 44);

        buttonsCreated=true;
    }

    public void createButton(int x, int y, int width, int height, String text, Boolean visibility) {
        JButton button=new JButton();
        button.setBounds(x, y, width, height);
        button.setFont(new Font("ComicSans", Font.BOLD, 20));
        button.setText(text);
        button.addActionListener(this);
        button.setVisible(visibility);
        button.addKeyListener(this);
        buttons.add(button);
        this.add(button);

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        if (gameOn) {
            for (Rectangle platform : platforms) {
                g2D.draw(platform);
                g2D.fill(platform);
            }
            player1.drawPlayer(g);
            player2.drawPlayer(g);
            g.setFont(new Font("ComicSans", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("Lives: "+player1.lives,280,620);
            g.drawString(player1.damagePercent+"%",320,670);
            g.setColor(Color.BLUE);
            g.drawString("Lives: "+player2.lives,770,620);
            g.drawString(player2.damagePercent+"%",810,670);
        }
        if (controlsOn) {
            if (player1Controls) {
                g2D.setPaint(Color.RED);
            } else {
                g2D.setPaint(Color.BLUE);
            }
            g2D.fillRect(300, 100, 600, 500);
            if (keyReplacement){
                g.setColor(Color.BLACK);
                g.setFont(new Font("ComicSans", Font.BOLD, 20));
                g.drawString("Press any key to replace",485,200);
            }
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (keyReplacement) {
            changeKeys2(keyToBeReplaced, e.getKeyCode());
        }
        if (gameOn){
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
        repaint();
        if (gameOn) {
            player1.move();
            player2.move();
            player1.playerDeath();
            player2.playerDeath();
        }
    }













    public void buttonSelection(ActionEvent e){
        if (e.getSource() == buttons.get(0)) {
            gameOn = true;
            player1=new Players(150,PLATFORM1_Y,40,40,1,player1C,buttons);
            player2=new Players(1010,PLATFORM1_Y,40,40,2,player2C,buttons);
        } else if (e.getSource() == buttons.get(1)) {
            controlsOn = true;

        } else if (e.getSource() == buttons.get(2)) {
            movesOn = true;
        }
        if (movesOn || gameOn || controlsOn) {
            buttons.get(0).setVisible(false);
            buttons.get(1).setVisible(false);
            buttons.get(2).setVisible(false);
        } else {
            buttons.get(0).setVisible(true);
            buttons.get(1).setVisible(true);
            buttons.get(2).setVisible(true);
        }

    }
    public void changeKeys1(JButton button) {
        for (int i=5;i<buttons.size();i++) {
            buttons.get(i).setBackground(Color.WHITE);
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
        for (int i=5;i<buttons.size();i++) {
            buttons.get(i).setBackground(Color.WHITE);
        }
        keyReplacement = false;
    }

    public void controls(ActionEvent e) {
        if (controlsOn) {
            for (int i=5;i<buttons.size();i++) {
                buttons.get(i).setVisible(true);
            }
            buttons.get(4).setVisible(true);
            buttons.get(3).setVisible(true);
            if (e.getSource() == buttons.get(3) && !keyReplacement) {
                player1Controls = !player1Controls;
            }
            if (e.getSource() == buttons.get(4) && !keyReplacement) {
                controlsOn = false;
                buttons.get(4).setVisible(false);
                buttons.get(3).setVisible(false);
                for (JButton button : buttons) {
                    button.setVisible(false);
                }
            }
            if (player1Controls) {
                buttons.get(3).setText("Player 1 Controls");
                for (int i=5;i<buttons.size();i++) {
                    buttons.get(i).setText(KeyEvent.getKeyText(player1C.get(buttons.get(i))));
                }
            } else {
                buttons.get(3).setText("Player 2 Controls");
                for (int i=5;i<buttons.size();i++) {
                    buttons.get(i).setText(KeyEvent.getKeyText(player2C.get(buttons.get(i))));
                }
            }
            if (e.getSource() instanceof JButton) {
                if (e.getSource() != buttons.get(4) && e.getSource() != buttons.get(3) && e.getSource() != buttons.get(1)) {
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

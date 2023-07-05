import javax.swing.*;
public class GameFrame extends JFrame {
    public static GamePanel gamePanel;
    GameFrame()  {
        gamePanel=new GamePanel();
        this.add(gamePanel);
        this.setTitle("SuperSmashBlocks");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
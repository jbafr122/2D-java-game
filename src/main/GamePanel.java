package src.main;
import src.entity.Player;
import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    
    // Game Screen Configuration
    final int originalTitleSize = 16; // 16 x 16 tile size
    final int scale = 3;

    public final int tileSize = originalTitleSize * scale; //48 x 48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // FPS = 60
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startGameThread();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // Calls the run method when called
    }

    @Override // The core game loop to ensure the game keeps moving
    public void run() {

        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){
            long currentTime = System.nanoTime();
            // Update information such as character changes positions
            update();
            // Draw screen with the updated information
            repaint(); // how paintComponent method is called

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; // Convert from nano to milliseconds

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void update(){
        // X Values increase to the right
        // Y values increase as they do down
       player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        player.draw(g2);
        g2.dispose();
    }
}

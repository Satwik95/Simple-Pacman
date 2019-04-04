import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Main_Game extends Canvas implements Runnable, KeyListener {
    /**
     * Using Java AWT to make the graphics, and thread to run the game loop.
     * Once we set the basic graphics window, title size etc, we start the thread.
     *
     * The game loop is used to refresh at the rate of 60 frames per second,
     * and to keep everything synchronized.
     */

    private boolean isRunning = false;
    public static int WIDTH = 640, HEIGHT = 480 ;
    public static final String TITLE = "Pac-Man";
    private Thread thread;
    public static Player me;
    public static Level level;
    public static SpriteSheet sheet;

    public Main_Game(){
        Dimension dim = new Dimension(Main_Game.WIDTH, Main_Game.HEIGHT);
        setMaximumSize(dim);
        setMinimumSize(dim);
        setPreferredSize(dim);
        addKeyListener(this);
        me = new Player(Main_Game.WIDTH/2, Main_Game.HEIGHT/2);
        String fileName = System.getProperty("user.dir") + "\\src\\map.png";
        //System.out.println(fileName);
        level = new Level(fileName);
        String path= System.getProperty("user.dir") + "\\src\\sprites.png";
        sheet = new SpriteSheet(path);
        new Texture();
    }

    public synchronized void start(){
        // if game already running return
        if(isRunning) return;
        isRunning = true;
        // passing the runnable obj of this class, Main_Game
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){
        if (!isRunning) return;
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick(){
        /**
         * Player's tick() is taking care of the keyboard inputs, movements
         * etc, collecting the coins and collision.
         */
        me.tick();
        level.tick();
    }

    private void render(){
        /**
         * The drawings first get stored in a buffer,
         * and on calling bs.show() it gets grawn on canvas
         */
        BufferStrategy bs = getBufferStrategy();
        if (bs == null){
            createBufferStrategy(4);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, Main_Game.WIDTH, Main_Game.HEIGHT);
        me.render(g);
        level.render(g);
        g.dispose();
        bs.show();
    }
    public void run(){
        /**
         * Game loop implementation.
         */
        // without this, you have to click on the frame to move
        requestFocus();
        int fps = 0;
        double timer = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double targetTick = 60.0;
        double delta = 0;
        //interval b/w ticks
        double ns = 1000000000/targetTick;
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;

            while(delta >= 1){
                tick();

                delta--;
            }
            render();
            fps++;
            if (System.currentTimeMillis() - timer>=1000){
                System.out.println(fps);
                fps = 0;
                timer += 1000;
            }
        }
        stop();
    }

    public static void main(String[] args) {
        Main_Game game = new Main_Game();
        JFrame frame = new JFrame();
        frame.setTitle(TITLE);
        frame.add(game);
        // keep the window size same
        frame.setResizable(false);
        frame.pack(); // to display the exact fixed size we need to add this as well
        // closes the program on closing the frame windows
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        // to display the frame
        frame.setVisible(true);
        game.start();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN) me.down = true;
        if(e.getKeyCode() == KeyEvent.VK_UP) me.up = true;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) me.right = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT) me.left = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN) me.down = false;
        if(e.getKeyCode() == KeyEvent.VK_UP) me.up = false;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) me.right = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT) me.left = false;
    }
}

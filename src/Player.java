import com.sun.tools.javac.Main;
import org.w3c.dom.css.Rect;

import java.awt.*;

public class Player extends Rectangle {

    public boolean left, right, up, down;
    private int speed = 4;
    public Player(int x, int y){
        setBounds(x, y, 32, 32);
    }

    public void tick(){
        /**
         * Check which has true value, and if can move then we move to that position.
         * Collecting coins along the way as we move.
         */
        if(right && canMove(x+speed, y))  x += speed;
        if(left && canMove(x-speed, y))   x -= speed;
        if(up && canMove(x, y-speed)) y -= speed;
        if(down && canMove(x, y+speed))   y += speed;

        Level l = Main_Game.level;

        for(int i=0; i<l.coins.size();i++){
            if(this.intersects(l.coins.get(i))){
                l.coins.remove(i);
                //break;
            }
        }

        if (l.coins.size()==0) {
            // restarting
            Main_Game.me = new Player(0, 0);
            String fileName = System.getProperty("user.dir") + "\\src\\map.png";
            Main_Game.level = new Level(fileName);
            return;
        }

        for(int i = 0;i< Main_Game.level.bots.size();i++)
        {
            Bots b = Main_Game.level.bots.get(i);
            if(b.intersects(this))
            {
                Main_Game.me = new Player(0,0);
                Main_Game.level = new Level(System.getProperty("user.dir") + "\\src\\map.png");
                return;

            }
        }
    }

    private boolean canMove(int x, int y){
        /**
         * Checking collision, by simply using rectangle class intersection method.
         * If teo rect objs are intersecting hence collision.
         */
        Rectangle bounds = new Rectangle(x, y, width, height);
        Level level = Main_Game.level;
        for(int i=0;i<level.tiles.length;i++){
            for(int j=0;j<level.tiles[0].length;j++){
                if(level.tiles[i][j] != null){
                    if(bounds.intersects(level.tiles[i][j]))
                        return false;
                }
            }
        }
        return true;
    }

    public void render(Graphics g){
        g.drawImage(Texture.player[0],x,y,width,height,null);
        //else   g.drawImage(Texture.player[imageIndex%2],x+32,y,-width,height,null);
        //when we do -width it flips but it become x-32
        //hence we add 32 to x to adjust it
    }

}

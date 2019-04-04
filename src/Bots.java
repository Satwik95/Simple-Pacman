import com.sun.tools.javac.Main;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.Random;

public class Bots extends Rectangle {

    private int random=0, smart=1,find_path=2;
    private int state = random;
    private int speed = 4;
    private int right = 0, left = 1, up=2, down = 3;
    private int dir = -1;
    private int lastDir = -1;
    private int time = 0;
    public Random randomGen;
    private int targetTime = 60*4;
    public Bots(int x, int y){
        randomGen = new Random();
        setBounds(x, y, 32, 32);
        dir = randomGen.nextInt(4);
    }

    public void tick(){
        /** There are three states random, smart and find path. When the state is random as set initially, we see if the
         * dir = right if it can move in that direction then continue moving
         * else random direction and so on for every direction.
         * we start with the state as smart... we start chasing the player in this state,
         * We compare the position of the bot and the player, and accordingly move
         * and set move to true. If the bot can't move.( move = false )
         * hence we need to now refer to the lastDir and and check if movement along.
         **/
        if(state == random) {
            if(dir==right){
                if(canMove(x+speed,y)) {
                    x+=speed;
                }else { dir = randomGen.nextInt(4);}
            }
            else if(dir==left) {
                if(canMove(x-speed,y)) {
                    x-=speed;
                }
                else { dir = randomGen.nextInt(4);}    //returns a random int value b/w 0 and the specified value
            }
            else if(dir==up) {
                if(canMove(x,y-speed)) {
                    y-=speed;
                }else { dir = randomGen.nextInt(4);}
            }
            else if(dir==down) {
                if(canMove(x,y+speed)) {
                    y+=speed;
                }else { dir = randomGen.nextInt(4);}
            }
        }
        else if(state == smart)
        {
            // follow the player
            boolean move = false;
            if(x<Main_Game.me.x){
                if(canMove(x+speed,y))
                {
                    x+=speed;
                    move = true;
                    lastDir = right;
                }}

            if(x>Main_Game.me.x){
                if(canMove(x-speed,y))
                {
                    x-=speed;
                    move = true;
                    lastDir = left;
                }
            }
            if(y<Main_Game.me.y){
                if(canMove(x,y+speed))
                {
                    y+=speed;
                    move = true;
                    lastDir = down;
                }
            }
            if(y>Main_Game.me.y){
                if(canMove(x,y-speed))
                {
                    y-=speed;
                    move = true;
                    lastDir = up;
                }}

            if(x == Main_Game.me.x && y == Main_Game.me.y) move = true;
            if(!move) {
                state = find_path;
            }
            time++;
            if(time == targetTime){
                state = random;
                time = 0; }
        }
        else if(state == find_path)
        {
            if(lastDir==right)
            {   if(y< Main_Game.me.y)
            {if(canMove(x,y+speed))
            { y+=speed;
                state = smart;}
            else if(canMove(x,y-speed))
                y-=speed;
                state = smart;}

            else if(canMove(x+speed,y))
            {
                x+=speed;
            }
            }
            else if(lastDir == left)
            {
                if(y< Main_Game.me.y)
                {
                    if(canMove(x,y+speed))
                    {y+=speed;
                        state = smart;}

                    else if(canMove(x,y-speed))
                        y-=speed;
                    state = smart;
                }

                if(canMove(x-speed,y))
                {
                    x-=speed;
                }
            }
            else if(lastDir == down)
            {
                if(x< Main_Game.me.x)
                {
                    if(canMove(x+speed,y)){
                        x+=speed;
                        state = smart;
                    }
                    else if(canMove(x-speed,y))
                        x-=speed;
                    state = smart;
                }
                else if(canMove(x,y+speed))
                {
                    y+=speed;
                }
            }
            else if(lastDir == up)
            {
                if(x< Main_Game.me.x)
                {
                    if(canMove(x+speed,y))
                    {
                        x+=speed;
                        state = smart;
                    }
                    else if(canMove(x-speed,y))
                        x-=speed;
                    state = smart;
                }
                if(canMove(x,y-speed))
                {
                    y-=speed;
                }
            }
            time++;
            if(time == targetTime) {
                state = random;
                time = 0;
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
        g.drawImage(Texture.ghost, x, y,width, height, null);
    }
}

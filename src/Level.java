import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {

    public int height;
    public int width;
    public List<Coins> coins;
    public List<Bots> bots;
    public Tile[][] tiles;
    public Level(String path){
        coins = new ArrayList<>();
        bots = new ArrayList<>();
        try {
            BufferedImage map = ImageIO.read(new FileInputStream(path));
            this.width = map.getWidth();
            this.height = map.getHeight();
            int[] pixels = new int[width*height];
            tiles = new Tile[width][height];
            map.getRGB(0, 0,width, height, pixels, 0, width);
            for(int i=0; i<width; i++){
                for(int j=0; j<height; j++){
                    int val = pixels[i+(j*width)];
                    if(val== 0xFF000000){
                        // these are the black tiles, where we can't go
                        tiles[i][j] = new Tile(i*32, j*32);
                    }
                    else if(val==0xFF0000FF){
                        //player
                        Main_Game.me.x = i*32;
                        Main_Game.me.y = j*32;
                    }
                    else if(val==0xFFFF0000){
                        //bots
                        bots.add(new Bots(i*32, j*32));
                    }
                    else{
                        coins.add(new Coins(i*32, j*32));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick(){
        for(int i=0; i< bots.size();i++){
            bots.get(i).tick();
        }
    }

    public void render(Graphics g){
        /**
         * Rendering/drawing the objects on the maze
         * i.e. tiles, bots and the coins
         *
         * The rendering methods in each of the classes of these objects,
         * simply csets the color and fills the rectangle object.
         */
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                if(tiles[i][j]!=null) tiles[i][j].render(g);
            }
        }
        for(int i=0; i< coins.size();i++){
            coins.get(i).render(g);
        }

        for(int i=0; i< bots.size();i++){
            bots.get(i).render(g);
        }
    }
}

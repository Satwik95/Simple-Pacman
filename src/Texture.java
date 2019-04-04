import java.awt.image.BufferedImage;

public class Texture {
    public static BufferedImage[] player;
    public static BufferedImage ghost;

    public Texture(){
        player = new BufferedImage[2];// set size
        player[0] = Main_Game.sheet.getSprite(0,0);
        player[1] = Main_Game.sheet.getSprite(20,0);
        ghost = Main_Game.sheet.getSprite(0,20);
    }


}
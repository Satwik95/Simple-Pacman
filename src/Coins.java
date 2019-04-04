import java.awt.*;

public class Coins extends Rectangle {
    public Coins(int x, int y){
        setBounds(x+10, y+10, 8, 8);
    }

    public void render(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(x, y,width, height);
    }
}

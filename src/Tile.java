import java.awt.*;

public class Tile extends Rectangle {
    public Tile(int x, int y){
        setBounds(x, y, 32, 32);
    }
    public void render(Graphics g){
        g.setColor(new Color(0, 19, 127));
        g.fillRect(x, y, width, height);
    }
}

package topdownshooter;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    
    public Wall (Point2D.Float pos) {
        int size = Arena.TILE_SIZE;
        BufferedImage sprite = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        sprite.getGraphics().setColor(Color.PINK);
        sprite.getGraphics().fillRect (0,0,50,50);
        super.GameObject(pos, sprite);
    }
}

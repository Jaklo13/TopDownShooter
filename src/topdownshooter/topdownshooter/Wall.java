package topdownshooter;

import java.awt.geom.Point2D;

public class Wall extends GameObject {
    
    public Wall (Point2D.Float pos) {
        super (pos, GameManager.GM.getSprite(GameManager.WALL_SPRITES, 0));
        int size = Arena.TILE_SIZE;
    }
}

package topdownshooter;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {
    public static final String[] DIRECTION_NAMES = new String[]{"UP","DOWN","LEFT","RIGHT"};
    //These corrispond with the tiles in Arena
    public static final Point[] spawnPoints = new Point[]{new Point (0,0), new Point (1,0)};
    private int pn = 0; //playerNumber
    private float speed = 5f;
    
    //construct a Player object with it`s number
    public Player (int pn, BufferedImage sprite) {
        this.pn = pn;
        Point2D.Float pos = (pn < spawnPoints.length)? new Point2D.Float(spawnPoints[pn].x * Arena.TILE_SIZE + Arena.TILE_SIZE / 2, spawnPoints[pn].y * Arena.TILE_SIZE + Arena.TILE_SIZE / 2) : new Point2D.Float (0,0);
        super.GameObject(pos, sprite);
    }
    
    public void Update (ArrayList<Integer> keys) {
        Move (keys);
        LookAtPoint (GameManager.GM.GetWindow().GetMousePos());
    }
    
    //This compares the PressedKeys from Window with the the Keys for the 
    //PlayerNumber to determine the Direction
    public void Move (ArrayList<Integer> keys) {
        boolean[] dir = new boolean[4];
        for (int i = 0; i < dir.length; i++) {
            dir[i] = (keys.contains(Key.valueOf(DIRECTION_NAMES[i] + pn).GetKeyCode()));
        }
        boolean vertical = (dir[0] != dir[1]), horizontal = (dir[2] != dir[3]), diagonal = (vertical && horizontal);
        if (diagonal) {
            float localSpeed = (float)Math.sqrt(Math.pow(speed,2) / 2);
            Move ((dir[2])? -localSpeed : localSpeed, (dir[0])? -localSpeed : localSpeed);
        } else if (vertical) {
            Move (0,(dir[0])? -speed : speed);
        } else if (horizontal) {
            Move ((dir[2])? -speed : speed,0);
        }
    }
    
    public void Shoot () {
        
    }
}

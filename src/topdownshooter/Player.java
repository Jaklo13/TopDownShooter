package topdownshooter;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {
    public static final String[] DIRECTION_NAMES = new String[]{"UP","DOWN","LEFT","RIGHT"};
    private int pn = 0; //playerNumber
    private float speed = 5f;
    
    //construct a Player object with it`s number
    public Player (int pn, BufferedImage sprite) {
        super (GameManager.GM.GetArena().GetSpawnPoint(pn), sprite);
        this.pn = pn;
        bounds = new Rectangle2D.Float (bounds.x,bounds.y,bounds.width, bounds.width);
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

package topdownshooter;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {
    public static final String[] DIRECTION_NAMES = new String[]{"UP","DOWN","LEFT","RIGHT"};
    private int pn = 0; //playerNumber
    protected float speed = 5f;
    
    //construct a Player object with it`s number
    public Player (int pn, BufferedImage sprite) {
        super (GameManager.GM.getArena().GetSpawnPoint(pn), sprite);
        this.pn = pn;
        bounds = new Rectangle2D.Float (bounds.x,bounds.y,bounds.width, bounds.width);
    }
    
    public void Update (/*ArrayList<Integer> keys*/) {
        move(GameManager.GM.getWindow().GetKp());
        LookAtPoint (GameManager.GM.getWindow().getMousePos());
    }
    
    //This compares the PressedKeys from Window with the the Keys for the 
    //PlayerNumber to determine the Direction
    public void move(ArrayList<Integer> keys) {
        boolean[] dir = new boolean[4];
        for (int i = 0; i < dir.length; i++) {
            dir[i] = (keys.contains(Key.valueOf(DIRECTION_NAMES[i] + pn).GetKeyCode()));
        }

        float dx = 0;
        float dy = 0;

        double mySpeed = speed;

        if(dir[0]) dy -= 1;
        if(dir[1]) dy += 1;
        if(dir[2]) dx -= 1;
        if(dir[3]) dx += 1;

        if(Math.abs(dx) == Math.abs(dy)) mySpeed /= Math.sqrt(2);
        dx *= mySpeed;
        dy *= mySpeed;

        Move(dx, dy);

       /* boolean vertical = (dir[0] != dir[1]), horizontal = (dir[2] != dir[3]), diagonal = (vertical && horizontal);
        if (diagonal) {
            float localSpeed = (float)Math.sqrt(Math.pow(speed,2) / 2);
            move ((dir[2])? -localSpeed : localSpeed, (dir[0])? -localSpeed : localSpeed);
        } else if (vertical) {
            move (0,(dir[0])? -speed : speed);
        } else if (horizontal) {
            move ((dir[2])? -speed : speed,0);*/

    }

    public void move(float x, float y){
        super.Move(x, y);
    }
    
    public void Shoot () {
        
    }
}

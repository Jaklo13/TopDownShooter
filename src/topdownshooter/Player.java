package topdownshooter;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

public class Player extends GameObject {
    public static final String[] DIRECTION_NAMES = new String[]{"UP","DOWN","LEFT","RIGHT"};
    private int pn; //playerNumber
    protected float speed = 5f;
    
    //construct a Player object with it`s number
    public Player (int pn) {
        super (GameManager.GM.getArena().getSpawnPoint(pn), GameManager.GM.getSprite(GameManager.PLAYER_SPRITES,pn));
        this.pn = pn;
        bounds = new Rectangle2D.Float (bounds.x,bounds.y,bounds.width, bounds.width);
    }
    
    public void update () {
        move(GameManager.GM.getKp());
        lookAtPoint(GameManager.GM.getWindow().getMousePos());
    }

    public void lookAtPoint(Point2D.Float p){
        Point2D.Float rotCenter = new Point2D.Float (bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);    //Setting the center, around wich the Object is rotated
        rotation = (float)((Math.atan2(rotCenter.getX() - p.getX(), rotCenter.getY() - p.getY()))) * -1;
    }
    
    //This compares the PressedKeys from Window with the the Keys for the 
    //PlayerNumber to determine the Direction
    public void move(HashSet<Integer> keys) {
        boolean[] dir = new boolean[4];
        for (int i = 0; i < dir.length; i++) {
            dir[i] = (keys.contains(Key.valueOf(DIRECTION_NAMES[i] + pn).getKeyCode()));
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

        super.move(dx, dy);
    }

    public void move(float x, float y){
        super.move(x, y);
    }
    
    public void Shoot () {
        
    }
}

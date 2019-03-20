package topdownshooter;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Item extends GameObject{
    public static final int DMG_UP = 0,SHOT_SPEED_UP = 1,FIRE_RATE_UP = 2,HEAL = 3,MAX_COOLDOWN = 100;
    public static final Color[] TYPE_COLOR = new Color[]{Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN};
    public static final float[] DEFAULT_VALUES = new float[]{5f,5f,1f,30f};
    private int type;   //0-dmg, 1-shot speed, 2-fire rate, 3-heal
    private boolean active = true;
    private int coolDown = 0;
    
    public Item(Point2D.Float pos, int type) {   
        super(pos, GameManager.colorImage (GameManager.GM.getSprite(GameManager.ITEM_SPRITES, 0),TYPE_COLOR[type]));
        this.type = type;
    }
    
    //Items will never disappear. This update function will reactivate them, after they were collected
    public void update () {
        if (!active) {
            if (coolDown <= 0)
                setActive (true);
            else
                coolDown--;
        }
    }
    
    public void collected (GameObject go) {
        if (go instanceof Player) {
            switch (type) {
                case 0:
                    ((Player)go).buffDmg (DEFAULT_VALUES[0]);
                    break;
                case 1:
                    ((Player)go).buffShotSpeed (DEFAULT_VALUES[1]);
                    break;
                case 2:
                    ((Player)go).buffFireRate (DEFAULT_VALUES[2]);
                    break;
                case 3:
                    ((Player)go).heal (DEFAULT_VALUES[3]);
                    break;
            }
        } else {
            System.out.println ("Error, not a player");
        }
        setActive (false);
        resetCoolDown ();
    }
    
    public void setActive (boolean a) {
        active = a;
    }
    
    public boolean isActive () {
        return active;
    }
    
    public void resetCoolDown () {
        coolDown = MAX_COOLDOWN;
    }
}

package topdownshooter;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

public class Player extends GameObject {
    public static final String[] DIRECTION_NAMES = new String[]{"UP","DOWN","LEFT","RIGHT"};
    public static final int MAX_HEALTH = 100, DEFAULT_FIRE_RATE = 10, DEFAULT_DMG = 10, DEFAULT_SHOT_SPEED = 50;
    public static final Color[] PLAYER_COLORS = new Color[]{new Color (52,120,255),new Color (22,255,60),new Color (255,59,59),new Color (255,250,52)}; //Colors for the first 4 players: Blue,green,red,yellow
    private int pn; //playerNumber
    protected float speed = 5f;
    private int health = MAX_HEALTH, fireRate = DEFAULT_FIRE_RATE, dmg = DEFAULT_DMG, shotSpeed = DEFAULT_SHOT_SPEED;
    private int nextShot = 0;
    private boolean shooting = false;
    private BufferedImage bullet;
    
    //construct a Player object with it`s number
    public Player (int pn) {
        super (GameManager.GM.getArena().getSpawnPoint(pn), GameManager.colorImage(GameManager.GM.getSprite(GameManager.PLAYER_SPRITES,0),PLAYER_COLORS[pn]));
        this.pn = pn;
        bounds = new Rectangle2D.Float (bounds.x,bounds.y,bounds.width, bounds.width);
        bullet = GameManager.colorImage(GameManager.GM.getSprite(GameManager.BULLET_SPRITES, 0), PLAYER_COLORS[pn]);
    }
    
    public void update () {
        move(GameManager.GM.getKp());
        lookAtPoint(GameManager.GM.getWindow().getMousePos());
        if (nextShot > 0)
            nextShot--;
        shoot ();
        checkForItems ();
    }

    public void lookAtPoint(Point2D.Float p){
        Point2D.Float rotCenter = getPos (GameObject.CENTER);                   //Setting the center, around wich the Object is rotated
        rotation = (float)((Math.atan2(rotCenter.getY() - p.getY(),rotCenter.getX() - p.getX())));
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
    
    public void shoot () {
        if (shooting && nextShot == 0) {
            GameManager.GM.getShotHandler().addShot(getPos(0), shotSpeed, getRotation(), this, dmg);
            nextShot = fireRate;
        }
    }
    
    public void setShooting (boolean s) {
        shooting = s;
    }
    
    public void takeDamage (int dmg) {
        health -= dmg;
        if (health <= 0) {
            die ();
        }
    }
    
    public void die () {
        GameManager.GM.remove (this);
    }
    
    public void checkForItems () {
        ArrayList<GameObject> items = GameManager.GM.intersectsAny(bounds, Item.class);
        for (GameObject i : items) {
            ((Item)i).collected(this);
        }
    }
    
    public void buffDmg (float d) {
        dmg += d;
    }
    
    public void buffShotSpeed (float s) {
        shotSpeed += s;
    }
    
    public void buffFireRate (float f) {
        fireRate -= f;
        if (fireRate < 1)
            fireRate = 1;
    }
    
    public void heal (float h) {
        health += h;
        if (health > MAX_HEALTH)
            health = MAX_HEALTH;
    }

    public int getPn() {
        return pn;
    }

    public BufferedImage getBullet() {
        return bullet;
    }

    public int getHealth() {
        return health;
    }
    
    public float getHealthPercentage() {
        return (float)health / MAX_HEALTH;
    }
}

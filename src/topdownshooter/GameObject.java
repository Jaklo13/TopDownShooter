package topdownshooter;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class GameObject  {
    public static final byte TOP = 0b1, BOTTOM = 0b10, LEFT = 0b100, RIGHT = 0b1000;
    protected Rectangle2D.Float bounds; //This contains both the size of the hitBox and the position
    protected BufferedImage sprite;
    protected float rotation = 0;
    
    public GameObject (Point2D.Float pos, BufferedImage sprite) {
        this.sprite = sprite;
        this.bounds = new Rectangle2D.Float (pos.x, pos.y, sprite.getWidth(), sprite.getHeight());
        GameManager.GM.addGameObject(this);
    }
    
    //Moves the object by a certain amount and checks for collision
    //This requires the Object to be moved less than the size of it's own hitbox
    protected void move (float x, float y) {
        Point2D.Float newPos = new Point2D.Float (bounds.x + x, bounds.y + y);
        ArrayList<GameObject> iObj;
        Rectangle2D.Float inter;
        int relevantSides = determineRelevantSides (x,y),adjustAxis;
        float newValue;

        /*Encountered a bug where players can disappear if they walk against each other*/

        int i,maxAttempts = 100;                                //I have encountered a rare bug where, if both Players walk diagonal into each other, sometimes this loop never breaks.
        for (i = 0; i < maxAttempts; i++) {                     //I might look into this later, but this makes the code safer than a while(true) loop would anyway
            inter = new Rectangle2D.Float(newPos.x,newPos.y,bounds.width,bounds.height);  //Sets the Rectangle, where it would be
            iObj = GameManager.GM.intersectsAny (inter);        //Find all other Objects
            removeThis (iObj);                                  //Removes itself from the list
            if (iObj.isEmpty())                                 //If the list is empty, it can move
                break;
            for (GameObject go : iObj) {
                inter = new Rectangle2D.Float(newPos.x,newPos.y,bounds.width,bounds.height);
                Rectangle2D.intersect (inter, go.bounds,inter);    //Finds the part that intersects and saves it into inter
                adjustAxis = chooseAxisToAdjust (relevantSides, (newPos.x - bounds.x), (newPos.y - bounds.y), go, inter);   //1 = horizontal, -1 = vertical
                if (adjustAxis == 1) {
                    newValue = (Math.abs(x) / x) * (Math.abs(x) - inter.width);
                    newPos = new Point2D.Float (bounds.x + newValue, newPos.y);
                    break;
                } else if (adjustAxis == -1){ 
                    newValue = (Math.abs(y) / y) * (Math.abs(y) - inter.height);
                    newPos = new Point2D.Float (newPos.x, bounds.y + newValue);
                    break;
                }
            }
        }
        if (i >= maxAttempts)
            System.out.println ("Could not move correctly, max attempts reached");
        this.moveTo (newPos);
    }
    
    //Takes an array of GameObjects and removes itself from it
    public void removeThis (ArrayList<GameObject> o) {
        Iterator<GameObject> iter = o.iterator();
        while (iter.hasNext()) {
            GameObject go = iter.next();
            if (go.equals(this)) {
                iter.remove();
            }
        }
    }
    
    public int determineRelevantSides (float x, float y) {
        int relevantSides = 0;                          //Determine the sides of the hitbox it could move into.
        if (y != 0)                                     //e.g. if this moves straight up, it can only hit the bottom of an Object
            relevantSides |= (y > 0)? TOP : BOTTOM;    
        if (x != 0)
            relevantSides |= (x > 0)? LEFT : RIGHT;
        return relevantSides;
    }
    
    public int chooseAxisToAdjust (int sides, float x, float y, GameObject go, Rectangle2D.Float inter) {
        Point2D.Float p2 = go.getPos();
        Rectangle2D.Float r = go.getBounds();
        
        sides = sidesOnGameObjectSides (sides, r, inter);       //Removes all sides, that aren't on the side os the GameObject
        if (Integer.bitCount(sides) == 1)                       //If there is only one side, it is returned
            return isHorizontal(sides)?1:-1;
        return (inter.width == Math.abs(x) && inter.height == Math.abs(y))?0:((inter.height > Math.abs(y))?1:-1);
    }
    
    public boolean isHorizontal (int side) {
        if (Integer.bitCount(side) != 1) {
            System.out.println ("Error, more than one side");
            return false;
        }
        boolean hor = (side == LEFT || side == RIGHT);
        return hor;
    }
    
    //Checks, of all sides, which are on the the side of an Object and removes the ones that aren't
    public int sidesOnGameObjectSides (int sides, Rectangle2D.Float go, Rectangle2D.Float inter) {
        if ((sides & TOP) == TOP) {
            if (go.y < inter.y)
                sides &= ~TOP;
        }
        if ((sides & BOTTOM) == BOTTOM) {
            if (go.y + go.height > inter.y + inter.height)
                sides &= ~BOTTOM;
        }
        if ((sides & LEFT) == LEFT) {
            if (go.x < inter.x)
                sides &= ~LEFT;
        }
        if ((sides & RIGHT) == RIGHT) {
            if (go.x + go.width > inter.x + inter.width)
                sides &= ~RIGHT;
        }
        return sides;
    }
    
    //Teleports the object to a certain position
    protected void moveTo (Point2D.Float pos) {
        bounds.setFrame(pos.x, pos.y, bounds.width, bounds.height);
    }

    public Rectangle2D.Float getBounds () {
        return bounds;
    }
    
    public Point2D.Float getHitBox  () {
        return new Point2D.Float (bounds.width, bounds.height);
    }
    
    public Point2D.Float getPos () {
        return new Point2D.Float (bounds.x, bounds.y);
    }

    public BufferedImage getSprite () {
        return sprite;
    }

    public float getRotation () {
        return rotation;
    }
}

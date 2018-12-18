package topdownshooter;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class GameObject  {
    public static final int TOP = 1, BOTTOM = 2, LEFT = 4, RIGHT = 8;
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
    protected void Move (float x, float y) {
        Point2D.Float newPos = new Point2D.Float (bounds.x + x, bounds.y + y);
        ArrayList<GameObject> iObj;
        Rectangle2D.Float inter;
        int relevantSides = DetermineRelevantSides (x,y),adjustAxis;
        float newValue;
        
        int i,maxAttempts = 100;                                //I have encountered a rare bug where, if both Players walk diagonal into each other, sometimes this loop never breaks.
        for (i = 0; i < maxAttempts; i++) {                     //I might look into this later, but this makes the code safer than a while(true) loop would anyway
            inter = new Rectangle2D.Float(newPos.x,newPos.y,bounds.width,bounds.height);  //Sets the Rectangle, where it would be
            iObj = GameManager.GM.IntersectsAny (inter);        //Find all other Objects
            RemoveThis (iObj);                                  //Removes itself from the list
            if (iObj.isEmpty())                                 //If the list is empty, it can move
                break;
            for (GameObject go : iObj) {
                inter = new Rectangle2D.Float(newPos.x,newPos.y,bounds.width,bounds.height);
                Rectangle2D.intersect (inter, go.bounds,inter);    //Finds the part that intersects and saves it into inter
                adjustAxis = ChooseAxisToAdjust (relevantSides, (newPos.x - bounds.x), (newPos.y - bounds.y), go, inter);   //1 = horizontal, -1 = vertical
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
        this.MoveTo (newPos);
    }
    
    //Takes an array of GameObjects and removes itself from it
    public void RemoveThis (ArrayList<GameObject> o) {
        Iterator<GameObject> iter = o.iterator();
        while (iter.hasNext()) {
            GameObject go = iter.next();
            if (go.equals(this)) {
                iter.remove();
            }
        }
    }
    
    public int DetermineRelevantSides (float x, float y) {
        int relevantSides = 0;                          //Determine the sides of the hitbox it could move into.
        if (y != 0)                                     //e.g. if this moves straight up, it can only hit the bottom of an Object
            relevantSides |= (y > 0)? TOP : BOTTOM;    
        if (x != 0)
            relevantSides |= (x > 0)? LEFT : RIGHT;
        return relevantSides;
    }
    
    public int ChooseAxisToAdjust (int sides, float x, float y, GameObject go, Rectangle2D.Float inter) {
        Point2D.Float p2 = go.getPos();
        Rectangle2D.Float r = go.getBounds();
        
        sides = SidesOnGameObjectSides (sides, r, inter);       //Removes all sides, that aren't on the side os the GameObject
        if (Integer.bitCount(sides) == 1)                       //If there is only one side, it is returned
            return IsHorizontal(sides)?1:-1;
        return (inter.width == Math.abs(x) && inter.height == Math.abs(y))?0:((inter.height > Math.abs(y))?1:-1);
    }
    
    public boolean IsHorizontal (int side) {
        if (Integer.bitCount(side) != 1) {
            System.out.println ("Error, more than one side");
            return false;
        }
        boolean hor = (side == LEFT || side == RIGHT);
        return hor;
    }
    
    //Checks, of all sides, which are on the the side of an Object and removes the ones that aren't
    public int SidesOnGameObjectSides (int sides, Rectangle2D.Float go, Rectangle2D.Float inter) {
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
    protected void MoveTo (Point2D.Float pos) {
        bounds.setFrame(pos.x, pos.y, bounds.width, bounds.height);
    }
    
    public void LookAtPoint (Point p) {
        Point2D.Float rotCenter = new Point2D.Float (bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);    //Setting the center, around wich the Object is rotated
        rotation = (float)((Math.atan2(rotCenter.getX() - p.getX(), rotCenter.getY() - p.getY()))) * -1;
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

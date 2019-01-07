package topdownshooter;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
        Point2D.Float p1 = getPos (GameObject.CENTER), p2 = GameManager.GM.getShootingDistance (rotation);  //Takes the position and a point offscreen, that has the correct angle.
        p2 = new Point2D.Float (p2.x + p1.x, p2.y + p1.y);                                                  //Sets the second point relative to the first 
        Line2D.Float line = new Line2D.Float (p1,p2);                                                       //Creates a line between these points
        Window.lines.add (line);                                                                            //For debugging only, until a proper method for displaying shots is implemented
        
        ArrayList<GameObject> iObj;                                                                         //Similar to the collision algorythm, an array is created, to store all Objects,
        iObj = GameManager.GM.intersectsAny (line);                                                         //that intersect with the line
        removeThis (iObj);
        for (GameObject go : iObj) {
            if (go instanceof Player) {                                                                     //If no player Object is intersected, there is no need, to check if it is hit
                GameObject firstObject = findFirstHit (line,iObj);
                if (firstObject instanceof Player) {
                    System.out.println ("HIT");
                } else {
                    System.out.println ("MISS");
                }
                return;                                                                                     //If at least one Player Object is hit, the first Object, hit by the shot is determined
            }
        }
    }
    
    public GameObject findFirstHit (Line2D.Float l, ArrayList<GameObject> iObj) {
        int relevantSides = determineRelevantSides (l.x2 - l.x1, l.y2 - l.y1);
        ArrayList<Line2D.Float> lines = new ArrayList<>();
        ArrayList<Point2D.Float> points = new ArrayList<>();
        
        for (GameObject go : iObj) {                                                                        //Finds all lines, on the outside of all GameObjects, that are hit
            if ((relevantSides & TOP) == TOP) {
                lines.add (new Line2D.Float (go.getPos(TOP | LEFT),go.getPos(TOP | RIGHT)));
            } else if ((relevantSides & BOTTOM) == BOTTOM) {
                lines.add (new Line2D.Float (go.getPos(BOTTOM | LEFT),go.getPos(BOTTOM | RIGHT)));
            }
            if ((relevantSides & LEFT) == LEFT) {
                lines.add (new Line2D.Float (go.getPos(LEFT | TOP),go.getPos(LEFT | BOTTOM)));
            } else if ((relevantSides & RIGHT) == RIGHT) {
                lines.add (new Line2D.Float (go.getPos(RIGHT | TOP),go.getPos(RIGHT | BOTTOM)));
            }
        }
                                                                                                            //Finds all intersection points
        if ((l.y1 != l.y2) && (l.x1 != l.x2)) {     //There are 2 reasons for this seperation. 1: if x1 == x2, devision by 0. 2: finding the point is much easier, if this is true
            float m = (float)((l.y2 - l.y1) / (l.x2 - l.x1)), n = l.y1 - (m * l.x1);                        //m and n of f(x)=m*x+n

            Iterator<Line2D.Float> iter = lines.iterator();
            while (iter.hasNext()) {
                Line2D.Float line = iter.next();
                if (line.intersectsLine(l)) {
                    float y = line.y1, x;                                       //Calculates x and y of the intersection point and adds it
                    x = (y - n) / m;
                    points.add (new Point2D.Float (x,y));                       //So far, these are the lines, that could intersect, if they don't, they are stopped here
                    
                    iter.next();                                                //Calculates x and y of the intersection point and adds it
                    iter.remove();
                } else {
                    iter.remove ();
                    line = iter.next();
                    
                    float y, x = line.x1;
                    y = x * m + n;
                    points.add (new Point2D.Float (x,y));
                }
            }
        } else {
            for (Line2D.Float line : lines) {
                if (isHorizontal (l)) {
                    points.add (new Point2D.Float (line.x1, l.y1));
                } else {
                    points.add (new Point2D.Float (l.x1, line.y1));
                }
            }
        }
        Window.points.addAll(points);   //For debugging only
        
        Point2D.Float p1 = new Point2D.Float (l.x1,l.y1), p2 = points.get(0);
        float dist = (float)p1.distance(p2);
        for (Point2D.Float p : points) {                                                                    //Finds the point, that is closest to the player
            float d = (float)p1.distance(p);
            if (d < dist) {
                dist = d;
                p2 = p;
            }
        }
        
        return iObj.get (points.indexOf(p2));
    }
    
    //This should only get lines that are either horizontal or vertical
    public boolean isHorizontal (Line2D.Float line) {
        return (line.y1 == line.y2);
    }
}

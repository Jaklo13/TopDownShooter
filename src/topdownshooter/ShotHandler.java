package topdownshooter;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import static topdownshooter.GameObject.BOTTOM;
import static topdownshooter.GameObject.LEFT;
import static topdownshooter.GameObject.RIGHT;
import static topdownshooter.GameObject.TOP;

public class ShotHandler {
    private ArrayList<Shot> shots = new ArrayList<>();
    
    public ShotHandler () {
    
    }
    
    public void update () {
        updateShots ();
    }
    
    public void updateShots () {
        for (int i = 0; i < shots.size(); i++) {
            shots.get(i).update();
            checkShot (shots.get(i));
        }
    }
    
    public void checkShot (Shot shot) {
        if (shot.isMoving()) {
            if (!checkOutOfBounds (shot)) {
                checkHits (shot);
            } else {
                shot.stopMoving (new Point2D.Float (shot.getHitLine().x2,shot.getHitLine().y2));
            }
            
        } else if (shot.tooShort()) {
            shots.remove (shot);
        }
    }
    
    public boolean checkOutOfBounds (Shot shot) {  //should return true when out of bounds, to be implemented later
        return false;                                   //this is just a failsafe, in case a shot goes out of bounds
    }
    
    public void addShot (Point2D.Float p1, float speed, float dir, GameObject o) {
        Point2D.Float p2 = new Point2D.Float (p1.x + speed * (float)Math.cos(dir) * -1, p1.y + speed * (float)Math.sin(dir) * -1);
        Line2D.Float l = new Line2D.Float (p1,p2);
        Shot s = new Shot (this,l,o);
        shots.add (s);
        checkHits (s);
    }
    
    public void checkHits (Shot shot) {   
        ArrayList<GameObject> iObj;                                                                         //Similar to the collision algorythm, an array is created, to store all Objects,
        iObj = GameManager.GM.intersectsAny (shot.getHitLine());                                            //that intersect with the line
        shot.getShooter().removeThis (iObj);                                                                //removes the shooter from the objects that are hit
        if (iObj.isEmpty()) {
            return;
        }                                                                //If no player Object is intersected, there is no need, to check if it is hit
        GameObject firstObject = findFirstHit (shot,iObj);
        if (firstObject instanceof Player) {
            ((Player) firstObject).takeDamage(shot.getDmg());
        } else {
        }
    }
    
    public GameObject findFirstHit (Shot shot, ArrayList<GameObject> iObj) {
        Line2D.Float l = shot.getHitLine();
        int relevantSides = GameObject.determineRelevantSides (l.x2 - l.x1, l.y2 - l.y1);
        ArrayList<Line2D.Float> lines = findLines(iObj, relevantSides);
        ArrayList<Point2D.Float> points = findPoints (lines, l);
        Point2D.Float p = findClosestPoint (points, l);
        
        shot.stopMoving(p);
        return iObj.get (points.indexOf(p));
    }
    
    public ArrayList<Line2D.Float> findLines (ArrayList<GameObject> iObj, int relevantSides) {      //Finds all lines, on the outside of all GameObjects, that are hit
        ArrayList<Line2D.Float> lines = new ArrayList<>();
        for (GameObject go : iObj) {                                                                        
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
        return lines;
    }
    
    public ArrayList<Point2D.Float> findPoints (ArrayList<Line2D.Float> lines, Line2D.Float l) {    //Finds all intersection points
        ArrayList<Point2D.Float> points = new ArrayList<>();

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
        return points;
    }
    
    public Point2D.Float findClosestPoint (ArrayList<Point2D.Float> points, Line2D.Float l) {
        Point2D.Float p1 = new Point2D.Float (l.x1,l.y1), p2 = points.get(0);
        float dist = (float)p1.distance(p2);
        for (Point2D.Float p : points) {                                                                    //Finds the point, that is closest to the player
            float d = (float)p1.distance(p);
            if (d < dist) {
                dist = d;
                p2 = p;
            }
        }
        return p2;
    }
        
    //This should only recieve lines that are either horizontal or vertical
    public boolean isHorizontal (Line2D.Float line) {
        return (line.y1 == line.y2);
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }
    
    public ArrayList<Line2D.Float> getTraces() {
        ArrayList<Line2D.Float> traces = new ArrayList<>();
        for (Shot s : shots)
            traces.add(s.getTrace());
        return traces;
    }
}

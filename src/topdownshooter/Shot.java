package topdownshooter;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Shot {
    public static final float MAX_TRACE_LENGTH = 5;     //times the hitLine length
    public static final float TRACE_SPEED = 0.8f;       //how fast the trail is compared to the shot
    private ShotHandler sh;
    private GameObject shooter;
    private BufferedImage sprite;
    private Line2D.Float hitLine, trace;
    private Point2D.Float endPoint;
    private boolean isMoving = true;
    private int dmg = 10;

    
    public Shot (ShotHandler sh, Line2D.Float hitLine, GameObject shooter) {
        this.sh = sh;
        this.hitLine = hitLine;
        this.shooter = shooter;
        this.trace = new Line2D.Float (hitLine.x1,hitLine.y1,hitLine.x2,hitLine.y2);
        if (shooter instanceof Player) {
            int pn = ((Player)shooter).getPn () + 1;
            this.sprite = ((Player)shooter).getBullet ();
        } else {
            this.sprite = GameManager.GM.getSprite(GameManager.BULLET_SPRITES, 0);
        }
        
    }
    
    public void update () {
        move ();
    }
    
    public void move () {
        float d1 = hitLine.x2 - hitLine.x1, d2 = hitLine.y2 - hitLine.y1; 
        if (isMoving) {
            hitLine.x1 += d1;
            hitLine.x2 += d1;
            hitLine.y1 += d2;
            hitLine.y2 += d2;
        }
        
        if (isMoving) {
            trace.x2 = hitLine.x2;
            trace.y2 = hitLine.y2;
        } else if (endPoint != null) {
            trace.x2 = endPoint.x;
            trace.y2 = endPoint.y;
        } else
            System.out.println ("Error, no endPoint");
        
        if (!hasReachedMaxTrailLength()) {
            d1 *= TRACE_SPEED;
            d2 *= TRACE_SPEED;
        }
        trace.x1 += d1;
        trace.y1 += d2;
        
    }
    
    public boolean hasReachedMaxTrailLength () {
        return (Math.hypot(hitLine.x2 - hitLine.x1, hitLine.y2 - hitLine.y1) * MAX_TRACE_LENGTH < Math.hypot(trace.x2 - trace.x1, trace.y2 - trace.y1));
    }
    
    public boolean tooShort () {
        return (Math.hypot(hitLine.x1 - hitLine.x2, hitLine.y1 - hitLine.y2) > Math.hypot(trace.x1 - trace.x2, trace.y1 - trace.y2));
    }
    
    public boolean isMoving () {
        return isMoving;
    }
    
    public void stopMoving (Point2D.Float p) {
        endPoint = p;
        isMoving = false;
    }

    public Line2D.Float getHitLine() {
        return hitLine;
    }

    public Line2D.Float getTrace() {
        return trace;
    }

    public GameObject getShooter() {
        return shooter;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getDmg() {
        return dmg;
    }
}

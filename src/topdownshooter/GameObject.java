package topdownshooter;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class GameObject  {
    protected Rectangle hitBox;
    protected Point2D.Float pos;
    protected BufferedImage sprite;
    protected float rotation = 0;
    
//    public void GameObject (Rectangle hitBox, Point2D.Float pos, BufferedImage sprite) {
//        this.hitBox = hitBox;
//        this.pos = pos;
//        this.sprite = sprite;
//    }
    
    public void GameObject (Point2D.Float pos, BufferedImage sprite) {
        this.pos = pos;
        this.sprite = sprite;
        this.hitBox = new Rectangle (sprite.getWidth(), sprite.getHeight());
        GameManager.GM.AddGameObject(this);
    }
    
    protected void Move (float x, float y) {
        this.pos = new Point2D.Float ((float)pos.getX() + x, (float)pos.getY() + y);
    }
    
    protected void MoveTo (Point2D.Float pos) {
        this.pos = pos;
    }
    
    public void LookAtPoint (Point p) {
        rotation = (float)((Math.atan2(pos.getX() - p.getX(), pos.getY() - p.getY()))) * -1;
//        rotationDegree = (float)((rotation / Math.PI) * 180);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Point2D.Float getPos() {
        return pos;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public float getRotation() {
        return rotation;
    }
}

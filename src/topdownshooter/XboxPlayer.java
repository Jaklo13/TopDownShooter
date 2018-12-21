package topdownshooter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class XboxPlayer extends Player {
//
    public XboxPlayer (int pn) {
        super (pn);
    }
//
//    @Override
//    public void Update(){
//        moveXBOX(GameManager.GM.getWindow().getXboxDirectionsLeft());
//        LookAtDirection(GameManager.GM.getWindow().getXboxDirectionsRight());
//    }
//
//    private void LookAtDirection(float[] directions){
//
//        Point2D.Float myPoint = getPos();
//
//            myPoint.x += directions[0] * 100f;
//            myPoint.y += directions[1] * -100f;
//
//        lookAtPoint(myPoint);
//    }
//
//    private void moveXBOX(float[] directions){
//
//        //need to make sure the value is bigger than a small treshold, else the player is moving without the controller being touched
//        if(Math.abs(directions[0]) > 0.05f && Math.abs(directions[1]) > 0.05f)
//        {
//            move(directions[0] * speed, directions[1] * -speed);
//        }
//        //System.out.println(directions[0] + " " + directions[1]);
//    }

}

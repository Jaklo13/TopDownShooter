package topdownshooter;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

public class XboxPlayer extends Player {

    public XboxPlayer (int pn, BufferedImage sprite) {
        super (pn, sprite);
    }

    @Override
    public void Update(){
        moveXBOX(GameManager.GM.getWindow().getXboxDirections());
    }

    private void LookAtDirection(){

    }

    private void moveXBOX(float[] directions){

        //need to make sure the value is bigger than a small treshold, else the player is moving without the controller being touched
        if(Math.abs(directions[0]) > 0.05f && Math.abs(directions[1]) > 0.05f)
        {
            move(directions[0] * speed, directions[1] * -speed);
        }
        System.out.println(directions[0] + " " + directions[1]);
    }

}

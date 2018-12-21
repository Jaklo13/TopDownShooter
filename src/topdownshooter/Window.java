package topdownshooter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;


public class Window {
    public static final int BORDER_SIZE = 25, TOP_BORDER_SIZE = 20;
    public static final Point UPPER_LEFT_CORNER = new Point (BORDER_SIZE, BORDER_SIZE + TOP_BORDER_SIZE);
    private JFrame jFrame;
    private BufferedImage backgroundImage;
    private int width, height;
    //index 0 => x axis, index 1 => y axis


//    private ControllerManager cmanager;
//    private ControllerState cstate;

    public Window (int width, int height) {
        this.width = width;
        this.height = height;
        SwingUtilities.invokeLater(new Runnable () {
            @Override
            public void run() {
                createWindow ();
            }
        });
//        
//        cmanager = new ControllerManager();
//        cmanager.initSDLGamepad();
    }
    
    public void createWindow () {
        jFrame = new JFrame ();
        jFrame.setTitle ("Game");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.add (new Panel(this));
        jFrame.pack();
        jFrame.setLocationRelativeTo (null);
    }
    
    public BufferedImage paintBackgroundImage (Arena a) {
        BufferedImage image = new BufferedImage (width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics g = image.getGraphics();
        g.setColor (new Color (000,200,100));           //Here we will ad a Grass Image later
        g.fillRect(0, 0, width, height);
        ArrayList<Wall> walls;
        walls = a.getWalls();
        for (Wall w : walls) {
            Rectangle2D.Float b = w.getBounds();
            g.drawImage(GameManager.GM.getSprite(GameManager.WALL_SPRITES, 0), (int)b.x, (int)b.y, null);
        }
        return image;
    }

//    public float[] getXboxDirectionsLeft(){
//        cstate = cmanager.getState(0);
//        return new float[]{cstate.leftStickX, cstate.leftStickY};
//    }

//    public float[] getXboxDirectionsRight(){ //doesnt get a new state because its always called directly after getXboxDirectionsLeft()
//        return new float[]{cstate.rightStickX, cstate.rightStickY}; // => avoids double reading
//    }
    
    public Point2D.Float getDimensions () {
        return new Point2D.Float (width, height);
    }
    
    public Point2D.Float getMousePos() {
        try {
            Point mp = MouseInfo.getPointerInfo().getLocation(), fp = jFrame.getLocation();
            return new Point2D.Float(mp.x - fp.x + UPPER_LEFT_CORNER.x - Arena.TILE_SIZE, mp.y - fp.y - TOP_BORDER_SIZE + UPPER_LEFT_CORNER.x - Arena.TILE_SIZE);
        } catch (NullPointerException e) {
            System.out.println(e + ", getMousePos");
            return new Point2D.Float ();
        }
    }
    
    public void setBackgroundImage (Arena a) {
        backgroundImage = paintBackgroundImage (a);
    }
    
    public void setBackgroundImage (BufferedImage image) {
        backgroundImage = image;
    }
    
    public BufferedImage getBackgroundImage () {
        return backgroundImage;
    }
}

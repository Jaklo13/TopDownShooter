package topdownshooter;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Window {
    private JFrame jFrame;
    private JPanel panel;
    private BufferedImage backgroundImage;
    private int width, height;
    public Menu menu;
    //index 0 => x axis, index 1 => y axis

    private ControllerManager cmanager;
    private ControllerState cstate;
    
    public Window () {  //for loading the menu
        this.width = 1920;
        this.height = 1080;
        SwingUtilities.invokeLater(new Runnable () {
            @Override
            public void run() {
                createWindow ();
            }
        });
        
        cmanager = new ControllerManager();
        cmanager.initSDLGamepad();
    }
    
    public void createWindow () {
        jFrame = new JFrame ();
        jFrame.setTitle ("Game");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        menu = new Menu();
        panel = menu;
        jFrame.add (panel);
        jFrame.pack();
        jFrame.setLocationRelativeTo (null);
    }
    
    public void setWindow (JPanel p) {  //To change between game and main menu
        jFrame.remove(panel);
        panel = p;
        jFrame.add (panel);
        panel.requestFocusInWindow();
        jFrame.pack();
        jFrame.setLocationRelativeTo (null);
    }
    
    public void paintBackgroundImage (Arena a) {
        Point p = a.getWindowDimensions();
        BufferedImage image = new BufferedImage (p.x,p.y,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics g = image.getGraphics();
        g.setColor (new Color (000,200,100));           //Here we will ad a Ground Image later
        g.fillRect(0, 0, p.x, p.y);
        ArrayList<Wall> walls;
        walls = a.getWalls();
        for (Wall w : walls) {
            Rectangle2D.Float b = w.getBounds();
            g.drawImage(GameManager.GM.getSprite(GameManager.WALL_SPRITES, 0), (int)b.x, (int)b.y, null);
        }
        backgroundImage = image;
    }
    
    public void setBackgroundImageToPanel () {
        if (panel instanceof Panel) {
            ((Panel)panel).setBackgroundImage (backgroundImage);
        }
    }
    
    public float[] getXboxDirectionsLeft(){
        cstate = cmanager.getState(0);
        return new float[]{cstate.leftStickX, cstate.leftStickY};
    }

    public float[] getXboxDirectionsRight(){ //doesnt get a new state because its always called directly after getXboxDirectionsLeft()
        return new float[]{cstate.rightStickX, cstate.rightStickY}; // => avoids double reading
    }

    public boolean getXboxShooting(){
        cstate = cmanager.getState(0);
        //System.out.println(cstate.leftTrigger);
        return cstate.rightTrigger > 0.05f;
    }
    
    public Point2D.Float getDimensions () {
        return new Point2D.Float (width, height);
    }
    
    public float getDiagonal () {
        return (float)Math.sqrt(Math.pow(width,2) + Math.pow(height, 2));
    }
    
    //This is relative to the upper left corner of the panel
    public Point2D.Float getMousePos() {
        try {
            Point mp = MouseInfo.getPointerInfo().getLocation(), pp = jFrame.getComponent(0).getLocationOnScreen();
            return new Point2D.Float (mp.x - pp.x, mp.y - pp.y);
        } catch (NullPointerException | IllegalComponentStateException e) {
            System.out.println(e + ", getMousePos");
            return new Point2D.Float ();
        }
    }
    
    public void setBackgroundImage (Arena a) {
        paintBackgroundImage (a);
        setBackgroundImageToPanel ();
    }
    
    public void setBackgroundImage (BufferedImage image) {
        backgroundImage = image;
        setBackgroundImageToPanel ();
    }
    
    public BufferedImage getBackgroundImage () {
        return backgroundImage;
    }
    
    public JFrame getJFrame () {
        return jFrame;
    }
}

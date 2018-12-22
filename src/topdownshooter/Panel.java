package topdownshooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Panel extends JPanel{
    private Image backgroundImage = createImage (0,0);

    public Panel (Window window) {
        Point2D.Float dim = window.getDimensions();
        setFocusable (true);
        setBackground (Color.LIGHT_GRAY);
        setBorder (BorderFactory.createLineBorder(Color.BLACK, 10));
        setPreferredSize(new Dimension ((int)dim.x,(int)dim.y));
        
        MA ma = new MA ();
        addMouseListener (ma);
        addMouseMotionListener (ma);
        addMouseWheelListener (ma);
        addKeyListener (new KL ());
        
        setBackgroundImage (window.getBackgroundImage());
    }
    
    public void paintBackgroundImage (Graphics2D g) {
        g.drawImage(backgroundImage, 0,0, null);
    }
    
    public void paintGameObjects (Graphics2D g) {
        try {
            ArrayList<GameObject> gObjects = GameManager.GM.getGameObjects();
            for (GameObject go : gObjects) {
                if (!(go instanceof Wall)) {
                    BufferedImage sprite = go.getSprite();
                    float halfWidth = (float)go.getBounds().getWidth() / 2, halfHeight = (float)go.getBounds().getWidth() / 2;
                    Point2D.Float pos = go.getPos(GameObject.CENTER);
                    AffineTransform at = new AffineTransform ();

                    at.translate(pos.getX(), pos.getY());  //First the Affine Transform is centerd on the position
                    at.rotate(go.getRotation());                                    //Then it's rotated and centered around the Object's position
                    at.translate(-halfWidth, -halfHeight);                          
                    g.drawImage(go.getSprite(), at, this);

                    g.setColor(Color.red);                                        //use this to see the hitboxes of all Objects                        
                    g.drawRect((int)go.bounds.x, (int)go.bounds.y, (int)go.bounds.width, (int)go.bounds.height);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println (e + ", paintObjects");
        }
    }
    
    public void paintBullets (Graphics2D g) {
        
    } 
    
    //for testing and debugging
    public void paintDebug (Graphics2D g) {
        for (Line2D.Float l : Window.lines) {
            g.setColor(Color.BLACK);
            g.drawLine((int)l.x1,(int) l.y1, (int)l.x2, (int)l.y2);
        }
        for (Point2D.Float p : Window.points) {
            g.setColor(Color.RED);
            g.fillOval((int)p.x - 10,(int)p.y - 10,20,20);
        }
    }

    public void paintEverything (Graphics2D g) {
        try { 
            paintBackgroundImage (g);
            paintGameObjects (g);
            paintBullets (g);
            paintDebug (g);

        } catch (NullPointerException e) {
            System.out.println(e + ", paintComponent");
        }

        repaint ();
    }

    @Override
    public void paintComponent (Graphics g) {
        try {
            Image dbImage = createImage (getWidth(),getHeight());
            paintEverything ((Graphics2D)dbImage.getGraphics());
            g.drawImage(dbImage, 0, 0, this);
        } catch (Exception e) {
            System.out.println (e + ", paint");
        }
    }
    
    public void setBackgroundImage (Image b) {
        backgroundImage = b;
    }
    
    public class MA extends MouseAdapter {
        @Override
        public void mousePressed (MouseEvent e) {
            GameManager.GM.mouseClicked ();
        }
    }
    
    public static class KL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            GameManager.GM.addKp(e.getKeyCode());
        }
        @Override
        public void keyReleased(KeyEvent e) {
            GameManager.GM.removeKp(e.getKeyCode());
        }
    }
}

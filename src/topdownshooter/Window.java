package topdownshooter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import javax.swing.JFrame;
import com.studiohartman.jamepad.*;


public class Window extends JFrame {
    public static final int BORDER_SIZE = 25, TOP_BORDER_SIZE = 20;
    public static final Point UPPER_LEFT_CORNER = new Point (BORDER_SIZE, BORDER_SIZE + TOP_BORDER_SIZE);
    private int width, height;
    private ArrayList<Integer> kp = new ArrayList<Integer>(); //Keys Pressed
    //index 0 => x axis, index 1 => y axis


    private ControllerManager cmanager;
    private ControllerState cstate;

    public Window (int width, int height) {
        setTitle ("Game");
        setSize (width + BORDER_SIZE * 2, height + BORDER_SIZE * 2 + TOP_BORDER_SIZE);
        setVisible (true);
        setResizable (false);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);
        addKeyListener (new KL ());
        addMouseListener (new ML ());
        setLayout (null);
        
        this.width = width;
        this.height = height;

        cmanager = new ControllerManager();
        cmanager.initSDLGamepad();

    }

    public float[] getXboxDirectionsLeft(){
        cstate = cmanager.getState(0);
        return new float[]{cstate.leftStickX, cstate.leftStickY};
    }

    public float[] getXboxDirectionsRight(){ //doesnt get a new state because its always called directly after getXboxDirectionsLeft()
        return new float[]{cstate.rightStickX, cstate.rightStickY}; // => avoids double reading
    }

    
    public class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            AddPressedKey (e.getKeyCode());
        }
        @Override
        public void keyReleased(KeyEvent e) {
            RemovePressedKey (e.getKeyCode());
        }
    }
    
    public void AddPressedKey (int keyCode) {
        if (!kp.contains(keyCode))
            kp.add(keyCode);
    }
    
    public void RemovePressedKey (int keyCode) {
        if (kp.contains(keyCode))
            kp.remove(new Integer (keyCode));
    }
    
    public class ML implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public Point2D.Float getMousePos() {
        try {
            Point mp = MouseInfo.getPointerInfo().getLocation(), fp = getLocation();
            return new Point2D.Float(mp.x - fp.x + UPPER_LEFT_CORNER.x - Arena.TILE_SIZE, mp.y - fp.y - TOP_BORDER_SIZE + UPPER_LEFT_CORNER.x - Arena.TILE_SIZE);
        } catch (NullPointerException e) {
            System.out.println(e + ", getMousePos");
            return new Point2D.Float ();
        }
    }

    public ArrayList<Integer> GetKp() {
        return kp;
    }

    //all paint methods
    public void paintBackground (Graphics g) {
        g.setColor (Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void paintGround (Graphics2D g) {
        g.setColor (new Color (000,200,100));
        g.fillRect(0, 0, width, height);
        int ts = Arena.TILE_SIZE;
        for (Point p : GameManager.GM.getArena().getSpawnPoints()) {
            g.setColor(Color.BLUE);
            g.fillRect(p.x * ts, p.y * ts, ts, ts);
        }
    }
    
    //Dieser Code ist noch nicht Perfekt, sollte aber erstmal funktionieren
    public void paintGameObjects (Graphics2D g) {
        try {
            ArrayList<GameObject> gObjects = GameManager.GM.getGameObjects();
            for (GameObject go : gObjects) {
                //BufferedImage sprite = go.getSprite();
                int halfWidth = (int)go.getBounds().getWidth() / 2, halfHeight = (int)go.getBounds().getWidth() / 2;
                Point2D.Float pos = go.getPos();
                AffineTransform at = new AffineTransform ();
                
                at.translate(pos.getX() + halfWidth, pos.getY() + halfHeight);  //First the Affine Transform is centerd on the position
                at.rotate(go.getRotation());                                    //Then it's rotated and centered around the Object's position
                at.translate(-halfWidth, -halfHeight);                          
                g.drawImage(go.getSprite(), at, this);
                
                g.setColor(Color.red);                                        //use this to see the hitboxes of all Objects                        
                g.drawRect((int)go.bounds.x, (int)go.bounds.y, (int)go.bounds.width, (int)go.bounds.height);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println (e + ", paintObjects");
        }
    }
    
    public void paintBullets (Graphics2D g) {
        
    } 
    
    //for testing and debugging
    public void paintDebug (Graphics2D g) {

    }
    
    public void paintComponent (Graphics2D g) {
        try { 
            paintGround (g);
            paintGameObjects (g);
            paintBullets (g);
            paintDebug (g);
            
        } catch (NullPointerException e) {
            System.out.println(e + ", paintComponent");
        }
        
        repaint ();
    }
    
    @Override 
    public void paint (Graphics g) {
        try {
            Image dbImage = createImage (width,height);
            paintComponent ((Graphics2D)dbImage.getGraphics());
            paintBackground (g);
            g.drawImage(dbImage, BORDER_SIZE, BORDER_SIZE + TOP_BORDER_SIZE, this);
        } catch (Exception e) {
            System.out.println (e + ", paint");
        }
    }
}

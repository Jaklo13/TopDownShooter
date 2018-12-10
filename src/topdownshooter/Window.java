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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Window extends JFrame {
    public static final int BORDER_SIZE = 25, TOP_BORDER_SIZE = 20;
    private int width, height;
    private ArrayList<Integer> kp = new ArrayList<Integer>(); //Keys Pressed
    
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
    
    public Point GetMousePos () {
        try {
            Point mp = MouseInfo.getPointerInfo().getLocation(), fp = getLocation();
            Point pos = GameManager.AddPoint(mp, fp);
//            Point pos = new Point (mp.x - fp.x, mp.y - fp.y);
            return pos;
        } catch (NullPointerException e) {
            System.out.println(e + ", GetMousePos");
            return new Point ();
        }
    }

    public ArrayList<Integer> GetKp() {
        return kp;
    }

    //all paint methods
    public void paintBackground (Graphics2D g) {
        g.setColor (Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor (new Color (000,200,100));
        g.fillRect(BORDER_SIZE, BORDER_SIZE + TOP_BORDER_SIZE, width, height);
    }
    
    public void paintGameObjects (Graphics2D g) {
        ArrayList<GameObject> gObjects = GameManager.GM.GetGameObjects();
        for (GameObject go : gObjects) {
            BufferedImage sprite = go.getSprite();
            int spriteWidth = sprite.getWidth(), spriteHeight = sprite.getHeight();
            Point pos = new Point ();
            pos.setLocation(go.getPos());
            pos.setLocation (pos.getX() - spriteWidth / 2, pos.getY() - spriteHeight / 2);
            pos.setLocation (pos.getX() + BORDER_SIZE, pos.getY() + BORDER_SIZE + TOP_BORDER_SIZE);
            AffineTransform at = new AffineTransform ();
            at.translate(pos.getX(), pos.getY());               //These three lines need to be read in reverse order 
            at.rotate(go.getRotation());                        //First the Affine transform is centerd on the Image
            at.translate(spriteWidth / -2, spriteHeight / -2);  //Then it's rotated and centered around the player position
            g.drawImage(go.getSprite(), at, this);
            g.fillOval((int)pos.getX(), (int)pos.getY(), 5, 5);
        }
    }
    
    public void paintBullets (Graphics2D g) {
        
    } 
    
    //for testing and debugging
    public void paintDebug (Graphics2D g) {

    }
    
    public void paintComponent (Graphics2D g) {
        try { 
            paintBackground (g);
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
        Image dbImage = createImage (getWidth(),getHeight());
        paintComponent ((Graphics2D)dbImage.getGraphics());
        g.drawImage(dbImage, 0, 0, this);
    }
}

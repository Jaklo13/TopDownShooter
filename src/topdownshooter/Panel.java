package topdownshooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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
    private Point2D.Float dim;

    public Panel (int x, int y) {
        dim = new Point2D.Float (x,y);
        setFocusable (true);
        setBackground (Color.LIGHT_GRAY);
        setBorder (BorderFactory.createLineBorder(Color.BLACK, 10));
//        setBorder (BorderFactory.createLineBorder(Color.GRAY, ((1920 - dim.x < 1080 - dim.y)? (int)((1920 - dim.x) / 2) : (int)((1080 - dim.y) / 2))));
        setPreferredSize(new Dimension ((int)dim.x,(int)dim.y));
        
        MA ma = new MA ();
        addMouseListener (ma);
        addMouseMotionListener (ma);
        addMouseWheelListener (ma);
        addKeyListener (new KL ());
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
                    float halfWidth = (float)go.getBounds().getWidth() / 2, halfHeight = (float)go.getBounds().getHeight() / 2;
                    Point2D.Float pos = go.getPos(GameObject.CENTER);
                    AffineTransform at = new AffineTransform ();

                    at.translate(pos.getX(), pos.getY());                           //First the Affine Transform is centerd on the position
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
        BufferedImage bulletSprite = GameManager.GM.getSprite(GameManager.BULLET_SPRITES, 0);
        float width = (float)bulletSprite.getWidth(), halfHeight = (float)bulletSprite.getHeight() / 2, thickness = 0.5f;
        Shot[] shots = new Shot[GameManager.GM.getShotHandler().getShots().size()];
        GameManager.GM.getShotHandler().getShots().toArray(shots);
        Line2D.Float l;
        AffineTransform at;
        
        for (Shot shot : shots) {
            l = shot.getTrace();
            at = new AffineTransform ();
            at.translate(l.getX1(), l.getY1());
            at.rotate(l.x2 - l.x1, l.y2 - l.y1);
            at.translate(0, -halfHeight * thickness);
            at.scale(Math.hypot(l.x2 - l.x1, l.y2 - l.y1) / width, thickness);
            g.drawImage(shot.getSprite (), at, this);
        }
    } 
    
    public void paintUI (Graphics2D g) {
        ArrayList<Player> players = GameManager.GM.getPlayers();
        if (players.isEmpty())
            return;
        Point ulc = new Point (10,10); //upperLeftCorner
        int borderSize = 10;
        int gap = 60;
        int healthBarLength = ((int)dim.x - (players.size() * borderSize * 2 + gap * (players.size() - 1) + ulc.x * 2)) / (players.size ()), healthBarHeight = 15;
        
        for (int i = 0; i < players.size(); i++) {
            g.setColor(new Color (100,100,100,100));
            g.fillRect(ulc.x + i * (borderSize * 2 + healthBarLength + gap), ulc.y, borderSize * 2 + healthBarLength,healthBarHeight + borderSize * 2);
            g.setColor(Player.PLAYER_COLORS[players.get(i).getPn()]);
            g.fillRect(ulc.x + borderSize + i * (borderSize * 2 + healthBarLength + gap), ulc.y + borderSize, (int)(GameManager.GM.getPlayerHealthPercentage(i) * healthBarLength),healthBarHeight);
        }
    }
    
    //for testing and debugging
    public void paintDebug (Graphics2D g) {

    }

    public void paintEverything (Graphics2D g) {
        try { 
            paintBackgroundImage (g);
            paintGameObjects (g);
            paintBullets (g);
            paintUI (g);
            paintDebug (g);

        } catch (NullPointerException e) {
            System.out.println(e + ", paintEverything");
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
            System.out.println (e + ", paintComponent");
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
    
    public class KL extends KeyAdapter {
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

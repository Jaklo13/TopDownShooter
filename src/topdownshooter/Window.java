package topdownshooter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Window extends JFrame {
    private boolean[] bp = new boolean[0]; //Buttons Pressed
    private int x = 250, y = 250;
    private float speed = 3, lookDir = 0, lookDirDegree = 0; //lookDir in radians, lookDir in Degrees
    private Point mousePos = new Point(0,0);
    private BufferedImage[] sprites = new BufferedImage[0]; //Sprite0 = PlayerSprite
    private String[] spritePaths = new String[]{
            "src\\Assets\\PlayerSprite.png"};
    
    public Window () {
        setTitle ("Game");
        setSize (500,500);
        setVisible (true);
        setResizable (false);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);
        addKeyListener (new KL ());
        addMouseListener (new ML ());
        setLayout (null);
        
        GetSprites ();
        
        bp = new boolean[Controlls.BUTTONS.length];
    }

    public void GetSprites () {
        try {
            File file;
            sprites = new BufferedImage[spritePaths.length];
            for (String path : spritePaths) {
                file = new File (path);
                sprites[0] = ImageIO.read(file);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
    public void Update () {
        mousePos = Controlls.GetMousePos();
        LookAtPoint (mousePos);
        if (bp[0] != bp[1])
            y += (int)((bp[0])? -speed : speed);
        if (bp[2] != bp[3])
            x += (int)((bp[2])? -speed : speed);
    }
    
    public class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            Controlls.Direction[] d = Controlls.Direction.values();
            for (int i = 0; i < d.length; i++) {
                if (keyCode == d[i].GetKC())
                    bp[i] = true;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            Controlls.Direction[] d = Controlls.Direction.values();
            for (int i = 0; i < d.length; i++) {
                if (keyCode == d[i].GetKC())
                    bp[i] = false;
            }
        }
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
    
    public void LookAtPoint (Point p) {
        lookDir = (float)((Math.atan2(x - p.x, y - p.y))) * -1;
        lookDirDegree = (float)((Math.atan2(x - p.x, y - p.y) / Math.PI) * 360);
        System.out.println (lookDir);
    }
    
    public void paintComponent (Graphics2D g) {
        try { 
            Point mp = Controlls.GetMousePos();
            AffineTransform at = new AffineTransform ();
            int psw = sprites[0].getWidth(this), psh = sprites[0].getHeight(this); //PlayerSpriteWidth and Height
            at.translate(x, y);                 //These three lines need to be read in reverse order 
            at.rotate(lookDir);                 //First the Affine transform is centerd on the Image
            at.translate(psw / -2, psh / -2);   //Then it's rotated and centered around the player position
            g.drawImage(sprites[0], at, this);
            g.drawLine(x, y, mp.x, mp.y);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        repaint ();
    }
    
    @Override 
    public void paint (Graphics g) {
        Image dbImage = createImage (500,500);
        paintComponent ((Graphics2D)dbImage.getGraphics());
        g.drawImage(dbImage, 0, 0, this);
    }

}

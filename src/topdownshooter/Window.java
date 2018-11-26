package topdownshooter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Window extends JFrame {
    private int x = 0, y = 0, dir = -1;
    private float speed = 3, lookDir = 0;
    private Point mousePos = new Point(0,0);
    
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
        
//        jButton = new JButton ();
//        jButton.addActionListener(new ActionListener(){
//            @Override 
//            public void actionPerformed (ActionEvent evt) {
//                System.out.println ("test");
//            }
//        });
//        l = new JLabel ();
//        l.setBounds (200,200,100,100);
//        this.add(l);
//        l.requestFocusInWindow ();
//        JTextField t2 = new JTextField ();
//        t2.setBounds (300,200,100,100);
//        this.add(t2);
////        t2.requestFocusInWindow ();
//        jButton.setBounds(100,100,100,100);
//        this.add (jButton);
    }
    
    public void Update () {
        mousePos = Controlls.GetMousePos();
//        LookAtPoint (mousePos);
        switch (dir) {
            case 0:
                y -= speed;
                break;
            case 1:
                y += speed;
                break;
            case 2:
                x -= speed;
                break;
            case 3:
                x += speed;
                break;
        }
    }
    
    public class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            for (Controlls.Direction d : Controlls.Direction.values()) {
                if (keyCode == d.GetKC())
                    dir = d.GetDir();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            for (Controlls.Direction d : Controlls.Direction.values()) {
                if (keyCode == d.GetKC() && dir == d.GetDir())
                    dir = -1;
            }
        }
    }
    
    public class ML implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
//            x = e.getX();
//            y = e.getY();
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
    
//    public void LookAtPoint (Point p) {
////        Math.atan
//        lookDir = (float)Math.atan2(x - p.x, y - p.y);
//        lookDir = (float)Math.tan(lookDir);
//        System.out.println (lookDir);
//    }
    
    public void paintComponent (Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, 100, 100);
//        g.fillOval(x, y, WIDTH, HEIGHT);
        
        repaint ();
    }
    
    @Override 
    public void paint (Graphics g) {
        Image dbImage = createImage (500,500);
        paintComponent (dbImage.getGraphics());
        g.drawImage(dbImage, 0, 0, this);
    }
    
//    private enum Direction {
//        UP(0),DOWN(1),LEFT(2),RIGHT(3);
//        public static final int[] DIR_BUTTONS = new int[] {KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT};
//        int dir = 0, kCode = 0;
//        Direction (int dir) {
//            this.dir = dir;
//            switch (dir) {
//                default:
//                    kCode = KeyEvent.VK_UP;
//                    break;
//                case 1:
//                    kCode = KeyEvent.VK_DOWN;
//                    break;
//                case 2:
//                    kCode = KeyEvent.VK_LEFT;
//                    break;
//                case 3:
//                    kCode = KeyEvent.VK_RIGHT;
//                    break;
//            }
//        }
//        public int GetDir () {
//            return dir;
//        }
//        public int GetKC () {
//            return kCode;
//        }
//    }
}

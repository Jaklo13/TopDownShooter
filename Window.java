package topdownshooter;

import java.awt.Graphics;
import java.awt.Image;
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
    int x = 0, y = 0;
    float speed = 3;
    boolean UP, DOWN, LEFT, RIGHT;
    JButton jButton;
    JLabel l = new JLabel ();
    
    public Window () {
        //Hello World
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
        if (UP) {
            y -= 3;
        }
        else if (DOWN) {
            y += 3;
        }
    }
    
    public class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == e.VK_UP) {
                UP = true;
            }
            if (keyCode == e.VK_DOWN) {
                DOWN = true;
            }
            l.setText(l.getText() + e.getKeyChar());
        }
        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == e.VK_UP) {
                UP = false;
            }
            if (keyCode == e.VK_DOWN) {
                DOWN = false;
            }
        }
    }
    
    public class ML implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
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
    
    public void paintComponent (Graphics g) {
        g.fillRect(x, y, 100, 100);
        
        repaint ();
    }
    
    @Override 
    public void paint (Graphics g) {
        Image dbImage = createImage (500,500);
        paintComponent (dbImage.getGraphics());
        g.drawImage(dbImage, 0, 0, this);
    }
}

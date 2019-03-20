package topdownshooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class Menu extends JPanel {
    private JButton[] buttons = new JButton[3];
    private String[] labels = new String[]{"Start Game","Options","End Game"};
    private Point[] locations = new Point[]{new Point(400,500),new Point(400,600),new Point(400,700)};
    private Point[] sizes = new Point[]{new Point(500,100),new Point(500,100),new Point(500,100)};
    private JLabel title = new JLabel ("GAME");
    
    public Menu () {
        setPreferredSize (new Dimension (1920,1080));
        setLayout (null);
        
        try {
            SynthLookAndFeel laf = new SynthLookAndFeel ();
            laf.load(new File (GameManager.ASSETS_PATH + "Button.xml").toURI().toURL());
            UIManager.setLookAndFeel(laf);
        } catch (Exception e) {
            System.out.println (e);
            e.printStackTrace ();
        }
        
        initButtons ();
        initTitle ();
        
    }
    
    public void initButtons () {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton ();
            buttons[i].setText (labels[i]);
            buttons[i].setBounds(0,locations[i].y,1920,sizes[i].y);
            add (buttons[i]);
        }
        buttons[0].addActionListener((ActionEvent e) -> {
            GameManager.GM.startGame();
        });

        buttons[1].addActionListener(e -> {
            GameManager.GM.window.setWindow(new OptionMenu());
        });

        buttons[2].addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
    }
    
    public void initTitle () {
        title.setBounds(100, 100, 1000, 100);
        title.setFont(new Font ("Arial",Font.BOLD,100));
        add (title);
    }
    
    @Override
    public void paintComponent (Graphics g) {
        g.setColor (Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

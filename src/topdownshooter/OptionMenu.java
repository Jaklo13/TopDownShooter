package topdownshooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionMenu extends JPanel{
    private JButton button;
    
    public OptionMenu () {
        setPreferredSize (new Dimension (500,500));
        setVisible (true);
        setBackground (Color.LIGHT_GRAY);
        setLayout (null);
        
        button = new JButton ();
        button.setBounds (100,100,100,100);
        button.setText("test");
        button.setFont(new Font ("Arial",Font.BOLD,10));
        this.add(button);
    }
}

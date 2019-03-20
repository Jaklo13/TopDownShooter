package topdownshooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.*;

public class OptionMenu extends JPanel{
    private JButton button;
    
    public OptionMenu () {
        setPreferredSize (new Dimension (500,500));
        setVisible (true);
        setBackground (Color.LIGHT_GRAY);
        setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
        
        button = new JButton ();
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setBounds (100,100,100,100);
        button.setText("Exit");
        button.addActionListener(e -> {
            GameManager gm = GameManager.GM;
            gm.window.setWindow(gm.window.menu);
        });
        //button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setFont(new Font ("Arial",Font.BOLD,20));

        this.add(button);

        File dir = new File(GameManager.ASSETS_PATH + "\\Arenas");

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                int li = name.lastIndexOf('.');
                String ftype = name.substring(li);
                return ftype.equals(".txt");
            }
        };

        File[] files = dir.listFiles(filter);
        if(files != null){
            for(File f : files){
                final String full_name = f.getName();
                final int li = full_name.lastIndexOf('.');
                final String name = full_name.substring(0, li);

                JButton b = new JButton();
                b.setBounds(100, 100, 100, 100);
                b.setFont(new Font ("Arial",Font.BOLD,20));
                b.setText(name);
                b.setAlignmentX(CENTER_ALIGNMENT);

                b.addActionListener(e -> {
                    TxtArena.selectedArena = name;
                    GameManager.GM.startGame();
                });

                add(b);
            }
        }

    }
}

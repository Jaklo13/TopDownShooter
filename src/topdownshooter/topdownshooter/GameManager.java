package topdownshooter;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//@startuml

public class GameManager implements Runnable{

    public static String ASSETS_PATH;

    public static final String[][] SPRITE_NAMES = new String[][]{
            new String[] {"Player.png"},
            new String[] {"Pistol.png","Rifle.png"},
            new String[] {"Orb.png"},
            new String[] {"Wall.png"},
            new String[] {"Bullet.png"}};
    public static final int PLAYER_SPRITES = 0, WEAPON_SPRITES = 1, ITEM_SPRITES = 2, WALL_SPRITES = 3, BULLET_SPRITES = 4; //use as the first pointer in the allSprites array
    public static GameManager GM;
    private BufferedImage[][] sprites = new BufferedImage[][]{new BufferedImage[SPRITE_NAMES[0].length], new BufferedImage[SPRITE_NAMES[1].length], new BufferedImage[SPRITE_NAMES[2].length], new BufferedImage[SPRITE_NAMES[3].length], new BufferedImage[SPRITE_NAMES[4].length]};
    private ArrayList<GameObject> gObjects = new ArrayList<>();   //this keeps track of all GameObjects, so the Window can draw it
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private HashSet<Integer> kp = new HashSet<>();  //Keys pressed
    private Arena arena;
    public Window window;
    private int menu = 1; //0 - in game, 1 - main menu
    private ShotHandler sHandler;
    private Clip bgm;

    public GameManager () {

        GM = this;
        initializeSprites();
        sHandler = new ShotHandler ();
        window = new Window ();
        
        Key.initializeKeyCodesArray();
    }

    public void startGame () {
        menu = 0;
        //arena = new Arena (1);
        arena = new TxtArena();
        spawnPlayer(0, false);
        spawnPlayer(1, true);
        Point p = arena.getWindowDimensions();
        window.setWindow (new Panel (p.x,p.y));
        window.setBackgroundImage(arena);
        kp.clear();
        startMusic ();
    }
    
    public void endGame () {
        JOptionPane.showMessageDialog(window.getJFrame(), "Player " + (players.get(0).getPn() + 1) + " has won", "End Of Match", 0);
        menu = 1;
        remove (players.get(0));
        window.setWindow(window.menu);
        stopMusic ();
    }
    
    public void spawnPlayer(int pn, boolean isXbox) {
        try {
            Player player = (isXbox) ? new XboxPlayer (pn) : new Player (pn);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println (e + ", Invalid Player Number");
            e.printStackTrace ();
        }
    }
    
    public <T> void addGameObject(T o) {
        if (o instanceof GameObject) {
            gObjects.add((GameObject)o);        //adds the GameObject to gObject for the paint function
            if (o instanceof Wall)              //adds the object to the correct array
                walls.add((Wall)o);
            else if (o instanceof Player)
                players.add((Player)o);
            else if (o instanceof Item)
                items.add((Item)o);
        } else {
            System.out.println ("Error, not a GameObject");
        }
    }
    
    public <T> void remove (T o) {
        if (o instanceof GameObject) {
            gObjects.remove((GameObject)o);        //adds the GameObject to gObject for the paint function
            if (o instanceof Wall)              //adds the object to the correct array
                walls.remove((Wall)o);
            else if (o instanceof Player) {
                players.remove((Player)o);
                if (hasEnded()) {
                    endGame ();
                }
            } else if (o instanceof Item)
                items.remove((Item)o);
        } else {
            System.out.println ("Error, not a GameObject");
        }
    }
    
    public boolean hasEnded () {
        return (players.size() == 1);
    }
    
    private void update() {
        updatePlayers();
        sHandler.update();
        checkFocus ();
    }
    
    public void updatePlayers() {
        try {
            for (Player p : players) {
                p.update();
            }
        } catch (ConcurrentModificationException e) {
            System.out.println (e);
        }
    }
    
    public void checkFocus () {
        JFrame f = window.getJFrame();
        if (f != null)
            if (!f.isActive())
                kp.clear();
    }
    
    public void mouseClicked () {
        if (!(players.get(0)instanceof XboxPlayer))
            players.get(0).setShooting (true);
    }
    
    public void mouseReleased () {
        if (!(players.get(0)instanceof XboxPlayer))
            players.get(0).setShooting (false);
    }
    
    public void initializeSprites() {
        try {
            for (int i = 0; i < SPRITE_NAMES.length; i++){
                for (int j = 0; j < SPRITE_NAMES[i].length; j++) {
                    File file = new File (ASSETS_PATH + SPRITE_NAMES[i][j]);
                    sprites[i][j] = ImageIO.read(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    
    public static BufferedImage colorImage (BufferedImage bi, Color c) {
        bi = new BufferedImage (bi.getColorModel(),bi.copyData(null),bi.isAlphaPremultiplied(),null);   //creates a copy of the default image
        int[] rgb = bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), null, 0, bi.getWidth());
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] &= c.getRGB();
        }
        bi.setRGB (0,0,bi.getWidth(),bi.getHeight(),rgb,0,bi.getWidth());
        return bi;
    }
    
    public <T> ArrayList<GameObject> intersectsAny (Shape s, Class<T> type) {
        ArrayList<GameObject> intersectedObjects = new ArrayList<>();
        ArrayList<T> list;
        if (type == Wall.class) {
            list = (ArrayList<T>)walls;
        } else if (type == Player.class) {
            list = (ArrayList<T>)players;
        } else if (type == Item.class) {
            list = (ArrayList<T>)items;
        } else {
            list = (ArrayList<T>)gObjects;
        }
        for (GameObject g : (ArrayList<GameObject>)list) {
            if (s instanceof Line2D) {
                if (g.getBounds().intersectsLine((Line2D)s))
                    intersectedObjects.add(g); 
            } else {
                if (g.getBounds().intersects((Rectangle2D)s)) {
                    intersectedObjects.add(g);
                }
            }
        }
        return intersectedObjects;
    }
    
    public void startMusic () {
        try {
            bgm = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File (ASSETS_PATH + "Doom_Theme.wav"));
            bgm.open(inputStream);
            FloatControl volume = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-1 * 20);
            bgm.setLoopPoints(0, -1);
            bgm.loop(999);
            bgm.start();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
    public void stopMusic () {
        bgm.stop();
    }
    
    public Window getWindow() {
        return window;
    }

    public Arena getArena() {
        return arena;
    }
    
    public ShotHandler getShotHandler() {
        return sHandler;
    }
    
    public ArrayList<GameObject> getGameObjects() {
        return gObjects;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public int getPlayerHealth (int pn) {
        return players.get(pn).getHealth ();
    }
    
    public float getPlayerHealthPercentage (int pn) {
        return players.get(pn).getHealthPercentage ();
    }
    
    public ArrayList<Shot> getShots () {
        return sHandler.getShots();
    }

    public HashSet<Integer> getKp() {
        return kp;
    }

    public void addKp(int k) {
        kp.add(k);
    }
    
    public void removeKp (int k) {
        kp.remove (k);
    }
    
    public Point2D.Float getShootingDistance (float r) {
        float minLength = window.getDiagonal() * -1;        //The will always be longer than it needs to be, but since there aren't any Objects outside
        Point2D.Float p = new Point2D.Float ();             //the window, the length doesn't matter, as long as the shot reaches the end
        p.x = (float)(Math.cos (r) * minLength);
        p.y = (float)(Math.sin (r) * minLength);
        return p;
    }
    
    public  BufferedImage[][] getSprites() {
        return sprites;
    }
    
    public  BufferedImage[] getSpriteType(int type) {
        return sprites[type];
    }
    
    public  BufferedImage getSprite (int type, int nr) {
        try {
            if (nr >= sprites[type].length) {
                throw new Exception ();
            }
        } catch (Exception e) {
            System.out.println ("Error, sprite not found");
            e.printStackTrace ();
            return sprites[type][0];
        }
        return sprites[type][nr];
    }
    
    public Point2D.Float getMousePos () {
        return window.getMousePos();
    }
    
    public static void main(String[] args) {

        //workaround to ensure correct path
        File f = new File("");
        System.out.println(f.getAbsolutePath() + "\\");
        ASSETS_PATH = f.getAbsolutePath() + "\\src\\Assets\\";

        Thread t1 = new Thread(new GameManager());
        t1.start();

    }

    @Override
    public void run() {
        try {
            while (true) {
//                long test = System.currentTimeMillis (); // use this to test if the game is lagging
                long n = System.currentTimeMillis ();
                
                update();
                
                n = System.currentTimeMillis () - n;
                
                if (n < 16)
                    Thread.sleep (16 - n);
//                System.out.println(System.currentTimeMillis () - test);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
}
//@enduml
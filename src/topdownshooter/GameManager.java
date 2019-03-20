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
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
    private static BufferedImage[][] sprites = new BufferedImage[][]{new BufferedImage[SPRITE_NAMES[0].length], new BufferedImage[SPRITE_NAMES[1].length], new BufferedImage[SPRITE_NAMES[2].length], new BufferedImage[SPRITE_NAMES[3].length], new BufferedImage[SPRITE_NAMES[4].length]};
    private static BufferedImage backgroundImage;
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
        sHandler = new ShotHandler ();
        window = new Window ();
        
        Key.initializeKeyCodesArray();
    }
    
    public void startGame () {
        menu = 0;
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
        removeEverything ();
        window.setWindow(new Menu ());
        stopMusic ();
    }
    
    public void removeEverything () {
        while (!players.isEmpty()) {
            remove (players.get(0));
        }
        while (!items.isEmpty()) {
            remove (items.get(0));
        }
        while (!walls.isEmpty()) {
            remove (walls.get(0));
        }
    }
    
    public void spawnPlayer(int pn, boolean isXbox) {
        try {
            Player player = (isXbox)? new XboxPlayer (pn) : new Player (pn);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println (e + ", Invalid Player Number");
            e.printStackTrace ();
        }
    }
    
    public void addGameObject(GameObject o) {
        if (o instanceof Wall)              //adds the object to the correct array
            walls.add((Wall)o);
        else if (o instanceof Player)
            players.add((Player)o);
        else if (o instanceof Item)
            items.add((Item)o);
    }
    
    public void remove (GameObject o) {
        if (o instanceof Wall)                  //adds the object to the correct array
            walls.remove((Wall)o);
        else if (o instanceof Player) {
            players.remove((Player)o);
            if (hasEnded()) {
                endGame ();
            }
        } else if (o instanceof Item)
            items.remove((Item)o);
    }
    
    public boolean hasEnded () {
        return (players.size() == 1);
    }
    
    private void update() {
        updatePlayers();
        updateItems ();
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
    
    public void updateItems () {
        for (Item i : items) {
            i.update();
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
    
    public static void initializeSprites() {
        try {
            for (int i = 0; i < SPRITE_NAMES.length; i++){
                for (int j = 0; j < SPRITE_NAMES[i].length; j++) {
                    File file = new File (ASSETS_PATH + SPRITE_NAMES[i][j]);
                    sprites[i][j] = ImageIO.read(file);
                }
            }
            backgroundImage = ImageIO.read(new File (ASSETS_PATH + "Background.png"));
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
            System.out.println ("Error, not a valid type");
            return intersectedObjects;
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
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace ();
        }
    }
    
    public void stopMusic () {
        bgm.stop();
    }
    
    public void playShotSound () {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File (ASSETS_PATH + "Shot.wav"));
            clip.open(inputStream);
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-1 * 30);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    public void playHitSound () {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File (ASSETS_PATH + "HitPlayer.wav"));
            clip.open(inputStream);
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-1 * 25);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace ();
        }
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
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
    
    public BufferedImage[][] getSprites() {
        return sprites;
    }
    
    public BufferedImage[] getSpriteType(int type) {
        return sprites[type];
    }
    
    public BufferedImage getSprite (int type, int nr) {
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
    
    public BufferedImage getBackgroundImage () {
        return backgroundImage;
    }
    
    public Point2D.Float getMousePos () {
        return window.getMousePos();
    }
    
    public static void main(String[] args) {
        //workaround to ensure correct path
        File f = new File("");
        System.out.println(f.getAbsolutePath() + "\\");
        ASSETS_PATH = f.getAbsolutePath() + "\\src\\Assets\\";

        initializeSprites ();
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

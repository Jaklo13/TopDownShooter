package topdownshooter;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameManager implements Runnable{
    public static final String ASSETS_PATH = "src\\Assets\\";
    public static final String[][] SPRITE_NAMES = new String[][]{
            new String[] {"PlayerSprite1.png","PlayerSprite2.png"},
            new String[] {"Pistol.png","Rifle.png"},
            new String[] {},
            new String[] {"Wall.png"}};
    public static final int PLAYER_SPRITES = 0, WEAPON_SPRITES = 1, ITEM_SPRITES = 2, WALL_SPRITES = 3; //use as the first pointer in the allSprites array
    public static GameManager GM;
    private BufferedImage[][] sprites = new BufferedImage[][]{new BufferedImage[SPRITE_NAMES[0].length], new BufferedImage[SPRITE_NAMES[1].length], new BufferedImage[SPRITE_NAMES[2].length], new BufferedImage[SPRITE_NAMES[3].length]} ;
    private ArrayList<GameObject> gObjects = new ArrayList<>();   //this keeps track of all GameObjects, so the Window can draw it
    private ArrayList<Player> players = new ArrayList<>();
    private HashSet<Integer> kp = new HashSet<>();  //Keys pressed
    private Arena arena;
    private Window window;
    
    public GameManager () {
        GM = this;
        initializeSprites();
        arena = new Arena (1);
        window = arena.getWindow ();
        
        Key.initializeKeyCodesArray();
        
        spawnPlayer(0, false);
        spawnPlayer(1, true);
    }
    
    public void spawnPlayer(int pn, boolean isXbox) {
        try {
            Player player = (isXbox)? new XboxPlayer (pn) : new Player (pn);
            players.add(player);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println (e + ", Invalid Player Number");
        }
    }
    
    public void addGameObject(GameObject o) {
        gObjects.add(o);
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    private void update() {
        updatePlayers();
        checkFocus ();
    }
    
    public void updatePlayers() {
        for (Player p : players) {

            p.update();
        }
    }
    
    public void checkFocus () {
        JFrame f = window.getJFrame();
        if (f != null)
            if (!f.isActive())
                kp.clear();
    }
    
    public void mouseClicked () {
        for (Player p : players) {
            if (!(p instanceof XboxPlayer)) {
                p.shoot ();
            }
        }
    }
    
    public void initializeSprites() {
        try {
            for (int i = 0; i < sprites.length; i++){
                for (int j = 0; j < sprites[i].length; j++) {
                    File file = new File (ASSETS_PATH + SPRITE_NAMES[i][j]);
                    sprites[i][j] = ImageIO.read(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
    public ArrayList<GameObject> intersectsAny (Shape s) {
        ArrayList<GameObject> intersectedObjects = new ArrayList<>();
        for (GameObject g : gObjects) {
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
    
    public Window getWindow() {
        return window;
    }

    public Arena getArena() {
        return arena;
    }
    
    public ArrayList<GameObject> getGameObjects() {
        return gObjects;
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
        return sprites[type][nr];
    }
    
    public Point2D.Float getMousePos () {
        return window.getMousePos();
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(new GameManager());
        t1.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                update();
                
                Thread.sleep (16);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
}

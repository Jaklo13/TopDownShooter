package topdownshooter;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;

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
//        System.out.println (kp.size());
    }
    
    public void updatePlayers() {
        for (Player p : players) {

            p.update();
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
    
    public ArrayList<GameObject> intersectsAny (Rectangle2D.Float r) {
        ArrayList<GameObject> intersectedObjects = new ArrayList<>();
        for (GameObject g : gObjects) {
            if (r.intersects(g.getBounds())) {
                intersectedObjects.add(g);
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
    
    public  BufferedImage[][] getSprites() {
        return sprites;
    }
    
    public  BufferedImage[] getSpriteType(int type) {
        return sprites[type];
    }
    
    public  BufferedImage getSprite (int type, int nr) {
        return sprites[type][nr];
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

package topdownshooter;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GameManager implements Runnable{
    public static final String assetsPath = "src\\Assets\\";
    public static final String[][] spriteNames = new String[][]{
            new String[] {"PlayerSprite1.png","PlayerSprite2.png"},
            new String[] {"Pistol.png","Rifle.png"},
            new String[] {},
            new String[] {"Wall.png"}};
    public static final int PLAYER_SPRITES = 0, WEAPON_SPRITES = 1, ITEM_SPRITES = 2, WALL_SPRITES = 3; //use as the first pointer in the allSprites array
    public static GameManager GM;
    private static BufferedImage[][] sprites = new BufferedImage[][]{new BufferedImage[spriteNames[0].length], new BufferedImage[spriteNames[1].length], new BufferedImage[spriteNames[2].length], new BufferedImage[spriteNames[3].length]} ;
    private ArrayList<GameObject> gObjects = new ArrayList<>();   //this keeps track of all GameObjects, so the Window can draw it
    private ArrayList<Player> players = new ArrayList<>();
    private Arena arena;
    private Window window;
    
    public GameManager () {
        GM = this;
        initializeSprites();
        arena = new Arena (1);
        window = arena.GetWindow ();
        
        Key.InitializeKeyCodesArray();
        
        spawnPlayer(0);
        //spawnPlayer(1);
        Player xbox = new XboxPlayer(1, sprites[PLAYER_SPRITES][1]);
        players.add(xbox);
    }
    
    public void spawnPlayer(int pn) {
        try {
            Player player = (new Player (pn,sprites[PLAYER_SPRITES][pn]));
            players.add(player);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println (e + ", Invalid Player Number");
        }
    }


    
    public void addGameObject(GameObject o) {
        gObjects.add(o);
    }
    
    private void update() {
        ArrayList<Integer> keys = window.GetKp();
        updatePlayers(keys);
    }
    
    public void updatePlayers(ArrayList<Integer> keys) {
        for (Player p : players) {

            p.Update();
        }
    }
    
    public void initializeSprites() {
        try {
            for (int i = 0; i < sprites.length; i++){
                for (int j = 0; j < sprites[i].length; j++) {
                    File file = new File (assetsPath + spriteNames[i][j]);
                    sprites[i][j] = ImageIO.read(file);
                }
            }
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
    
    public ArrayList<GameObject> getGameObjects() {
        return gObjects;
    }
    
    public ArrayList<GameObject> IntersectsAny (Rectangle2D.Float r) {
        ArrayList<GameObject> intersectedObjects = new ArrayList<>();
        for (GameObject g : gObjects) {
            if (r.intersects(g.getBounds())) {
                intersectedObjects.add(g);
            }
        }
        return intersectedObjects;
    }
    
    public static BufferedImage[][] getSprites() {
        return sprites;
    }
    
    public static BufferedImage[] getSpriteType(int type) {
        return sprites[type];
    }
    
    public static BufferedImage getSprite (int type, int nr) {
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

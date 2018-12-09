package topdownshooter;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GameManager implements Runnable{
    public static final String assetsPath = "src\\Assets\\";
    public static final String[] spriteNames = new String[]{
            "PlayerSprite1.png",
            "PlayerSprite2.png",
            "Pistol.png",
            "Rifle.png"};
    public static GameManager GM;
    private ArrayList<GameObject> gObjects = new ArrayList<GameObject>();   //this keeps track of all GameObjects, so the Window can draw it
    private BufferedImage[] playerSprites = new BufferedImage[2], weaponSpeites = new BufferedImage[0];
    private ArrayList<Player> players = new ArrayList<Player>();
    private Window window;
    
    public GameManager () {
        GM = this;
        window = new Window ();
        
        Key.InitializeKeyCodesArray();
        GetSprites ();
        SpawnPlayer (0);
        SpawnPlayer (1);
    }
    
    public void SpawnPlayer (int pn) {
        try {
            Player player = (new Player (pn,playerSprites[pn]));
            players.add(player);
            gObjects.add(player);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println (e + ", Invalid Player Number");
        }
    }
    
    private void Update () {
        ArrayList<Integer> keys = window.GetKp();
        UpdatePlayers (keys);
//        System.out.println (players.get(0).getRotation());
    }
    
    public void UpdatePlayers (ArrayList<Integer> keys) {
        for (Player p : players) {
            p.Update (keys);
        }
    }
    
    public void GetSprites () {
        try {
            File file;
            for (int i = 0; i < playerSprites.length; i++) {
                file = new File (assetsPath + spriteNames[i]);
                playerSprites[i] = ImageIO.read(file);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
    public Window GetWindow () {
        return window;
    }
    
    public ArrayList<GameObject>GetGameObjects () {
        return gObjects;
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(new GameManager());
        t1.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Update ();
                
                Thread.sleep (16);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
}

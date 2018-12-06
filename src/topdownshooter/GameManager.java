package topdownshooter;

import java.awt.event.KeyEvent;

public class GameManager implements Runnable{
    public static GameManager GM;
    private Player player1, player2;
    private Controlls contro1s1, controls2;
    private Window window;
    
    public GameManager () {
        GM = this;
        window = new Window ();
        contro1s1 = new Controlls (new int[]{KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT});
        controls2 = new Controlls (new int[]{KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D});
    }
    
    private void Update () {
        window.Update ();
    }
    
    public Window GetWindow () {
        return window;
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

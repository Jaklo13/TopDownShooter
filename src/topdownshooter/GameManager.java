package topdownshooter;

public class GameManager implements Runnable{
    public static GameManager GM;
    private Player player1, player2;
    private Window window;
    
    public GameManager () {
        GM = this;
        window = new Window ();
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

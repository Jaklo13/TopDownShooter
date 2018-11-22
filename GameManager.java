package topdownshooter;

public class GameManager implements Runnable{
    Player player1, player2;
    Window window;
    
    public GameManager () {
        window = new Window ();
    }
    
    private void Update () {
        window.Update ();
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

package topdownshooter;

public class Arena {
    public static final int TILE_SIZE = 50;
    public static Window window;
    private int width, height;
    
    public Arena (int width, int height) {
        this.width = width;
        this.height = height;
        
        window = new Window (width * TILE_SIZE, height * TILE_SIZE);
    }
    
    public Window GetWindow () {
        return window;
    }
}

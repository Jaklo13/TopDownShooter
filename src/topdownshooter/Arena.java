package topdownshooter;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Arena {
    public static final int TILE_SIZE = 50;
    public static Window window;
    private int width, height;
    //0 - open, 1 - wall
    private int[][] tiles;
    private ArrayList<Wall> walls;
    
    public Arena (int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new int[width][height];
        window = new Window (width * TILE_SIZE, height * TILE_SIZE);
        
        walls = new ArrayList<Wall>();
        CreateOuterWall ();
    }
    
    public void CreateOuterWall () {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i][0] = 1;
            tiles[i][tiles[i].length-1] = 1;
        }
        for (int i = 0; i < tiles[0].length; i++) {
            tiles[0][i] = 1;
            tiles[tiles.length-1][i] = 1;
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == 1)
                    walls.add(new Wall(new Point2D.Float(i*TILE_SIZE + TILE_SIZE / 2,j*TILE_SIZE + TILE_SIZE / 2)));
            }
        }
    }
    
    public Window GetWindow () {
        return window;
    }
}

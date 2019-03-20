package topdownshooter;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Arena {
    public static final int TILE_SIZE = 50;
    private int width, height;
    //0 - open, 1 - wall
    protected int[][] tiles;
    private ArrayList<Wall> walls;
    private ArrayList<Point> spawnPoints;


    //needed for txtarena
    protected Arena(){

    }

    public Arena (int type) {
        createArena (type);
        
        placeOuterWall ();
        placeTiles ();
        createTiles ();
    }
    
    public void createArena (int type) {
        switch (type) {
            case 1:
                width = 20;
                height = 10;
                tiles = new int[width][height];
                break;
        }
    }
    
    public void placeOuterWall () {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i][0] = 1;
            tiles[i][tiles[i].length-1] = 1;
        }
        for (int i = 0; i < tiles[0].length; i++) {
            tiles[0][i] = 1;
            tiles[tiles.length-1][i] = 1;
        }
    }
    
    public void placeTiles () {
        placeWall (true,new Point(3,3),5);
        placeWall (true,new Point(3,6),5);
        placeWall (true,new Point(12,3),5);
        placeWall (true,new Point(12,6),5);
        placeWall (false,new Point(9,3),4);
        placeWall (false,new Point(10,3),4);
        tiles[1][1] = 2;
        tiles[18][8] = 2;
    }
    
    public void placeWall (boolean horizontal, Point pos, int length) {
        for (int i = 0; i < length; i++) {
            tiles[pos.x + ((horizontal)?i:0)][pos.y + ((horizontal)?0:i)] = 1;
        }
    }
    
    public void createTiles () {
        walls = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == 1) {
                    walls.add(new Wall(new Point2D.Float(i*TILE_SIZE,j*TILE_SIZE)));
                }
                if (tiles[i][j] == 2) {
                    spawnPoints.add(new Point(i,j));
                }
            }
        }
    }
    
    public Point2D.Float getSpawnPoint (int p) {
        return new Point2D.Float(spawnPoints.get(p).x * TILE_SIZE,spawnPoints.get(p).y * TILE_SIZE);
    }
    
    public ArrayList<Point> getSpawnPoints () {
        return spawnPoints;
    }
    
    public Point getWindowDimensions () {

        int w = tiles.length;
        int h = tiles[0].length;
        return new Point (w * TILE_SIZE, h * TILE_SIZE);
    }
    
    public ArrayList<Wall> getWalls () {
        return walls;
    }
}

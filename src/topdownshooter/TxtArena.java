package topdownshooter;


import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/*
 * Creates an arena by reading from a txt file, follwing this Syntax:
 * P = Spawnpoint
 * X = Wall
 * . = Floor
 * */
public class TxtArena extends Arena {

    private File arena1;
    //protected int[][] tiles;

    public static final int TILE_SIZE = 50;
    private int width, height;
    private ArrayList<Wall> walls;
    private ArrayList<Point> spawnPoints;

    public TxtArena(){
        arena1 = new File("src\\Assets\\arena1.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(arena1));
            ArrayList<String> rows = new ArrayList<>();
            String s;
            while ((s = reader.readLine()) != null) {
                rows.add(s);
            }
            tiles = new int[rows.get(0).length()][rows.size()];

            for(int x = 0; x < tiles.length; x++){
                for(int y = 0; y < tiles[0].length; y++){
                    char c = rows.get(y).charAt(x);
                    tiles[x][y] = parseType(c);
                }
            }

            createTiles();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private int parseType(char c){
        if(c == 'P'){
            return 2;
        }
        if(c == 'X' || c == 'x'){
            return 1;
        }

        if(c == '.'){
            return 0;
        }

        System.out.println("ERROR AT PARSING");

        return -1;
    }

    private void printTiles(){
        for(int[] col : tiles){
            for(int row : col){
                System.out.print(row);
            }
            System.out.println();
        }
    }

}

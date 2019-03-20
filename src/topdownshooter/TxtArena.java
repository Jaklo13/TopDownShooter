package topdownshooter;

import java.io.*;
import java.util.ArrayList;

/*
 * Creates an arena by reading from a txt file, follwing this Syntax:
 * P = Spawnpoint
 * X = Wall
 * . = Floor
 * */
public class TxtArena extends Arena {


    private final String arenaPath = "src\\Assets\\Arenas";

    public static String selectedArena = "test1";

    //instantiates a new arena, parsing the tiles[][] array from a txt file "arena1"
    //TODO: User selectable Maps
    public TxtArena(){
        try {
            File file = new File(arenaPath + "\\" + selectedArena + ".txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
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
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    * parses input from the txt file to arena base encoding
    * P/p -> 2 (player spawn)
    * X/x -> 1 (wall)
    * . -> 0 (floor)
    * */
    private int parseType(char c){
        if(c == 'P' || c == 'p'){
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

    /*
    * Prints tiles array in console
    * FOR DEBUGGING ONLY
    * */
    private void printTiles(){
        for(int[] col : tiles){
            for(int row : col){
                System.out.print(row);
            }
            System.out.println();
        }
    }

}

package topdownshooter;

import java.io.*;
import java.util.ArrayList;

/*
 * Creates an arena by reading from a txt file, follwing this Syntax:
 * P = Spawnpoint
 * X = Wall
 * . = Floor
    //Items
 * H = Healing Orb
 * D = DMG
 * F = FireRate
 * s = Speed
 * */
public class TxtArena extends Arena {

    private File arena1;

    //sets the default arena to arena1
    public static String selectedArena  = "arena1";
    private final String arenaPath = GameManager.ASSETS_PATH + "Arenas\\";


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
        //Items
        if (c == 'D' || c == 'd') {
            return 3;
        }
        if (c == 'S' || c == 's') {
            return 4;
        }
        if (c == 'F' || c == 'f') {
            return 5;
        }
        if (c == 'H' || c == 'h') {
            return 6;
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

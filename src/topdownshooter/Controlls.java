package topdownshooter;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Controlls {
    public static int[] BUTTONS = new int[]{KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D};
    
    public Controlls (int[] keys) {
        
    }
    
    public static Point GetMousePos () {
        try {
            Point mp = MouseInfo.getPointerInfo().getLocation(), fp = GameManager.GM.GetWindow().getLocation();
            Point pos = new Point (mp.x - fp.x, mp.y - fp.y);
            return pos;
        } catch (NullPointerException e) {
            System.out.println(e);
            return new Point (0,0);
        }
    }
    
//    public static int SetMovingDirection (KeyEvent e) {
//        int dir = 0;
//        
//        return dir;
//    }
    
    public static enum Direction {
        //0-3 for player 1, 4-7 for player 2
        UP(0),DOWN(1),LEFT(2),RIGHT(3),UP2(4),DOWN2(5),LEFT2(6),RIGHT2(7);
//        public static final int[] DIR_BUTTONS = new int[] {KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT};
        int dir = 0, kCode = 0;
        
        Direction (int dir) {
            this.dir = dir % 4;
            kCode = BUTTONS[dir];
        }
        
        public int GetDir () {
            return dir;
        }
        
        public int GetKC () {
            return kCode;
        }
    }
}

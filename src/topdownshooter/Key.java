package topdownshooter;

import java.awt.event.KeyEvent;

public enum Key {
    /*Du hast zwar gesagt, du willst ein Controls Object an den Spieler hängen, 
    aber ich bin mir unsicher, wie genau du das umsetzen willst, deswegen setze 
    ich erstmal meinen ursprünglichen Plan um, bzw. hab diesen nochmal etwas geändert. wenn du etwas siest, was du ändern 
    willst kannst du das rhuig machen. */
    
    UP0(KeyEvent.VK_W),DOWN0(KeyEvent.VK_S),LEFT0(KeyEvent.VK_A),RIGHT0(KeyEvent.VK_D),
    UP1(KeyEvent.VK_UP),DOWN1(KeyEvent.VK_DOWN),LEFT1(KeyEvent.VK_LEFT),RIGHT1(KeyEvent.VK_RIGHT);
    
    private static int[] keyCodes = new int[Key.values().length];
    private int keyCode = 0; 
    
    Key (int keyCode) {
        this.keyCode = keyCode;
    }
    
    //This is called once at the start, to fill the array
    public static void InitializeKeyCodesArray () {
       for (int i = 0; i < keyCodes.length; i++) {
           keyCodes[i] = Key.values()[i].GetKeyCode();
       } 
    }
    
    //Returns the keyCode of this Key
    public int GetKeyCode () {
        return keyCode;
    }
    
    //Returns the keyCodes as an array
    public static int[] GetKeyCodes () {
        return keyCodes;
    }
    
    //Returns the Key for a certain keyCode
    public static Key GetKey (int keyCode) {
        for (Key k : Key.values()) {
            if (k.GetKeyCode() == keyCode)
                return k;
        }
        return null;
    }
}

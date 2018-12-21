package topdownshooter;

import java.awt.event.KeyEvent;

public enum Key {
    
    UP0(KeyEvent.VK_W),DOWN0(KeyEvent.VK_S),LEFT0(KeyEvent.VK_A),RIGHT0(KeyEvent.VK_D),
    UP1(KeyEvent.VK_UP),DOWN1(KeyEvent.VK_DOWN),LEFT1(KeyEvent.VK_LEFT),RIGHT1(KeyEvent.VK_RIGHT);
    
    private static int[] keyCodes = new int[Key.values().length];
    private int keyCode = 0; 
    
    Key (int keyCode) {
        this.keyCode = keyCode;
    }
    
    //This is called once at the start, to fill the array
    public static void initializeKeyCodesArray () {
       for (int i = 0; i < keyCodes.length; i++) {
           keyCodes[i] = Key.values()[i].getKeyCode();
       } 
    }
    
    //Returns the keyCode of this Key
    public int getKeyCode () {
        return keyCode;
    }
    
    //Returns the keyCodes as an array
    public static int[] getKeyCodes () {
        return keyCodes;
    }
    
    //Returns the Key for a certain keyCode
    public static Key getKey (int keyCode) {
        for (Key k : Key.values()) {
            if (k.getKeyCode() == keyCode)
                return k;
        }
        return null;
    }
}

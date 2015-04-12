/**
 * This is my first try to programm a console based snake clone
 * 
 * @copyright MIT 2.0
 * @author Selcuk Kekec <senycorp@googlemail.com>
 * @version 1.0
 */
package snake;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * SnakeKeyListener
 *
 * Listener for handling pressed keys
 *
 * @author Selcuk Kekec
 */
public class SnakeKeyListener implements NativeKeyListener {
    private ArrayList<Integer> keyMap;

    public SnakeKeyListener() {
        // Initialize key map
        this.keyMap = new ArrayList<>();
        this.keyMap.add(1111111);
        this.keyMap.add(SnakeConstants.MOVE_UP, SnakeConstants.KEYCODE_UP);
        this.keyMap.add(SnakeConstants.MOVE_DOWN, SnakeConstants.KEYCODE_DOWN);
        this.keyMap.add(SnakeConstants.MOVE_RIGHT, SnakeConstants.KEYCODE_RIGHT);
        this.keyMap.add(SnakeConstants.MOVE_LEFT, SnakeConstants.KEYCODE_LEFT);
    }
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        // Check if keycode is in map
        if (this.keyMap.contains(nke.getKeyCode())) {
            // Get move direction and set it
            int direction = this.keyMap.indexOf(nke.getKeyCode());
            
            Snake.setMoveDirection(direction);
        }
        
        // Unregister listener on ESCAPE
        if (nke.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
                
                System.exit(0);
            } catch (NativeHookException ex) {
                Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        // Not needed
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        // Not needed
    }

}

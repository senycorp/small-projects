/**
 * This is my first try to programm a console based snake clone
 *
 * @copyright MIT 2.0
 * @author Selcuk Kekec <senycorp@googlemail.com>
 * @version 1.0
 */
package snake;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * BaseClass
 *
 * @author Selcuk Kekec
 */
public class Snake {

    /**
     * The position of the head
     */
    private int[] headPos;

    /**
     * The position of the tail
     */
    private int[] tailPos;

    /**
     * The position of our star
     */
    private int[] starPos;

    /**
     * The playground
     */
    private int[][] playground;

    /**
     * Speed of game
     */
    private int speed;

    /**
     * Maximum x coordinate
     */
    private int maxX;

    /**
     * Maximum y coordinate
     */
    private int maxY;

    /**
     * Enable / Disable DebugMode
     */
    private boolean debugMode = false;

    /**
     * The current move direction of our snake
     */
    private static int direction;

    /**
     * Main
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create instance 
        Snake snakeGame = new Snake(30, 50, 100);
        
        snakeGame.run();
    }

    /**
     * Constructor with configuration options
     *
     * @param playgroundX
     * @param playgroundY
     */
    public Snake(int playgroundX, int playgroundY, int speed) {
        // Setting some options
        this.playground = new int[playgroundX][playgroundY];

        // Init some variables
        this.headPos = new int[2];
        this.tailPos = new int[2];
        this.starPos = new int[2];

        this.setHeadPos(3, 4);
        this.setTailPos(3, 2);

        
        // Set spped
        this.speed = speed;
        
        // Set max coordinates
        this.maxX = playgroundX;
        this.maxY = playgroundY;

        this.initialize();
    }

    /**
     * Set coords for head
     *
     * @param x
     * @param y
     */
    private void setHeadPos(int x, int y) {
        // Set x coord
        this.headPos[0] = x;

        // Set y coord
        this.headPos[1] = y;
    }

    /**
     * Set coords for tail
     *
     * @param x
     * @param y
     */
    private void setTailPos(int x, int y) {
        this.tailPos[0] = x;
        this.tailPos[1] = y;
    }

    /**
     * Run game and trigger some initialazition routines
     */
    public void run() {
        while (!this.isBorder(this.headPos[0], this.headPos[1])) {
            try {
                // Print playground
                this.printPlayground();
                
                // Move sname
                this.move();

                // Sleep
                Thread.sleep(this.speed);
            } catch (InterruptedException ex) {
                Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Game Over!");
        System.exit(0);
    }

    /**
     * Move snake to the wanted direction
     */
    public void move() {
        this.removeTailCoord();
        this.updateHeadCoord();
    }

    /**
     * Get value of coord
     * 
     * @param x
     * @param y
     * @return 
     */
    public int getCoord(int x, int y) {
        return this.playground[x][y];
    }

    public void removeTailCoord() {
        // Remove tail coord first
        this.removeCoord(this.tailPos[0], this.tailPos[1]);

        // Let us search for the next coord
        if (this.getCoord(this.tailPos[0] + 1, this.tailPos[1]) == 1) {
            this.tailPos[0]++;
        } else if (this.getCoord(this.tailPos[0] - 1, this.tailPos[1]) == 1) {
            this.tailPos[0]--;
        } else if (this.getCoord(this.tailPos[0], this.tailPos[1] + 1) == 1) {
            this.tailPos[1]++;
        } else if (this.getCoord(this.tailPos[0], this.tailPos[1] - 1) == 1) {
            this.tailPos[1]--;
        }

    }
    
    /**
     * Update head coordinate
     */
    public void updateHeadCoord() {
        switch (Snake.direction) {
            case SnakeConstants.MOVE_UP:
                // Increment head coord and set it on playground
                this.headPos[0]--;
                break;
            case SnakeConstants.MOVE_DOWN:
                this.headPos[0]++;
                break;

            case SnakeConstants.MOVE_LEFT:
                this.headPos[1]--;
                break;
            default:
            case SnakeConstants.MOVE_RIGHT:
                this.headPos[1]++;
                break;
        }

        // Check if snake touches itself
        if (this.getCoord(this.headPos[0], this.headPos[1]) == SnakeConstants.SNAKE_BODY) {
            System.out.println("Game Over!");
            System.exit(0);
        }
        
        // Check if snake touches star
        if (this.getCoord(this.headPos[0], this.headPos[1]) == SnakeConstants.STAR) {
            this.updateStar();
            this.setCord(this.headPos[0], this.headPos[1]);
            this.updateHeadCoord();
        }
        
        this.setCord(this.headPos[0], this.headPos[1]);
    }
    
    /**
     * Remove coord
     * 
     * @param x
     * @param y 
     */
    public void removeCoord(int x, int y) {
        this.playground[x][y] = 0;
    }

    /**
     * Initialize
     */
    public void initialize() {
        // Set current coords of snake
        this.setCord(3, 2);
        this.setCord(3, 3);
        this.setCord(3, 4);

        // Set star
        this.updateStar();

        // Set move direction
        Snake.setMoveDirection(SnakeConstants.MOVE_RIGHT);

        // Setup key listener
        this.setupKeyListener();
    }
    
    /**
     * Update coords to a free position
     */
    public void updateStar() {
        // Set star
        int[] freeCoords = this.freePosition();
        this.setStar(freeCoords[0], freeCoords[1]);
    }

    /**
     * Set star coords in position and playground
     * 
     * @param x
     * @param y 
     */
    public void setStar(int x, int y) {
        // Set star position
        this.starPos[0] = x;
        this.starPos[1] = y;

        // Update it on playground
        this.playground[x][y] = 2;
    }

    /**
     * Get coords of a free position on playground
     * 
     * @return 
     */
    public int[] freePosition() {
        int[] coords = new int[2];
        int x = 0;
        int y = 0;

        do {
            x = Snake.randInt(1, this.maxX-2);
            y = Snake.randInt(1, this.maxY-2);
        } while (!this.isFreePosition(x, y));

        coords[0] = x;
        coords[1] = y;

        return coords;
    }

    /**
     * Checks whether position is free
     * or not.
     * 
     * @param x
     * @param y
     * @return 
     */
    public boolean isFreePosition(int x, int y) {
        return (this.playground[x][y] == 0);
    }

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt(max - min + 1) + min;

        return randomNum;
    }

    /**
     * Setup the key listener to react on keyboard events
     * 
     */
    public void setupKeyListener() {
        // Instantiate KeyListener
        SnakeKeyListener SnakeKeyListener = new SnakeKeyListener();

        try {
            // Get the logger for "org.jnativehook" and set the level to off.
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            // Try to register listener hook
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        // Add Listener to scope
        GlobalScreen.addNativeKeyListener(SnakeKeyListener);
    }

    /**
     * Set move direction of our snake
     *
     * @param direction
     */
    public static void setMoveDirection(int direction) {
        Snake.direction = direction;
    }

    /**
     * Set coordinate in playground
     *
     * @param x
     * @param y
     */
    public void setCord(int x, int y) {
        this.playground[x][y] = 1;
    }

    /**
     * Prints the visual representation of an 2-dimensional integer array
     */
    public void printPlayground() {
        print("Executing printPlayground");

        // This line cleans the whole terminal output. Found it at 
        // http://www.reddit.com/r/java/comments/1uuvqo/clear_terminal_screen_linux/
        System.out.print("\033[H\033[2J");

        // Set coords
        int x = 0;
        int y = 0;

        String playgroundLine = "";

        for (x = 0; x < this.playground.length; x++) {
            y = 0;

            for (y = 0; y < this.playground[x].length; y++) {
                print("Current cord: [" + x + "][" + y + "]");

                if (this.playground[x][y] == 1) {
                    playgroundLine += "*";
                } else if (this.playground[x][y] == 2) {
                    playgroundLine = playgroundLine + "+";
                } else if (this.isBorder(x, y)) {
                    playgroundLine = playgroundLine + "*";
                    print("Border coordinate detected print");
                } else {
                    playgroundLine = playgroundLine + " ";
                }
            }

            System.out.println(playgroundLine);
            playgroundLine = "";
        }
    }

    /**
     * Check for border coordinate
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isBorder(int x, int y) {
        return (x == 0 || x == (this.maxX - 1)) || (y == 0 || y == (this.maxY - 1));
    }

    /**
     * Method for debugging purpose
     *
     * @param msg
     */
    public void print(String msg) {
        if (this.debugMode) {
            System.out.println(msg);
        }
    }
}

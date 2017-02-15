import java.util.Random;

/**
 * Created by Wenwen Yang on 5/23/15.
 */

public class Arrow extends Block{
	
    private final int BOARD_HEIGHT = 400;
    private int speed = 1;
    private Random random = new Random();

    public Arrow(int x, int y, int speed){
    	super(x,y);

        if (random.nextInt(50) % 2 == 0){
        	this.y = 0;
        	this.speed = -speed;
            loadImage("image/arrowdown.png");

        }
        else {
			this.speed = speed;
	        loadImage("image/arrow.png");
		}
        
        getImageDimensions();
    }

    public int getSpeed() {
        return speed;
    }
    
    protected void move() {
        y -= speed;
        /* need to handle different direction */
        if (y < 0 || y > BOARD_HEIGHT)
            vis = false;
        }
    
}



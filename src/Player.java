import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Wenwen Yang on 5/23/15.
 * 
 * - add field finalScore to record score after success.
 */
public class Player extends Block{
    private int dx;
    private int dy;
    private int score;
    private int level;
    private int finalScore = 0;
    private int life;
    private final int MOVESPEED = 8;
    private ArrayList<Missile> missiles;
    private boolean isLarge;
    private Image largeImage;
    private int bombs;

    public Player(int x, int y, int score, int level){
        super(x, y);
        initPlayer();
        this.score = score;
        this.level = level;
        this.life = 3;
        this.bombs = 2;

    }

    private void initPlayer() {

        missiles = new ArrayList<Missile>();
        loadImage("image/player.png");
        getImageDimensions();
        ImageIcon ii = new ImageIcon("image/bigfrog.png");
        largeImage = ii.getImage();
    }
    
    protected int getBombs() {
	    return bombs;
	}

	protected void setBombs(int bombs) {
	    this.bombs = bombs;
	}



	protected int getScore() {
        return score;
    }

    protected void setScore(int score) {
        this.score = score;
    }

    protected void setFinalScore(int score){
    	this.finalScore = score;
    }
    
    protected int getFinalScore(){
    	return finalScore;
    }
    
    protected int getLevel() {
        return level;
    }

    protected void setLevel(int level) {
        this.level = level;
    }
    
    
    protected void setLifes(int life){
    	this.life = life;
    }
    
    protected int getLifes(){
    	return life;
    }
    
    protected int addLifes(int i){
    	life += i;
    	System.out.println(life);
    	return life;
    }

    protected void setSize(boolean size) {
        this.isLarge = size;
    }

    protected boolean isLarge() {
        return this.isLarge;
    }

    protected Image getLargeImage() {
        return largeImage;
    }

    protected Rectangle getLargeBounds() {
        return new Rectangle(x, y, largeImage.getWidth(null), largeImage.getHeight(null));
    }
    protected void move() {
        x += dx;
        y += dy;

        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }
    }


    protected ArrayList getMissiles() {
        return missiles;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_CONTROL) {
            fire();
        }

        if (key == KeyEvent.VK_LEFT) {
            dx = -MOVESPEED;
            score -= 10;
            if (score < 0) {
                score = 0;
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = MOVESPEED;
            score += 5;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -5;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 5;
        }
    }

    protected void fire() {
        missiles.add(new Missile(x + width, y + height / 2));
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}








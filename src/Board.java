import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.Timer;
/**
 * 
 * 6/1/15 Wenwen Yang
 * - Add restart(), nextLevel()
 * - change TAdapter class
 * - change actionPerformed, checkSuccess
 * 
 * 6/4/15 Wenwen Yang
 * - Add lifes, reset game.
 * - reset arrow
 * 
 */
public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private ArrayList<Arrow> arrows;
    private ArrayList<Mushroom> mushrooms;
    private int arrowCount = 0;
    private int arrowLimit;
    private boolean ingame;
    private boolean success;
    private long lastArrowUpdate;
    private long lastMushroomUpdate;
    private final int ICRAFT_X = 20;
    private final int ICRAFT_Y = 200;
    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 400;
    private final int DELAY = 20;
    private int[] arrowSpeed = {3, 5, 7, 10, 12, 15};
    private boolean largeFrog = false;



    public Board() {

        initBoard();
    }
    
    /* Init the panel board */
    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        ingame = true;
        success = false;
        arrowLimit = 10;
        lastArrowUpdate = System.currentTimeMillis();
        lastMushroomUpdate = System.currentTimeMillis();
        arrows = new ArrayList();
        mushrooms = new ArrayList();

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        player = new Player(ICRAFT_X, ICRAFT_Y, 0, 1);

        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    /* Paint component depends on what happened */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ingame) {
            if (!success) {
                drawObjects(g);
            } else {
                drawSuccess(g);
            }
        } else {
            drawDie(g);
        }


        Toolkit.getDefaultToolkit().sync();
    }

    /* Draw if not game over */
    private void drawObjects(Graphics g) {

        if (player.isVisible()) {
            if (player.isLarge()) {
                g.drawImage(player.getLargeImage(), player.getX(), player.getY(), this);
            } else {
                g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            }
        }

        ArrayList<Missile> ms = player.getMissiles();
        Lifes lifes = new Lifes(70, 18);
        for (int i = 0; i < player.getLifes(); i++) {
            g.drawImage(lifes.getImage(), lifes.getX() + 25*i, lifes.getY(), this);
        }

        Bombs bombs = new Bombs(730, 4);
        for (int i = 0; i < player.getBombs(); i++) {
            g.drawImage(bombs.getImage(), bombs.getX() + 25*i, bombs.getY(), this);
        }

        for (Missile m : ms) {
            if (m.isVisible()) {
                g.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
        }


        for (Arrow arrow : arrows) {
            if (arrow.isVisible()) {
                g.drawImage(arrow.getImage(), arrow.getX(), arrow.getY(), this);
            }
        }

        if (!largeFrog) {
            for (Mushroom mushroom : mushrooms) {
                if (mushroom.isVisible()) {
                    g.drawImage(mushroom.getImage(), mushroom.getX(), mushroom.getY(), this);
                }
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Current sore: " + player.getScore(), 5, 15);
        g.drawString("Life Left: ", 5, 35);
        g.drawString("Bombs: ", 680, 15);

    }

    /* Draw if game over */
    private void drawDie(Graphics g) {
    	String msg;
    	if(player.getLifes() > 0){
    		msg = "You have " + player.getLifes() + " lifes left. Press space to retry.";
    	} else {
    		msg = "Game Over. Press space to reset game.";
    	}
        Font small = new Font("Serif", Font.BOLD, 16);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.pink);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    /* Draw if pass the level */
    private void drawSuccess(Graphics g) {

        String msg = "You passed Level " + player.getLevel();
        if (player.getLevel() == arrowSpeed.length )
        	msg = "Congragulations! You finished the game!";
        Font small = new Font("Serif", Font.BOLD, 16);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.pink);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    /* Action performed override */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (System.currentTimeMillis() - lastMushroomUpdate > 400 && mushrooms.size() < 3) {
            Random generator = new Random();
            mushrooms.add(new Mushroom(generator.nextInt(300) + 100, B_HEIGHT));
            lastMushroomUpdate = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastArrowUpdate > 100) {
            generateArrow(arrowSpeed[player.getLevel() - 1]);
            lastArrowUpdate = System.currentTimeMillis();
        }
        inGame();

        updatePlayer();
        updateMissiles();
        updateArrowsAndMushroom();
        checkDeathAndChange();
        checkSuccess();

        repaint();
    }
    
    /* init Arrow */
    private void generateArrow(int speed) {
        if (arrowCount < arrowLimit) {
        	//System.out.println("generate new arrow");
            Random generator = new Random();
            int index = generator.nextInt(20);
            int x = index * (B_WIDTH - 100)/20 + 100;
            Arrow arrow = new Arrow(x, B_HEIGHT, speed);
            arrows.add(arrow);
            arrowCount++;
        }
    }

    /* Check if still in game */
    private void inGame() {
        if (!ingame) {
            timer.stop();
        }
    }

    /* Update player position */
    private void updatePlayer() {
        if (player.isVisible()) {
            player.move();
        }
    }

    private void updateMissiles() {

        ArrayList<Missile> ms = player.getMissiles();
        for (int i = 0; i < ms.size(); i++) {

            Missile m = ms.get(i);

            if (m.isVisible()) {
                m.move();
            } else {
                ms.remove(i);
            }
        }
    }

    /* Update all arrow position */
    private void updateArrowsAndMushroom() {
        for (int i = 0; i < arrows.size(); i++) {
            Arrow tmp = arrows.get(i);
            if (tmp.isVisible()) {
                tmp.move();
            } else {
                arrows.remove(tmp);
                arrowCount--;
            }
        }
        for (int i = 0; i < mushrooms.size(); i++) {
            Mushroom mushroom = mushrooms.get(i);
            if (mushroom.isVisible()) {
                mushroom.move();
            } else {
                mushrooms.remove(mushroom);
            }
        }
    }




    /* Check if collision */
    private void checkDeathAndChange() {
        Rectangle rO = player.getBounds();
        for (Mushroom mushroom : mushrooms) {
            Rectangle rM = mushroom.getBounds();
            if (rO.intersects(rM)) {
                player.setSize(true);
                mushroom.setVisible(false);
                largeFrog = true;
            }
        }

        Rectangle r3 = null;
        if (player.isLarge()) {
            r3 = player.getLargeBounds();
        } else {
            r3 = player.getBounds();
        }

        for (Arrow arrow : arrows) {
            Rectangle r2 = arrow.getBounds();
            if (r3.intersects(r2)) {
                player.setVisible(false);
                arrow.setVisible(false);
                ingame = false;
                player.addLifes(-1); // life decrease
            }
        }
        ArrayList<Missile> ms = player.getMissiles();

        for (Missile m : ms) {
            Rectangle r1 = m.getBounds();
            for (Arrow arrow : arrows) {
                Rectangle r2 = arrow.getBounds();

                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    arrow.setVisible(false);
                }
            }
        }
        
    }


    /* check if pass level */
    private void checkSuccess() {
        if (player.getX() >= B_WIDTH) {
            success = true;
            player.setFinalScore(player.getScore());
        }
    }

    /* restart either level */
    private void restartLevel(){
    	player.setScore(player.getFinalScore());
        player.setSize(false);
        largeFrog = false;
    	resetVariable();
        player.setBombs(2);
    }
    
    /* restart game, reset all variable to init */
    private void restartGame(){
    	player.setScore(0);
    	player.setLevel(1);
    	player.setLifes(3);
        player.setSize(false);
        largeFrog = false;
    	resetVariable();
    }
    
    /* advance to next level */
    private void nextLevel(){
    	System.out.println(player.getLevel());
    	if (player.getLevel() < arrowSpeed.length ) {
    		// increase level and add arrow limit
	    	player.setLevel(player.getLevel() + 1);
	    	arrowLimit += 5;
	    	resetVariable();
    	}
    }
    
    /* reset setting each level (when pressing space) */
    private void resetVariable(){
    	ingame = true;
    	success = false;
    	player.setVisible(true);
    	player.setX(ICRAFT_X);
    	player.setY(ICRAFT_Y);
        player.setSize(false);
        largeFrog = false;
    	arrows.removeAll(arrows);
    	arrowCount = 0;
    	timer.start();
    }
    
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
        	// if press space
        	if (e.getKeyCode() == KeyEvent.VK_SPACE){
        		if (success == false)				//check if pass level
        			if( player.getLifes() == 0)		//check if life hit zero
        				restartGame();
        			else
        				restartLevel();
        		else{
        			nextLevel();
        		}
        	} else {
            player.keyPressed(e);
        	}

            if (e.getKeyCode() == KeyEvent.VK_B && player.getBombs() > 0) {
                arrows = new ArrayList();
                mushrooms = new ArrayList();
                arrowCount = 0;
                player.setBombs(player.getBombs()-1);
            }
        }
    }
}
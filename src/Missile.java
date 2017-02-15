/**
 * Created by Wenwen Yang on 6/5/15.
 */
public class Missile extends Block {

    private final int BOARD_WIDTH = 700;
    private final int MISSILE_SPEED =4;

    public Missile(int x, int y) {
        super(x, y);

        initMissile();
    }

    private void initMissile() {

        loadImage("image/missile.png");
        getImageDimensions();
    }

    protected void move() {

        x += MISSILE_SPEED;

        if (x > BOARD_WIDTH)
            vis = false;
    }
}
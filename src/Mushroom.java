/**
 * Created by Wenwen Yang on 6/4/15.
 */
public class Mushroom extends Block {
    Mushroom(int x, int y) {
        super(x, y);
        loadImage("image/mushroom.png");
        getImageDimensions();
    }

    protected void move() {
        y -= 2;
        if (y < 0)
            vis = false;
    }
}

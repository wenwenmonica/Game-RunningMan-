/**
 * Created by Wenwen Yang on 6/5/15.
 */
public class Lifes extends Block {
	
	public Lifes(int x, int y){
		super(x, y);
		initLifes();
	}
	
	private void initLifes(){
        loadImage("image/smallfog.png");
        getImageDimensions();
	}
}

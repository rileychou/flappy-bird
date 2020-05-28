import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Image;

/**
 * MovingPlatform that bounces off the side walls.  It bases the sides in the
 * following way:
 * 1.  The left side has an x-coord of 0 
 * 2.  The right wall has an x-coord of GameFrame.WIDTH
 * 
 * If you wish to have another definition, you might what to 
 * add another constructor that passes in the left and right walls
 * 
 * @author RHanson
 *
 */
public class HorizontalBouncingMovingPlatform extends MovingPlatform {

	private final int LEFT = 0, RIGHT = GameFrame.WIDTH;
	
	public HorizontalBouncingMovingPlatform(int x, int y, int w, int h, Color c) {
		super(x, y, w, h, c);
		// TODO Auto-generated constructor stub
	}

	public HorizontalBouncingMovingPlatform(int x, int y, int w, int h, Color c, int dx) {
		super(x, y, w, h, c, dx);
		// TODO Auto-generated constructor stub
	}

	public HorizontalBouncingMovingPlatform(int x, int y, int w, int h, Image i, int dx){
		super(x, y, w, h, i, dx);
	}

	@Override
	public void move() {
		Rectangle r = this.getRect();
		if (r.x+r.width > RIGHT) {
			r.x = RIGHT-r.width;
			this.setDx(getDx()*-1);
		}
		super.move();
	}
}

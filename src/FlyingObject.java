import java.awt.*;

public class FlyingObject extends GameObject {
	public static final double GRAVITY = 30;
	private boolean inAir = false;
	private double timeInAir = 0;
	private double dyAtLaunch;


	public FlyingObject(int x, int y, int w, int h, Color c) {
		this(x, y, w, h, c, 0, 0);
	}

	public FlyingObject(int x, int y, int w, int h, Color c, int dx, int dy) {
		super(x, y, w, h, c, dx, dy);
	}

	public FlyingObject(int x, int y, int w, int h, Image i , int dx, int dy){ super(x, y, w, h, i, dx, dy);};
	@Override
	public void moveY() {
		if(this.inAir) {
			timeInAir+=GameFrame.REFRESH*1.0/1000;
			this.setDy(dyAtLaunch+ this.GRAVITY*timeInAir);
		}

		super.moveY();
		
	}
	/**
	 * Called when this object is launched into the air. 
	 * What can we add so that the Object can't launch if already in the 
	 * air?  Some Objects act like this.  For instance, a rock can only
	 * fly (launch) once.  A bird can launch whenever by flapping wings.
	 */
	public void launch() {
		dyAtLaunch = this.getDy();
		this.inAir = true;
		timeInAir = 0;
	}
}

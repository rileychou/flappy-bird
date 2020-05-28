import java.awt.Color;
import java.awt.Image;

/**
 * Game Object that is rectangular (for now) that can move (or not)
 * Interesting use of a constructor that calls another constructor.
 * Just showing how it should be done.
 * @author RHanson
 *
 */
public class MovingPlatform extends GameObject {

	public MovingPlatform(int x, int y, int w, int h, Color c) {
		this(x, y, w, h, c,0);
	}
	public MovingPlatform(int x, int y, int w, int h, Color c, int dx) {
		this(x, y, w, h, c, dx, 0);
	}
	public MovingPlatform(int x, int y, int w, int h, Color c, int dx, int dy) {
		super(x, y, w, h, c, dx, dy);
	}
	public MovingPlatform(int x, int y, int w, int h, Image i, int dx){ super(x, y, w, h, i, dx, 0);}
}

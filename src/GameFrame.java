import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GameFrame extends JFrame {
	BufferedImage background;
	{
		try {
			background = ImageIO.read(new File("Pictures/Png.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage flappy;
	{
		try {
			flappy = ImageIO.read(new File("Pictures/flappy.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private static BufferedImage topP;
	{
		try {
			topP = ImageIO.read(new File("Pictures/topPipe.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static BufferedImage botP;
	{
		try{
			botP = ImageIO.read(new File("Pictures/botPipe.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private int ticks = 0;
	private int pCount = 0;
	private int score = 0;
	private int topScore = 0;
	FlyingObject flyer = new FlyingObject(300, 400, 50,60, flappy, 0,0);

	ArrayList<GameObject> objects = new ArrayList<>();
	ArrayList<HorizontalBouncingMovingPlatform> tp = new ArrayList<>();
	ArrayList<HorizontalBouncingMovingPlatform> bp = new ArrayList<>();

	// starting dimensions of window (pixels)
	public static final int WIDTH = 1000, HEIGHT = 800, REFRESH = 40;

	// where the game objects are displayed
	private JPanel panel = new JPanel() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawTheGame(g);
			// tried to get rid of some stuttering, changing REFRESH 
			// improved this issue
			panel.getToolkit().sync();
		}
	};
	private Timer timer;//timer that runs the game

	private JLabel scoreboard = new JLabel("Score: 0");
	private JLabel highScore = new JLabel("Highest score: "+topScore);
	private JButton restart = new JButton("Restart");

	public GameFrame(String string) {
		super(string);
		setUpStuff();
	}

	/**
	 * Sets up the panel, timer, other initial objects in the game
	 */
	private void setUpStuff() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		scoreboard.setBounds(0, 0, 100, 60);
		scoreboard.setFont(new Font("Arial", Font.PLAIN, 18));
		highScore.setBounds(100, 0, 100, 60);
		highScore.setFont(new Font("Arial", Font.PLAIN, 18));
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==restart){
					restart();
				}
			}
		});
		this.add(panel);
		this.pack();

		timer = new Timer(REFRESH, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ticks%40==0){
					int gapShift = 125-((int)(Math.random()*250));
					tp.add(new HorizontalBouncingMovingPlatform(1000, 0, 50, 275-gapShift, topP, 10));
					bp.add(new HorizontalBouncingMovingPlatform(1000, HEIGHT - (275+gapShift), 50, 275+gapShift, botP, 10));
					objects.add(tp.get(pCount));
					objects.add(bp.get(pCount));
					pCount++;
				}
				updateGame();
				panel.repaint();
				ticks++;
			}
		});
		timer.start();
		panel.add(scoreboard);
		panel.add(highScore);
		panel.add(restart);
		this.setVisible(true);
		restart.setVisible(false);
		panel.requestFocusInWindow();
		addKeys(panel);
		objects.add(flyer);
	}

	/**
	 * Proper way to acquire keystrokes in an application.  
	 * This method sets up the mapping which associates a Keystroke (you
	 * can Google Java KeyStroke API or examples) with an "action command" String 
	 * 
	 * The second part maps the action command String with an Action.
	 * I have shown you two ways this can be done.  You can write the code
	 * you want executed in the actionPerformed method or you can call the
	 * method from within the actionPerformed method call.
	 */
	private void addKeys(JPanel panel) {
		// this connects keystroke with a command
		panel.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "space");
		panel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rt_key");
		panel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "lt_key");
		panel.getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "space_r");
		panel.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "rt_key_r");
		panel.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "lt_key_r");
		
		
		panel.getActionMap().put("lt_key_r", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keysReleased();
			}
		});
		panel.getActionMap().put("rt_key_r", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keysReleased();
			}
		});
		panel.getActionMap().put("space_r", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("released the space bar");
			}
		});

		panel.getActionMap().put("space", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				upHit();
			}
		});
		panel.getActionMap().put("rt_key",new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rtHit();
			}

		});
		panel.getActionMap().put("lt_key",new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ltHit();
			}

		});
	}

	/** called when the left arrow is pressed.  Probably move something
	 *  to the left, or turn left or... */
	private void ltHit() {
		//flyer.setDx(-20);
	}
	
	private void keysReleased() {
		flyer.setDx(0);
	}

	/** called when the right arrow is pressed.  Probably move something
	 *  to the right, or turn right or... */
	private void rtHit() {
		//flyer.setDx(20);
	}
	
	/** this sets go's dy to -20 (basically setting its initial velocity to 
	 *  -20).  If you want it to go slower, set it to a smaller number.
	 *  I set it to a negative so that the y-coord will DECREASE making it go 
	 *  up (remember, smaller y value means higher on the panel).
	 *  I then call launch.  The object can be launched many times during its
	 *  "flight".  Some objects should only be launchable once, though.  This
	 *  ability to launch whenever is much like a bird flying.  You can change 
	 *  this behavior in the FlyingObject class, or, even better, create a 
	 *  subclass of FlyingObject (like Rock or Bird or Rocket or Spell or...)
	 */
	
	private void upHit() {
		flyer.setDy(-20);
		flyer.launch();
	}

	/**
	 * This is called every time the Timer goes off.  Right now, it moves all 
	 * the Objects and checks for collisions.  This is common in games with flying
	 * Objects.  You can do more, though.  Like add items or move to new screens
	 * or check to see if the turn is over or...
	 */
	protected void updateGame() {
		//System.out.println("Timer went off "+prints++);
		moveObjects();
		checkCollisions();
		for (int i = 0; i<tp.size(); i++){
			if (flyer.getRect().x==tp.get(i).getRect().x-tp.get(i).getRect().width){
				scored();
			}
		}
	}

	/**
	 * Right now I am checking for collisions between flyer
	 */
	private void checkCollisions() {
		// just for starters...
		for (HorizontalBouncingMovingPlatform t: tp){
			if(flyer.collidedWith(t)) {
				restart.setVisible(true);
				timer.stop();
			}
		}
		for (HorizontalBouncingMovingPlatform b: bp){
			if(flyer.collidedWith(b)) {
				restart.setVisible(true);
				timer.stop();
			}
		}

		if(flyer.getRect().y<=0 || flyer.getRect().y>=HEIGHT-flyer.getRect().getHeight()){
			restart.setVisible(true);
			timer.stop();
		}
	}

	/**
	 * get it...
	 */
	private void moveObjects() {
		for(GameObject go: objects)
			go.move();
	}

	private void scored(){
		score++;
		scoreboard.setText("Score: " + score);
	}


	/**
	 * Draws all the stuff in the game without changing them
	 * No reason to change this unless you wanted a background
	 * or something.
	 * @param g
	 */
	private void drawTheGame(Graphics g) {
		g.drawImage(background,0, 0, getWidth(), getHeight(), null);
		for(GameObject go:this.objects) {
				go.draw(g);
		}
	}

	public void trackHighScore(){
		if (score>topScore){
			topScore=score;
		}
	}

	public void endGame(){
		trackHighScore();
		score=0;//score of the player
		pCount = 0;//amount of pillars
		ticks=0;//incremented every time timer goes off
		scoreboard.setText("Score: " + score);
		highScore.setText("High Score: " + topScore);
		objects.clear();
		tp.clear();//arraylist to store the top pillars so the new ones i make are not under the same variable name
		bp.clear();//this is for bottom pillars, same logic as above, these helped solve all the issues with colliding
		timer.stop();
	}
	/**
	 * restarts game
	 */
	public void restart(){
		endGame();
		setUpStuff();
	}
}

import javax.swing.*;
//imports swing for the graphics

public class SwingGraphicsGame {

	    public static void main(String[] args) {

	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                GameFrame wnd = new GameFrame("Game on!");
	                
	                wnd.setVisible(true);
	                
	            }
	        });
	    }
	}
package mineSweeper.main;

import javax.swing.JFrame;

import mineSweeper.model.Game;
import mineSweeper.view.MineSweeperView;

/**
 * this is the Main for the MineSweeper Game in the mineSweeper package
 * it creates all the important objects to start the game
 * it gets the size x and size y and the amount of bombs as inputs strings
 * 
 * @author Eosandra Grund {egrund@uni-osnabrueck.de}
 */

public class MineSweeper {

	public static void main(String[] args) {
		
		// process input
		int[] size = new int[2];
		size[0] = Integer.valueOf(args[0]);
		size[1] = Integer.valueOf(args[1]);
		int number_bombs;
		try {
			number_bombs = Integer.valueOf(args[2]);
		}catch(ArrayIndexOutOfBoundsException e) { // if no input for number of bombs
			number_bombs = (int) size[0]*size[1] / 10;
		}
		
		
		// check if enough space for the bombs
		if(number_bombs >= size[0] * size[1]) {
			System.err.println("These many bombs do not fit in the field.");
			System.exit(-1);
		}
		
		// create all objects
		Game model = new Game(size, number_bombs);
		JFrame frame = new JFrame("Mine Sweeper     Size: " + size[0]+"x"+size[1] + "     Bombs: " + number_bombs);
		MineSweeperView view = new MineSweeperView(model, frame);
		frame.setContentPane(view);
		
		// so the game can stop if it is done
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// so the window has the right size from the beginning
		frame.pack();
		frame.setVisible(true);
	}
}

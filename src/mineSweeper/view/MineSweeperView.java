package mineSweeper.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mineSweeper.controller.MineSweeperController;
import mineSweeper.model.Game;

/**
 * The creates the graphics for the mine sweeper game by using swing
 * 
 * @author Eosandra Grund {egrund@uni-osnabrueck.de}
 */

public class MineSweeperView extends JPanel{

	private static final long serialVersionUID = 1L;
	/**
	 * The buttons creating the graphics, one for each field of the game
	 */
	private JButton[][] buttons;
	/**
	 * the game for which to create the graphics
	 */
	private Game model;
	
	public MineSweeperView(Game model, JFrame window) {
		this.model = model;
		this.model.attachListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				// check whether game ends, and create a little window with the message on it
				// when closing the small window also close the game one
				if(model.gameWon()) {
					// send message
					JOptionPane.showMessageDialog(null,"You Won");
					window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
					
				}else if(model.gameLost()) {
					JOptionPane.showMessageDialog(null,"You Lost");
					window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				}
				
				int[] size = model.getSize();
				String[][] labels = model.getLabels();
				

				for(int x = 0; x<size[0]; x++) {
					for(int y = 0; y<size[1]; y++) {
						this.changeButton(x,y,labels[x][y]);
					}
				}			
			}
			
			/**
			 * changes the label of button x,y to label and checks whether the button should be enabled or not
			 * @param x
			 * @param y
			 * @param label
			 */
			private void changeButton(int x,int y, String label) {
				buttons[x][y].setText(label);
				
				// enable already uncovered buttons
				if(label != " " && label != "!" && label != "") {
					buttons[x][y].setEnabled(false);
				}
			}
			
		});
		
		int[] size = this.model.getSize();
		
		this.buttons = new JButton[size[0]][size[1]];
		
		this.setLayout(new GridLayout(size[0],size[1]));
		
		// add all components to the window
		for(int x = 0; x<size[0]; x++) {
			for(int y = 0; y<size[1]; y++) {
				JButton button = new JButton("");
				button.setPreferredSize(new Dimension(50,50)); // starting size of the buttons
				button.setEnabled(true); 
				button.addMouseListener(new MineSweeperController(this.model,x,y)); // so something happens if you click on it
				this.add(button);
				this.buttons[x][y] = button;
			}
		}
	}

}

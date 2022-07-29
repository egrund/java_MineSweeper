package mineSweeper.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * this represents one MineSweeper Game
 * it takes care of all the fields, bombs and if the game was won or lost
 * 
 * @author Eosandra Grund {egrund@uni-osnabrueck.de}
 */

public class Game {
	
	private Field[][] field;
	private int[] size;
	
	/**
	 * the number of uncovered fields in the field array
	 */
	private int number_uncovered = 0;
	private int number_bombs;
	private boolean won = false;
	private boolean lost = false;
	
	/**
	 * the value fields should have when having a bomb on them
	 */
	private final int BOMBS_VALUE = -9;
	
	/**
	 * the PropertyChangeSupport to notify the listener (view) if there are changes
	 */
	private PropertyChangeSupport changed;
	
	public Game(int[] size, int number_bombs) {
		
		this.field = new Field[size[0]][size[1]];
		this.size = size;
		this.number_bombs = number_bombs;
		this.changed = new PropertyChangeSupport(this);
		
		// create empty fields
		for(int x = 0; x<size[0]; x++) {
			for(int y = 0; y<size[1];y++) {
				this.field[x][y] = new Field(0);
			}
		}
		
		int[][] bombs = new int[this.number_bombs][2];
		
		// create random bombs
		for(int b = 0; b<this.number_bombs;b++) {
			boolean new_coordinate = true;
			do { // make sure only one bomb in one field
				new_coordinate = true;
				bombs[b][0] = (int) (Math.random() * (this.size[0] ));
				bombs[b][1] = (int) (Math.random() * (this.size[1] ));
				
				for(int b2 = 0; b2<b;b2++) {
					if(bombs[b][0] == bombs[b2][0] && bombs[b][1] == bombs[b2][1]) {
						new_coordinate = false;
					}
				}
				
			}while(!new_coordinate);
			
			
			// put bombs in fields
			if(this.isValid(bombs[b][0], bombs[b][1])) {
				this.field[bombs[b][0]][bombs[b][1]].setValue(this.BOMBS_VALUE);
			}
			
			// calculate neighboring fields' values
			for(int x = bombs[b][0] -1; x<= bombs[b][0] +1; x++) {
				for(int y = bombs[b][1] -1; y <= bombs[b][1] +1; y++) {
					if(this.isValid(x, y) && ! this.isBomb(x, y)) { 
						this.field[x][y].addOne();
					}
				}
			}		
		}	
		this.notifyListeners(); // change view
	}
	
	public int[] getSize() {
		return this.size;
	}
	
	/**
	 * returns whether in field (x,y) is a bomb
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if bomb, else false
	 */
	private boolean isBomb(int x, int y) {
		if(this.field[x][y].getValue() == this.BOMBS_VALUE) {
			return true;
		}
		return false;
	}
	
	/**
	 * returns whether coordinates x,y are inside of the field
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if in field, false else
	 */
	private boolean isValid(int x, int y) {
		if (x<0 || y<0 || x>= this.size[0] || y>= this.size[1]) {
			return false;
		}
		return true;
	}
	
	/**
	 * returns whether all fields that are not bombs are uncovered
	 * @return true if all uncovered else false
	 */
	private boolean allUncovered() {
		if (this.number_uncovered >= this.size[0] * this.size[1] - this.number_bombs ) {
			return true;
		}
		return false;
	}
	
	public boolean gameLost() {
		return this.lost;
	}
	
	public boolean gameWon() {
		return this.won;
	}
	
	/**
	 * marker the field x,y
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void marker(int x, int y) {
		this.field[x][y].mark();
		
		this.notifyListeners();
	}
	
	/**
	 * uncovers field in coordinate x,y
	 * @param x x coordinate of field to uncover
	 * @param y y coordinate of field to uncover
	 * @param active whether this field was clicked on to uncover or is passively uncovered by a neighboring field through recursion
	 */
	public void uncover(int x, int y, boolean active) {

		int value = this.field[x][y].getValue();
		boolean worked = false; // if you could uncover the field (not already uncovered or marked)

		if(value != this.BOMBS_VALUE) {
			
			worked = this.field[x][y].uncover();
			this.notifyListeners();
			
			if(worked) {
				this.number_uncovered++;
				if(this.allUncovered()){
					this.won = true;
				}
				
			}
		// bomb and passive
		// just to be sure, because normally only neighbors of 0 valued fields are tried
		}else if(!active) {
			return;
		}
		// if bomb and active click
		else if (this.field[x][y].getMarked() == false) {
			this.lost = true;
		}
		
		if(value == 0 && worked) {
			
			// recursively uncover neighbors
			for(int x_val = x -1; x_val <= x + 1;x_val++) {
				for (int y_val = y -1; y_val <= y+1;y_val++) {
					this.uncoverIfValid(x_val, y_val);
				}
			}
		}
		this.notifyListeners();
	}
	
	/**
	 * uncovers the field if it is in the field
	 * only needed when passively uncovered, because player cannot click outside of the field
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	private void uncoverIfValid(int x, int y) {
		if(this.isValid(x, y)) {
			this.uncover(x, y, false);
		}
	}
	
	/**
	 * creates a 2D array of all the labels as strings which are supposed to be visible in view dependent on the state of the field
	 * if marked -> !
	 * if uncovered -> int value
	 * else -> empty
	 * @return 2D array of strings with the lables for the view
	 */
	public String[][] getLabels() {
		String[][] labels = new String[this.size[0]][this.size[1]];
		
		for(int x = 0; x<size[0]; x++) {
			for(int y = 0; y<size[1]; y++) {
				String label = "";
				if(this.field[x][y].getMarked()) {
					label = "!";
				}
				else if(this.field[x][y].getUncovered()) {
					label = Integer.toString(this.field[x][y].getValue());
				}
				labels[x][y] = label;
			}
		}
		return labels;
	}
	
	public void attachListener(PropertyChangeListener listener) {
		this.changed.addPropertyChangeListener(listener);
	}
	
	private void notifyListeners() {
		this.changed.firePropertyChange("", false, true);
	}
}

package mineSweeper.model;

/**
 * this represents one field of a MineSweeper Game
 * it can be marked, and opened and has a certain value which stands for the amount of bombs close by
 * 
 * @author Eosandra Grund {egrund@uni-osnabrueck.de}
 */

public class Field {
	
	private boolean marked = false;
	private int value = 0;
	private boolean uncovered = false;
	
	public Field(int value) {
		this.setValue(value);
	   }
	
	// getter and setter
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean getMarked() {
		return this.marked;
	}
	
	public boolean getUncovered() {
		return this.uncovered;
	}
	
	// methods
	/**
	 * adds one to the attribute value
	 */
	public void addOne() {
		this.value++;
	}
	
	/**
	 * if covered, mark if unmarked and unmark if marked
	 */
	public void mark() {
		if( ! this.uncovered) {
			if(this.marked) {
				this.marked = false;
			}
			else {
				this.marked = true;
			}
		}
	}
	
	/**
	 * uncover this field if not yet and if not marked
	 * @return if attempt to uncover was successful
	 */
	public boolean uncover() {
		
		if(! this.marked && ! this.getUncovered()) {
			this.uncovered = true;
			return true;
		}
		return false;
	}
}

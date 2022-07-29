package mineSweeper.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import mineSweeper.model.Game;

/**
 * This is a Controller for the MineSweeper Game
 * It controls the buttons from the MineSweeperView
 * 
 * @author Eosandra Grund {egrund@uni-osnabrueck.de}
 */

public class MineSweeperController extends MouseAdapter {
	
	private Game model;
	private int x;
	private int y;
	
	public MineSweeperController(Game model, int x, int y) {
		this.model = model;
		this.x = x;
		this.y = y;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		switch(e.getButton()) {
		case MouseEvent.BUTTON1: // left mouse key
			this.model.uncover(this.x,this.y,true);
			break;
		case MouseEvent.BUTTON3: // right mouse key
			this.model.marker(this.x,this.y);
			break;
		}
		
	}

}

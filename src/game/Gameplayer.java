package game;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
public class Gameplayer {
	
	// Players start values
	//private String playerDirection = "up";

	private List<Player> players;
	private Player me;
	private ScoreList slist;
	
	private String wall = "#";
	private String floor = "O";
	private KeyClass ko;
	

	
// level is defined column by column
	private String[][] level;
	// level is defined column by column
	private Screen screen;

	public Gameplayer(Player me, ScoreList s, List<Player> players) {
		this.me = me;
		this.slist = s;
		this.players = players;
		level = GameSettings.level;
		
		screen = new Screen(level,me.getXpos(),me.getYpos());
		screen.setVisible(true);
		ko = new KeyClass(this);
		screen.addKeyListener(ko); 
	}

	public void PlayerMoved(Direction direction) {
		me.setDirection(direction);
		int x = me.getXpos(), y = me.getYpos();
		
		switch(direction) {
			case Right:
				x = me.getXpos() + 1;
				break;
			case Left:
				x = me.getXpos() - 1;
				break;
			case Up:
				y = me.getYpos() - 1;
				break;
			case Down:
				y = me.getYpos() + 1;
				break;
		}
		
		if (level[x][y].equals(wall)) {
			me.subOnePoint();
			screen.movePlayerOnScreen(me.getXpos(), me.getYpos(), me.getXpos(), me.getYpos(), me.getDirection());
			slist.updateScoreOnScreen();
		} 
		else if(level[x][y].equals(floor)) {
			me.addOnePoint();
			slist.updateScoreOnScreen();
			screen.movePlayerOnScreen(me.getXpos(), me.getYpos(), x, y, me.getDirection());
			me.setXpos(x);
			me.setYpos(y);
		}
		
		try {
			Service.meMoved(me);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playerShoots() throws IOException {
		me.addPoints(-GameSettings.shootCost);
		Service.meFired(me);
	}

	public Screen getScreen() {
		return screen;
	}
}


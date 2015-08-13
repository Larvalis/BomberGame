package game;

import java.awt.GridLayout;


import java.awt.Point;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Screen extends JFrame {
	private JLabel[][] labels = new JLabel[20][20];
	private final String IMAGE_FOLDER_PATH = "./src/Image";
	private String[][] level;
	
	public Screen(String[][] level,int posX,int posY)
	{
		super("TKgame v. 1.0");
		
		
		this.level = level;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(100, 100);
		this.setSize(500, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		draw(posX,posY);
		this.setAlwaysOnTop(true);
		this.repaint();
		this.setVisible(true);
	}
	
	public void resetTile(int x, int y) {
		labels[x][y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + "/Gulv_B.png"));
	}
	
	public void movePlayerOnScreen(int oldX, int oldY, int x, int y, Direction playerDirection) {
		resetTile(oldX, oldY);

		switch(playerDirection) {
			case Right:
				labels[x][y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + "/Bomberman_right.png"));
				break;
			case Left:
				labels[x][y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + "/Bomberman_left.png"));
				break;
			case Up:
				labels[x][y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + "/Bomberman_up.png"));
				break;
			case Down:
				labels[x][y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + "/Bomberman_down.png"));
				break;
		}
	}
	
	/**
	 * Sætter en skudsalve gennem alle Gulv tiles, fra player position i playerDirection (indtil mur)
	 * @param x - Players x position
	 * @param y - Players y position
	 * @param playerDirection - Players direction of head
	 */
	public void showShotOnScreen(int x, int y, Direction d){
		Point currentPos = new Point(x, y);
		String imagePath = "";
		switch(d) {
			case Left:
				imagePath = "/ildVandret_B.png";
			break;
			case Right:
				imagePath = "/ildVandret_B.png";
			break;
			case Up:
				imagePath = "/ildLodret_B.png";
			break;
			case Down:
				imagePath = "/ildLodret_B.png";
			break;
		}
		
		currentPos = nextTile(currentPos, d);
		while(!GameSettings.level[currentPos.x][currentPos.y].equals("#")) {
			labels[currentPos.x][currentPos.y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + imagePath));
			currentPos = nextTile(currentPos, d);
		}
		
		switch(d) {
			case Left:
				currentPos = new Point(currentPos.x + 1, currentPos.y);
				imagePath = "/ildModMurVest_B.png";
			break;
			case Right:
				currentPos = new Point(currentPos.x - 1, currentPos.y);
				imagePath = "/ildModMurOest_B.png";
			break;
			case Up:
				currentPos = new Point(currentPos.x, currentPos.y + 1);
				imagePath = "/ildModMurNord_B.png";
			break;
			case Down:
				currentPos = new Point(currentPos.x, currentPos.y - 1);
				imagePath = "/ildModMurSyd_B.png";
			break;
		}
		if(!(currentPos.x == x && currentPos.y == y))
			labels[currentPos.x][currentPos.y].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + imagePath));
	}
	
	/**
	 * Fjerner skudsalven ved at genindsætte alle Gulv tiles, fra player position i playerDirection (indtil mur)
	 * @param x - Players x position
	 * @param y - Players y position
	 * @param playerDirection - Players direction of head
	 */
	public void hideShotOnScreen(int x, int y, Direction d){
		Point currentPos = new Point(x, y);
		currentPos = nextTile(currentPos, d);
		while(!GameSettings.level[currentPos.x][currentPos.y].equals("#")) {
			resetTile(currentPos.x, currentPos.y);
			currentPos = nextTile(currentPos, d);
		}
	}
	
	private Point nextTile(Point pos, Direction d) {
		int x = pos.x, y = pos.y;
		switch(d) {
			case Left:
				x--;
			break;
			case Right:
				x++;
			break;
			case Up:
				y--;
			break;
			case Down:
				y++;
			break;
		}
		return new Point(x, y);
	}
	
	public void draw(int posX,int posY) {
		for (int j = 0; j < 20; j++) {
			for (int i = 0; i < 20; i++) {
				if (level[i][j].equalsIgnoreCase("#")) {	
					JLabel l = new JLabel(new ImageIcon(IMAGE_FOLDER_PATH + "/mur_B.png"));
					l.setSize(50, 50);
					this.add(l);
					labels[i][j] = l;
				} else if (level[i][j].equalsIgnoreCase("O")) {
					JLabel l = new JLabel(new ImageIcon(IMAGE_FOLDER_PATH + "/gulv_B.png"));
					l.setSize(50, 50);
					this.add(l);
					labels[i][j] = l;
				}
				
			}

		}
		labels[posX][posY].setIcon(new ImageIcon(IMAGE_FOLDER_PATH + "/Bomberman_up.png"));
	}
}

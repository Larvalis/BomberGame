package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreList extends JFrame {

	/**
	 * @param args
	 */
//	Player me;
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();

	
	public ScoreList() {
		super("TKgame v. 1.0");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		this.setVisible(true);
		draw();
		this.setAlwaysOnTop(true);
	}
	
	public void draw() {
		Container contentPane = this.getContentPane();
		contentPane.removeAll();
		List<Player> players = Service.getPlayers();
		for (Player p : players) {
			addPlayerLabel(p.ToString());
		}	
		contentPane.revalidate(); 
		contentPane.repaint();
	}	
	
	public void updateScoreOnScreen() {
		List<Player> players = Service.getPlayers();
		draw();
	}	
	
	private void addPlayerLabel(String name) {
		JLabel l = new JLabel(name);
		l.setSize(50,200);
		this.add(l);
		labels.add(l);
	}
	
}	
	

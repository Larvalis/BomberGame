package game;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServerRunningWindow  extends JFrame implements ActionListener{
	
	JPanel pane = new JPanel();
	JLabel showme = new JLabel("Server running");
	JButton pressme = new JButton("Close server");
	public ServerRunningWindow(){
		super("Game server running");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(10, 10);
		this.setSize(50, 100);
		this.setResizable(true);
		
		Container con = this.getContentPane(); // inherit main frame
	    con.add(pane);    // JPanel containers default to FlowLayout
	    pane.add(showme);
	    pressme.addActionListener(this); 
	    pane.add(pressme);
	    //pressme.requestFocus();
		
		this.setVisible(true);

		//Add label with server running information
		//Add button with close server
		
	}
	
	public void actionPerformed(ActionEvent event)
	{
	    Object source = event.getSource();
	    if (source == pressme)
	    {
	    	System.exit(0);
	    }
	}
}

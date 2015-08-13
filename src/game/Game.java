package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Game {

	/**
	 * @param args
	 *
	 */
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	private static Runnable serverListener;
	private static String name;
	private static Socket socket;

 	public static final String SERVER_IP = "localhost";
//	public static final String SERVER_IP = "10.10.134.2"; //Peter
	public static final int SERVER_PORT = 5000;
	
	
	public static void main(String[] args) throws Exception {

		//insert Server IP information in dialog with option to choose localhost
		String serverIP;
		
		Object[] options1 = { "Use this IP", "Use LocalHost"};

		JPanel panel = new JPanel();
		panel.add(new JLabel("Enter an IP"));
		JTextField textField = new JTextField(20);
		panel.add(textField);
		
		int result = JOptionPane.showOptionDialog(null, panel, "Enter an IP",
		        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
		        null, options1, null);
		if (result == JOptionPane.YES_OPTION){
			serverIP = textField.getText();
		} else{
			serverIP = SERVER_IP;
		}
		//Insert Server IP done
		
		socket = new Socket(serverIP, SERVER_PORT);
		while(true) {
			
			 name =  (String) JOptionPane.showInputDialog(null, "Insert your name","Player", 
						JOptionPane.INFORMATION_MESSAGE, null, null, null);
			
			
		if (name==null){exit();}	

			if(name.length() > 10)
				name = name.substring(0, 10);

			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			out.reset();
			out.writeObject(name);
			out.flush();
			
			Object msg = in.readObject();
			//String status = (String)in.readObject();
			if(msg.getClass().isArray() /*status.equals("OK")*/) {
				GameSettings.setLevel((String[][])msg);
				break;
			} else {
				JOptionPane.showMessageDialog(null, "Error", (String)msg,
						JOptionPane.WARNING_MESSAGE);
				exit();
			}
		}
		
		serverListener = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						Msg msg = (Msg)in.readObject();
						Player initiator = msg.getInitiator();
						
						switch(msg.getAction()) {
							case Login:
								if(!name.equals(initiator.getName())) {
									Service.addPlayer(initiator);
								}
								else {
									Service.login(msg);
								}
								break;
							case Move:
								Service.playerMoved(msg);
								break;
							case MoveDeath:
								Service.moveDeath(msg);
								break;
							case Logout:
								Service.logout(msg);
								break;
							case Shoot:
								Service.shotFired(initiator);
								break;
							case ShootDeath:
								Service.shootDeath(msg);
								Service.shotFired(initiator);
								break;
						}
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(serverListener).run();
	}
	
	public static void sendMsg(Msg msg) throws IOException {
		out.reset();
		out.writeObject(msg);
		out.flush();
	}
	
	public static void exit() throws IOException {
		if (socket!=null) socket.close();
		System.exit(0);
	}
}

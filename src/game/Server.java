package game;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

public class Server {

	private ServerSocket serverSocket;
	private static final int PORT = 5000;
	private Map<Integer, PlayerThread> clients = new HashMap<Integer, PlayerThread>();
	private static int nextPlayerId = 1;

	public static void main(String[] args) throws Exception {
		//Choosing of level before server start
		int level = 1;		
		List<String> levelL = new ArrayList<String>();
		
		levelL.add("Default level (the original)"); // level 1
		levelL.add("Cross level (An outer ring with a cross)\n"); // level 2
		levelL.add("Square level (An outer ring with squares in it)\n"); // level 3
		levelL.add("Spiral level (A spiral)\n"); // level 4
		levelL.add("Outer Ring (A simple outer ring)\n"); // level 5
		levelL.add("Test level (A test level, with 3 open spots (the rest is walls))\n"); // level 6
		levelL.add("Open space. No walls\n"); // level 7
		levelL.add("Dyna (bomberman)"); // level 8
		
		Object[] possibleValues = levelL.toArray();
		Object answer = JOptionPane.showInputDialog(null, "Choose Level", "Choose a level by typing it's number!",
				JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
				possibleValues[0]);
		level = levelL.indexOf(answer)+1;
		
		//insert Server IP information in dialog
	    JOptionPane.showMessageDialog(null, "IP adresses on the server: " + Server.getServerIPAdresses());
		
		
		switch (level){
		case 1:
			GameSettings.setLevelDefault();
			System.out.println("Default chosen");
			break;
		case 2:
			GameSettings.setLevelCross();
			System.out.println("Cross chosen");
			break;
		case 3:
			GameSettings.setLevelSquare();
			System.out.println("Square chosen");
			break;
		case 4:
			GameSettings.setLevelSpiral();
			System.out.println("Spiral chosen");
			break;
		case 5:
			GameSettings.setLevelOuterRing();
			System.out.println("Outer Ring chosen");
			break;
		case 6:
			GameSettings.setLevelTest();
			System.out.println("Test chosen");
			break;
		case 7:
			GameSettings.setLevelOpenSpace();
			System.out.println("Open space chosen");
			break;
		case 8:
			GameSettings.setLevelDyna();
			System.out.println("Dyna (bomberman) chosen");
			break;
		default:
			GameSettings.setLevelDefault();
			System.out.println("No level or invalid input. Set to default level");
			break;
		}
//		//Level chosen, now the server will start
		new Server();
	}

	private Server() throws IOException {
		serverSocket = new ServerSocket(PORT, 10);

		System.out.println("Server started");
		new ServerRunningWindow();

		while (true) {
			// blocks until we get a client
			Socket client = serverSocket.accept();
			PlayerThread pt = new PlayerThread(this, client);
			pt.setPlayerId(nextPlayerId);
			clients.put(nextPlayerId, pt);
			nextPlayerId++;
			new Thread(pt).start();
		}
	}

	public boolean isNameOk(String name) {
		for (Entry<Integer, PlayerThread> i : clients.entrySet()) {
			PlayerThread playerThread = i.getValue();
			if (playerThread.getPlayer() != null
					&& playerThread.getPlayer().getName().equals(name))
				return false;
		}
		return true;
	}

	public synchronized void playerAction(PlayerThread initiatorPlayerThread, Action action)
			throws IOException {
		Player initiator = initiatorPlayerThread.getPlayer();
		int initiatorPlayerId = initiatorPlayerThread.getPlayerId();

		Msg extraMessage = null;

		switch (action) {
			case Login:
				sendToAllButOne(new Msg(Action.Login, initiator), initiatorPlayerId);
				sendToOne(new Msg(Action.Login, initiator, getAllPlayers()),
						initiatorPlayerId);
				break;
			case Move:
				// Check for MoveDeath
				Point pos = new Point(initiator.getXpos(), initiator.getYpos());
				for (Player pl : getAllPlayers()) {
					if (pl.getXpos() == pos.x && pl.getYpos() == pos.y
							&& !pl.getName().equals(initiator.getName())) {
						List<Player> affected = new ArrayList<Player>();
						initiator.addPoints(GameSettings.pointsMoveToDeath);
						pl.addPoints(-GameSettings.pointsMoveToDeath);
						Point randomPos = getRandomPosition();
						pl.setXpos(randomPos.x);
						pl.setYpos(randomPos.y);
						affected.add(pl);
						extraMessage = new Msg(Action.MoveDeath, initiator,
								affected);
						break;
					}
				}
				// End check for MoveDeath
	
				sendToAllButOne(new Msg(Action.Move, initiator), initiatorPlayerId);
				if (extraMessage != null)
					sendToAll(extraMessage);
				break;
			case Shoot:
				Direction d = initiator.getDirection();
				Point currentPos = new Point(initiator.getXpos(), initiator.getYpos());
				
				List<Player> playersHit = new ArrayList<Player>();
				
				currentPos = nextTile(currentPos, d);
				while(!GameSettings.level[currentPos.x][currentPos.y].equals("#")) {
					for (Player pl : getAllPlayers()) {
						if (pl.getXpos() == currentPos.x && pl.getYpos() == currentPos.y) {
							Point newPos = getRandomPosition();
							pl.setXpos(newPos.x);
							pl.setYpos(newPos.y);
							int pointsToSteal = (int) (pl.getPoint() * GameSettings.percentageShootToDeath);
							if(pointsToSteal < 0) 
								pointsToSteal = 0;
							pl.addPoints(-pointsToSteal);
							initiator.addPoints(pointsToSteal);
							playersHit.add(pl);
						}
					}
					currentPos = nextTile(currentPos, d);
				}
				
				if(!playersHit.isEmpty())
					sendToAll(new Msg(Action.ShootDeath, initiator, playersHit));
				else
					sendToAllButOne(new Msg(Action.Shoot, initiator, playersHit), initiatorPlayerThread.getPlayerId());
				break;
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
	
	public synchronized List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Entry<Integer, PlayerThread> i : clients.entrySet()) {
			players.add(i.getValue().getPlayer());
		}
		return players;
	}

	public synchronized Point getRandomPosition() throws IOException {
		Point pos = null;
		boolean found = false;

		List<Player> allPlayers = getAllPlayers();

		while (!found) {
			pos = new Point((int) (1 + (Math.random() * 18)),
					(int) (1 + (Math.random() * 18)));
			if (GameSettings.level[pos.x][pos.y].equalsIgnoreCase("O")) {
				found = true;
				for (Player p : allPlayers) {
					if (p.getXpos() == pos.x && p.getYpos() == pos.y) {
						found = false;
						break;
					}
				}
			}
		}
		return pos;
	}

	public void sendToAll(Msg msg) throws IOException {
		for (Entry<Integer, PlayerThread> i : clients.entrySet()) {
			PlayerThread pt = i.getValue();
			Player p = pt.getPlayer();
			pt.send(msg);
		}
	}

	public void sendToOne(Msg msg, int playerId) throws IOException {
		clients.get(playerId).send(msg);
	}

	public void sendToAllButOne(Msg msg, int playerId) throws IOException {
		for (Entry<Integer, PlayerThread> i : clients.entrySet()) {
			PlayerThread pt = i.getValue();
			Player p = pt.getPlayer();
			if (pt.getPlayerId() != playerId)
				pt.send(msg);
		}
	}

	public void removeClient(int playerId) throws IOException {
		if(clients.containsKey(playerId)) {
			Player initiator = clients.get(playerId).getPlayer();
			clients.remove(playerId);
			if(initiator != null)
				sendToAll(new Msg(Action.Logout, initiator));
		}
	}
	
	public static String getServerIPAdresses() throws Exception{
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		String ip;
		ArrayList<String> ips = new ArrayList<String>();
	    while (e.hasMoreElements())
	    {
	        NetworkInterface n = e.nextElement();
	        //System.out.println(n.getName());
	        Enumeration<InetAddress> ee = n.getInetAddresses();
	        while (ee.hasMoreElements())
	        {
	            InetAddress i = ee.nextElement();
	            ip = i.getHostAddress();
	            if(ip.contains(".")){
	            	System.out.println(ip);
	            	ips.add(ip);
	            }
	        }
	    }
	    
	    String listOfIps = "";
	    for (String s : ips) {
			listOfIps += "\n" + s;
		}
	    
	    return listOfIps;
	}
}

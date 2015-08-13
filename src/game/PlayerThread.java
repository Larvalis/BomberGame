package game;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerThread implements Runnable {

	private Socket playerSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;
	private Player player;
	private String[][] level;
	private int playerId;
	
	public PlayerThread(Server server, Socket client) throws IOException {
		this.server = server;
		playerSocket = client;
		in = new ObjectInputStream(client.getInputStream());
		out = new ObjectOutputStream(client.getOutputStream());
		out.flush();
		level = GameSettings.level;
	}

	@Override
	public void run() {
		try {
			if (handshake()) {
				out.reset();
				out.writeObject(GameSettings.level);
				out.flush();
				server.playerAction(this, Action.Login);
				while (true) {
					Msg playerState = (Msg) in.readObject();
					player = playerState.getInitiator();
					server.playerAction(this, playerState.getAction());
				}
			} else {
				out.writeObject("Navnet er taget");
			}
		} catch (ClassNotFoundException | IOException e) {
			// Client disconnect
			try {
				server.removeClient(playerId);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		
		try {
			server.removeClient(playerId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean handshake() throws ClassNotFoundException, IOException {
		String name = (String) in.readObject();
		boolean isOk = server.isNameOk(name);
		if (!isOk)
			return false;

		player = new Player(name);
		Point pos = server.getRandomPosition();
		player.setXpos(pos.x);
		player.setYpos(pos.y);
		return true;
	}

	public Player getPlayer() {
		return player;
	}

	public void send(Msg msg) throws IOException {
		if(playerSocket.isConnected()) {
			out.writeObject(msg);
			out.flush();
			out.reset();
		}
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}

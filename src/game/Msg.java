package game;

import java.io.Serializable;
import java.util.List;

public class Msg implements Serializable {
	private Action action;
	private Player initiator;
	private List<Player> affectedPlayers;
	
	public Msg(Action action, Player initiator) {
		this.action = action;
		this.initiator = initiator;
	}
	
	public Msg(Action action, Player initiator, List<Player> players) {
		this.action = action;
		this.initiator = initiator;
		this.affectedPlayers = players;
	}

	public Action getAction() {
		return action;
	}

	public List<Player> getPlayers() {
		return affectedPlayers;
	}
	
	public Player getInitiator() {
		return initiator;
	}
}

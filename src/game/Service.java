package game;

import java.io.IOException;
import java.util.ArrayList;

public class Service {
	private static ArrayList<Player> players;
	private static Player me;
	private static ScoreList scorelist;
	private static Gameplayer gameplayer;
	
	public static void login(Msg msg) {
		Player initiator = msg.getInitiator();
		players = new ArrayList<Player>(msg.getPlayers());
		me = initiator;
		
		scorelist = new ScoreList();
		scorelist.setVisible(true);
		gameplayer = new Gameplayer(me, scorelist, players);
		for(Player player : players)
			updatePlayerOnScreen(player);
	}
	
	public static void playerMoved(Msg msg) {
		Player initiator = msg.getInitiator();
		Player currentPlayer = getPlayer(initiator.getName());
		gameplayer.getScreen().movePlayerOnScreen(currentPlayer.getXpos(), 
				currentPlayer.getYpos(),
				initiator.getXpos(), 
				initiator.getYpos(), 
				initiator.getDirection());
		updatePlayerAttributes(currentPlayer, initiator);
		scorelist.updateScoreOnScreen();
	}

	private static void updatePlayerAttributes(Player oldP, Player newP) {
		oldP.setXpos(newP.getXpos());
		oldP.setYpos(newP.getYpos());
		oldP.setDirection(newP.getDirection());
		oldP.setPoint(newP.getPoint());
	}
	
	private static void updatePlayerOnScreen(Player player) {
		updatePlayerOnScreen(player, player);
	}
	
	private static void updatePlayerOnScreen(Player oldP, Player newP) {
		gameplayer.getScreen().movePlayerOnScreen(oldP.getXpos(), 
				oldP.getYpos(),
				newP.getXpos(), 
				newP.getYpos(), 
				newP.getDirection());
		scorelist.updateScoreOnScreen();
	}
	
	private static void updatePlayer(Player player) {
		Player oldP = getPlayer(player.getName());
		Player temp = new Player("!#�% TEMP !#�%");
		temp.setXpos(oldP.getXpos());
		temp.setYpos(oldP.getYpos());
		updatePlayerAttributes(oldP, player);
		updatePlayerOnScreen(temp, player);
	}

	public static void addPlayer(Player initiator) {
		players.add(initiator);
		updatePlayerOnScreen(initiator);
	}

	public static ArrayList<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}
	
	public static Player getPlayer(String name) {
		for(Player p : players) {
			if(p.getName().equals(name))
				return p;
		}
		return null;
	}

	public static void meMoved(Player me) throws IOException {
		updatePlayerAttributes(getPlayer(me.getName()), me);
		Game.sendMsg(new Msg(Action.Move, me));
	}
	
	public static void meFired(Player me) throws IOException {
		updatePlayerAttributes(getPlayer(me.getName()), me);
		shotFired(me);
		Game.sendMsg(new Msg(Action.Shoot, me));
	}

	public static void moveDeath(Msg msg) {
		updatePlayer(msg.getPlayers().get(0));
		updatePlayer(msg.getInitiator());
	}

	public static void logout(Msg msg) {
		Player loggedOutPlayer = getPlayer( msg.getInitiator().getName() );
		players.remove(loggedOutPlayer);
		gameplayer.getScreen().resetTile(loggedOutPlayer.getXpos(), loggedOutPlayer.getYpos());
		scorelist.updateScoreOnScreen();
	}

	public static void shotFired(Player initiator) {
		updatePlayer(initiator);
		scorelist.updateScoreOnScreen();
		gameplayer.getScreen().showShotOnScreen(initiator.getXpos(), initiator.getYpos(), initiator.getDirection());
		new Thread(new GunThread(initiator)).start();
	}
	public static void shotFiredFinish(Player initiator) {
		gameplayer.getScreen().hideShotOnScreen(initiator.getXpos(), initiator.getYpos(), initiator.getDirection());
		for(Player p : getPlayers())
			updatePlayerOnScreen(p, p);
	}

	public static void shootDeath(Msg msg) {
		updatePlayer(msg.getInitiator());
		for(Player p : msg.getPlayers()) {
			Player oldP = getPlayer(p.getName());
			updatePlayer(p);
		}
	}

	
}

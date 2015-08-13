package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyClass implements KeyListener {
	private Gameplayer g;
	private Map<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();

	public KeyClass(Gameplayer g) {
		this.g = g;
		keysDown.put(KeyEvent.VK_UP, false);
		keysDown.put(KeyEvent.VK_DOWN, false);
		keysDown.put(KeyEvent.VK_LEFT, false);
		keysDown.put(KeyEvent.VK_RIGHT, false);
		keysDown.put(KeyEvent.VK_SPACE, false);
	}

	public void keyPressed(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		if(keyCode == ke.VK_ESCAPE){
			try {
				Game.exit();
			} catch (IOException e) {
				e.printStackTrace();
			} }
		if(keysDown.containsKey(keyCode)) {
			if(!keysDown.get(keyCode)) {
				keysDown.put(keyCode, true);
				if (keyCode == ke.VK_UP) {
					g.PlayerMoved(Direction.Up);
				}
				if (keyCode == ke.VK_DOWN) {
					g.PlayerMoved(Direction.Down);
				}
				if (keyCode == ke.VK_LEFT) {
					g.PlayerMoved(Direction.Left);
				}
				if (keyCode == ke.VK_RIGHT) {
					g.PlayerMoved(Direction.Right);
				}
				if (keyCode == ke.VK_SPACE) {
					try {
						g.playerShoots();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void keyReleased(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		keysDown.put(keyCode, false);
	}

	public void keyTyped(KeyEvent ke) {
		
	}
}
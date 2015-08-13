package game;

public class GunThread implements Runnable {

	private Player initiator;
	
	public GunThread(Player initiator) {
		this.initiator = new Player("");
		this.initiator.setXpos(initiator.getXpos());
		this.initiator.setYpos(initiator.getYpos());
		this.initiator.setDirection(initiator.getDirection());
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Service.shotFiredFinish(initiator);
	}

}

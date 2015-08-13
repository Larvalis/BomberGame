package game;

import java.io.Serializable;

public class Player implements Serializable {
	private String name;
	private int xpos;
	private int ypos;
	private int point;
	private Direction direction;

	public Player(String name) {
		this.name = name;
		xpos = 1;
		ypos = 1;
		point = 0;
		direction = Direction.Up;
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String ToString() {
		return name + "   " + point;
	}

	public void addOnePoint() {
		point++;
	}

	public void subOnePoint() {
		point--;
	}
	
	public void addPoints(int value) {
		point += value;
	}
	
	int getPoint(){
		return point;
	}
	
	public void setPoint(int p){
		point = p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

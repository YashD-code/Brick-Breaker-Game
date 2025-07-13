package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Paddle {
	//fields
	private double x;
	private int width, height;
	public final int YPOS = BBMain.HEIGHT-100;
	
	

	public Paddle() {
		width=100;
		height=20;
		x= BBMain.WIDTH/2-width/2;
	}
	//update
	public void update() {
		
	}
	//draw
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(7));
		g.drawRect((int)x,YPOS,width,height);
		g.setColor(Color.DARK_GRAY);
		g.fillRect((int)x, YPOS, width, height);
	}
	public void mouseMoved(int mouseXPos) {
		x=mouseXPos;
		if(x>BBMain.WIDTH-width) {
			x=BBMain.WIDTH-width;
		}
	}
	public Rectangle getRect() {
		return new Rectangle((int)x, YPOS,width , height);
	}
	public int getWidth() {return width;}
}

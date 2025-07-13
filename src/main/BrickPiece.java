package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class BrickPiece{
	//fields
	private int x,y;
	private double dx,dy,gravity;
	private Color color;
	private Map theMap;
	private int size;
	//constructor
	public BrickPiece(int brickx, int bricky, Map theGameMap)
	{
		theMap=theGameMap;
		x=brickx+theMap.getBrickWidth()/2;
		y=bricky+theMap.getBrickHeight()/2;
		dx=(Math.random()*30-15);
		dy=(Math.random()*30-15);
		size=(int)(Math.random()*15+2);
		
		gravity= .8;
	}
	
	public void update() {
		x+=dx;
		y+=dy;
		dy+=gravity;
	}
	public void draw(Graphics2D g) {
		g.setColor(new Color(240,128,128));
		g.fillRect((int) x,(int) y, size, size);
	}

	


}

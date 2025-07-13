package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Score {
	//field
	private int score;
	//construct
	public Score() {
		init();
	}
	
	public void init() {
		score=0;
	}
	public void draw(Graphics2D g) {
		g.setFont(new Font("Courier New",Font.PLAIN,25));
		g.setColor(new Color(0,128,0));
		g.drawString("Score:" + score, 20, 20);
	}
	public int getScore() { return score;}
	public void addScore(int scoreToAdd) {
		score=score+scoreToAdd;
	}

}

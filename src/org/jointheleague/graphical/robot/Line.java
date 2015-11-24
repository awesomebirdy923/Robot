package org.jointheleague.graphical.robot;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Line {
	
	
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	private int lineSize;
	
	private Color color;
	
	public Line(int sx, int sy, int tx, int ty, int size, Color c)
	{
		startX = sx;
		startY = sy;
		endX = tx;
		endY = ty;
		lineSize = size + 1;
		
		color = c;
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		Stroke s = g2.getStroke();
		
		g2.setStroke(new BasicStroke(lineSize));
		g.setColor(color);
		g.drawLine(startX, startY, endX, endY);
		g2.setStroke(s);
	}
	
	public int getEndX() {
		return endX;
	}
	
	public int getEndY() {
		return endY;
	}
}

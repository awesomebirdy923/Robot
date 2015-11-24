package org.jointheleague.graphical.robot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RobotWindow extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	
	private JFrame window;
	
	private Color winColor;
	
	private ArrayList<Robot> robotList;
	
	//image fields
	private static final int IMG_WIDTH = 1408;
	private static final int IMG_HEIGHT = 1170;
	private static int imgX;
	private static int imgY;
	private static BufferedImage leagueLogo;
	
	static int ROBOT_COUNT = 0;
	
	private Timer updateTimer;
	
	public RobotWindow(int w, int h, Color c)
	{
		width = w;
		height = h;
		window = new JFrame();
		window.add(this);
		window.setSize(w, h);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		robotList = new ArrayList<Robot>();
		
		winColor = c;
		
		imgX = ((width / 2) - (IMG_WIDTH / 2)) + 740;
		imgY = ((height / 2) - (IMG_HEIGHT / 2)) - 410;
		
		updateTimer = new Timer(1000 / 60, this);
		
		updateTimer.start();
		
		try {
			leagueLogo = ImageIO.read(this.getClass().getResourceAsStream("league_logo.png"));
		} catch (IOException e) 
		{
			System.err.println("Cannot load background image.");
		}
	}
	
	public void setWinColor(Color c)
	{
		winColor = c;
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(winColor);
		g2.fillRect(0, 0, width, height);
		g2.drawImage(leagueLogo, imgX, imgY, IMG_WIDTH, IMG_HEIGHT, null);
		
		for(int i = 0; i < robotList.size(); i++)
		{
			Robot r = robotList.get(i);
			r.update(g2);
		}
	}
	
	public void addRobot(Robot r)
	{
		robotList.add(r);
		ROBOT_COUNT++;
	}

	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
}

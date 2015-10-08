package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class Robot implements ActionListener {

	private static final int WIDTH = 1780;
	private static final int HEIGHT = 1024;

	private boolean isVisible;
	private boolean penDown;
	private boolean isSparkling;
	private boolean isTurning;

	private float xPos;

	private float yPos;
	private int angle;
	private int newAngle;
	private int speed;
	private int moveDistance;
	private int distanceMoved;

	private int sx;
	private int sy;
	private int tx;
	private int ty;
	private int penSize;

	private Color penColor;

	private long startTime;

	private RobotImage rImage;

	private Line currentLine;
	private ArrayList<Line> lines;

	private static int count = 0;
	private static Color windowColor;
	private static RobotWindow window;
	private static Timer staticAnimationTimer;

	public Robot() {
		this(WIDTH / 2, HEIGHT / 2);
	}

	public Robot(String fileName) {
		this(WIDTH / 2, HEIGHT / 2, fileName);
	}

	public Robot(int x, int y) {
		this(x, y, "rob");
	}

	public static void main(String[] args) {
		Robot r = new Robot("formersecretaryofstatecondoleezzarice");
	}

	public Robot(int x, int y, String fileName) {
		xPos = x;
		yPos = y;
		angle = 0;
		speed = 1;
		newAngle = 0;
		moveDistance = 0;
		distanceMoved = 0;
		startTime = 0;

		sx = x;
		sy = y;
		tx = x;
		ty = y;
		penSize = 1;
		penColor = Color.BLACK;

		isVisible = true;
		penDown = false;
		isTurning = false;
		isSparkling = false;

		rImage = new RobotImage((int) xPos, (int) yPos, fileName);

		currentLine = new Line(x, y, x, y, 0, penColor);
		lines = new ArrayList<Line>();

		if (count == 0) {
			windowColor = new Color(220, 220, 220);
			window = new RobotWindow(WIDTH, HEIGHT, windowColor);
			staticAnimationTimer = new Timer(1000 / 30, this);
		}

		count++;
		window.addRobot(this);
	}

	public void draw(Graphics2D g) {
		for (int i = 0; i < lines.size(); i++) {
			Line l = lines.get(i);
			l.draw(g);
		}

		if (penDown)// draws under robot
		{
			currentLine.draw(g);
		}

		if (isVisible) {
			rImage.draw(g);
		}

		if (penDown)// draws over robot
		{
			g.setColor(penColor);
			int pSize = penSize + 3;
			int newX = (int) (xPos - (pSize / 2));
			int newY = (int) (yPos - (pSize / 2));
			g.fillOval(newX, newY, pSize, pSize);
		}

		if (isSparkling) {
			Random r = new Random();
			int xDot = r.nextInt(100) - 50;
			int yDot = r.nextInt(100) - 50;
			g.setColor(Color.WHITE);
			g.fillRect((int) (xPos + xDot), (int) (yPos + yDot), 5, 5);
		}
	}

	public void update() {
		float cos = (float) Math.cos(Math.toRadians(-angle));
		float sin = (float) Math.sin(Math.toRadians(-angle));

		if (distanceMoved < moveDistance) {
			int difference = moveDistance - distanceMoved;

			if (difference < speed) {
				float nextX = difference * sin;
				float nextY = difference * cos;
				xPos -= nextX;
				yPos -= nextY;
				distanceMoved += difference;
			} else {
				float nextX = speed * sin;
				float nextY = speed * cos;
				xPos -= nextX;
				yPos -= nextY;
				distanceMoved += speed;
			}
		} else if (distanceMoved > moveDistance) {
			int difference = distanceMoved - moveDistance;

			if (difference < speed) {
				float nextX = difference * sin;
				float nextY = difference * cos;
				xPos += nextX;
				yPos += nextY;
				distanceMoved -= (distanceMoved - moveDistance);
			} else {
				float nextX = speed * sin;
				float nextY = speed * cos;
				xPos += nextX;
				yPos += nextY;
				distanceMoved -= speed;
			}
		}

		tx = (int) xPos;
		ty = (int) yPos;

		if (penDown && !isTurning) {
			currentLine = new Line(sx, sy, tx, ty, penSize, penColor);

			if (moveDistance == distanceMoved) {
				lines.add(currentLine);
			}
		}

		if (angle < newAngle) {
			rImage.rotate(-1);
			angle++;
		} else if (angle > newAngle) {
			rImage.rotate(1);
			angle--;
		}

		rImage.x = (int) xPos;
		rImage.y = (int) yPos;
	}

	public void changeRobot(String fileName) {
		rImage.changeRobot(fileName);
		window.update(this);
	}

	public void setPenColor(Color c) {
		penColor = c;
	}

	public void setPenColor(int r, int g, int b) {
		r = Util.clamp(r, 0, 255);
		g = Util.clamp(r, 0, 255);
		b = Util.clamp(b, 0, 255);

		penColor = new Color(r, g, b);
	}

	public static void setWindowColor(Color c) {
		if (count != 0) {
			window.setWinColor(c);
		}
	}

	public static void setWindowColor(int r, int g, int b) {
		if (count != 0) {
			r = Util.clamp(r, 0, 255);
			g = Util.clamp(r, 0, 255);
			b = Util.clamp(b, 0, 255);

			window.setWinColor(new Color(r, g, b));
		}
	}

	public void setPenWidth(int size) {
		size = Util.clamp(size, 1, 10);
		penSize = size;
	}

	public void clear() {
		lines.clear();
		window.update(this);
	}

	public void miniturize() {
		rImage.miniturize();
		window.update(this);
	}

	public void expand() {
		rImage.expand();
		window.update(this);
	}

	public void sparkle() {
		isSparkling = true;
	}

	public void unSparkle() {
		isSparkling = false;
	}

	public void show() {
		isVisible = true;
		update();
	}

	public void hide() {
		isVisible = false;
		update();
	}

	public void move(int distance) {
		staticAnimationTimer.stop();

		distanceMoved = 0;
		moveDistance = distance;

		sx = (int) xPos;
		sy = (int) yPos;
		tx = (int) xPos;
		ty = (int) yPos;

		if (penDown) {
			currentLine = new Line(sx, sy, tx, ty, penSize, penColor);
		}

		startTime = System.currentTimeMillis();

		while (distanceMoved != moveDistance) {
			if ((System.currentTimeMillis() - startTime) > (1000 / 60)) {
				window.update(this);
				startTime = System.currentTimeMillis();
			}
		}

		staticAnimationTimer.start();
	}

	public void moveTo(int x, int y) {
		xPos = x;
		yPos = y;
		window.update(this);
	}

	public void setX(int newX) {
		boolean pen = penDown;
		penUp();

		xPos = newX;
		window.update(this);

		if (pen) {
			penDown();
		}
	}

	public void setY(int newY) {
		boolean pen = penDown;
		penUp();

		yPos = newY;
		window.update(this);

		if (pen) {
			penDown();
		}
	}

	public void turn(int degrees) {
		staticAnimationTimer.stop();

		newAngle = angle + degrees;

		startTime = System.currentTimeMillis();

		while (angle != newAngle) {
			isTurning = true;

			if ((System.currentTimeMillis() - startTime) > (10 - speed)) {
				window.update(this);
				startTime = System.currentTimeMillis();
			}
		}

		isTurning = false;
		staticAnimationTimer.start();
	}

	public void penUp() {
		penDown = false;
	}

	public void penDown() {
		penDown = true;
	}

	public void setSpeed(int s) {
		s = Util.clamp(s, 0, 10);

		speed = s;
	}

	public void setAngle(int a) {
		int turnAmt = angle - a;
		rImage.rotate(turnAmt);
		angle = a;
	}

	public void setRandomPenColor() {
		Random random = new Random();
		this.penColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	public void actionPerformed(ActionEvent arg0) {
		window.update(this);
	}

	public int getX() {
		return (int) xPos;
	}

	public int getY() {
		return (int) yPos;
	}

	public RobotWindow getWindow() {
		return this.window;
	}

}

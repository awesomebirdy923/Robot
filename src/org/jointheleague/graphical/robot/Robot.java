package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.Timer;

public class Robot {

	private static final int WIDTH = 1780;
	private static final int HEIGHT = 1024;

	private float xPos;
	private float yPos;
	private int moveSpeed;
	private int turnSpeed;
	private int angle;

	private int penSize;

	private Color penColor;

	private RobotImage rImage;

	private ArrayList<Line> lines;

	private static RobotWindow window;

	private LinkedList<Action> actionQueue;

	public Robot() {
		this(WIDTH / 2, HEIGHT / 2);
	}

	public Robot(String fileName) {
		this(WIDTH / 2, HEIGHT / 2, fileName);
	}

	public Robot(int x, int y) {
		this(x, y, "rob");
	}

	public Robot(int x, int y, String fileName) {
		xPos = x;
		yPos = y;
		angle = 0;
		moveSpeed = 1;
		turnSpeed = 1;

		penSize = 1;
		penColor = Color.BLACK;

		actionQueue = new LinkedList<Action>();

		rImage = new RobotImage((int) xPos, (int) yPos, fileName);

		lines = new ArrayList<Line>();

		if (RobotWindow.ROBOT_COUNT == 0) {
			window = new RobotWindow(WIDTH, HEIGHT, new Color(220, 220, 220));
		}

		window.addRobot(this);

		show();
	}

	// public void draw(Graphics2D g) {
	// for (int i = 0; i < lines.size(); i++) {
	// Line l = lines.get(i);
	// l.draw(g);
	// }
	//
	// if (penDown)// draws under robot
	// {
	// currentLine.draw(g);
	// }
	//
	// if (isVisible) {
	// rImage.draw(g);
	// }
	//
	// if (penDown)// draws over robot
	// {
	// g.setColor(penColor);
	// int pSize = penSize + 3;
	// int newX = (int) (xPos - (pSize / 2));
	// int newY = (int) (yPos - (pSize / 2));
	// g.fillOval(newX, newY, pSize, pSize);
	// }
	//
	// if (isSparkling) {
	// Random r = new Random();
	// int xDot = r.nextInt(100) - 50;
	// int yDot = r.nextInt(100) - 50;
	// g.setColor(Color.WHITE);
	// g.fillRect((int) (xPos + xDot), (int) (yPos + yDot), 5, 5);
	// }
	// }

	public void update(Graphics2D g) {

//		System.out.println("*****");

		rImage.x = (int) xPos;
		rImage.y = (int) yPos;

//		for (Action a : actionQueue) {
//			System.out.println(a.getAction());
//		}

		for (int i = 0; i < lines.size(); i++) {
			Line l = lines.get(i);
			l.draw(g);
		}

		for (int i = 0; i < actionQueue.size(); i++) {
			Action curAction = actionQueue.get(i);

			for (int j = 0; j < i; j++) {
				Action otherRunningAction = actionQueue.get(j);

//				System.out.println(i + " : " + j);
//
//				System.out.println(curAction.getAction() + " : "
//						+ otherRunningAction.getAction());
//
//				System.out.println(curAction.getType() + " : "
//						+ otherRunningAction.getType());
//
//				System.out
//						.println(curAction.canRunTogether(otherRunningAction));

				if (curAction.getAction()
						.equals(otherRunningAction.getAction())) {
					if (!curAction.allowsMultiple()
							|| !otherRunningAction.allowsMultiple()) {
						actionQueue.remove(j);
						j--;
						continue;
					}
				}

				if (!curAction.canRunTogether(otherRunningAction)) {
					return;
				}
			}

			for (int j = 0; j < i; j++) {
				Action otherRunningAction = actionQueue.get(j);

				if (curAction.isInverse(otherRunningAction)) {
					actionQueue.remove(j);
					j--;
				}
			}

//			System.out.println("***");
//			System.out.println(curAction.getAction());
//			System.out.println("---");

			switch (curAction.getAction()) {

			case ("changeRobot"):
				rImage.changeRobot(curAction.getAddInfo());
				break;

			case ("setPenColor"):
				penColor = new Color(curAction.getParams()[0],
						curAction.getParams()[1], curAction.getParams()[2]);
				actionQueue.remove(i);
				i--;
				break;

			case ("setPenWidth"):
				penSize = curAction.getParams()[0];
				actionQueue.remove(i);
				i--;
				break;

			case ("clear"):
				lines.clear();
				actionQueue.remove(i);
				i--;
				break;

			case ("minturize"):
				rImage.miniturize();
				break;

			case ("expand"):
				rImage.expand();
				break;

			case ("sparkle"):
				Random r = new Random();
				int xDot = r.nextInt(100) - 50;
				int yDot = r.nextInt(100) - 50;
				g.setColor(Color.WHITE);
				g.fillRect((int) (xPos + xDot), (int) (yPos + yDot), 5, 5);
				break;

			case ("show"):
				rImage.draw(g);
				break;

			case ("move"):

//				System.out.println("***MOVE***");

				int distLeft = curAction.getParams()[0];

				float cos = (float) -Math.cos(Math.toRadians(angle));
				float sin = (float) -Math.sin(Math.toRadians(angle));

//				System.out.println(cos + " " + sin);
//
//				System.out.println("Dist: " + distLeft);

				if (distLeft > moveSpeed) {
					yPos += moveSpeed * cos;
					xPos += moveSpeed * sin;

					actionQueue.get(i).setParams(
							new int[] { distLeft - moveSpeed });

				} else {
					yPos += distLeft * cos;
					xPos += distLeft * sin;

					actionQueue.remove(i);
					i--;

				}

//				System.out.println(xPos + " " + yPos);

				break;

			case ("moveTo"):
				yPos = curAction.getParams()[0];
				xPos = curAction.getParams()[1];
				actionQueue.remove(i);
				i--;
				break;

			case ("setX"):
				xPos = curAction.getParams()[0];
				actionQueue.remove(i);
				i--;
				break;

			case ("setY"):
				yPos = curAction.getParams()[0];
				actionQueue.remove(i);
				i--;
				break;

			case ("turn"):
				int degreesToTurn = curAction.getParams()[0];

				if (Math.abs(degreesToTurn) < turnSpeed) {
					angle += degreesToTurn;
					rImage.rotate(degreesToTurn);
					actionQueue.remove(i);
					i--;
				} else if (degreesToTurn > 0) {
					angle += turnSpeed;
					actionQueue.get(i).setParams(
							new int[] { degreesToTurn - turnSpeed });
					rImage.rotate(turnSpeed);
				} else if (degreesToTurn < 0) {
					angle += -turnSpeed;
					actionQueue.get(i).setParams(
							new int[] { degreesToTurn + turnSpeed });

					rImage.rotate(-turnSpeed);
				}
				break;

			case ("penDown"):

				if (lines.size() > 0) {
					Line lastLine = lines.get(lines.size() - 1);

					lines.add(new Line(lastLine.getEndX(), lastLine.getEndY(),
							(int) xPos, (int) yPos, penSize, penColor));
				} else {
					lines.add(new Line((int) xPos, (int) yPos, (int) xPos,
							(int) yPos, penSize, penColor));
				}
				break;

			case ("setSpeed"):
				moveSpeed = curAction.getParams()[0];
				turnSpeed = curAction.getParams()[0];
				actionQueue.remove(i);
				i--;
				break;
			}
		}
	}

	// public void update() {
	// float cos = (float) Math.cos(Math.toRadians(-angle));
	// float sin = (float) Math.sin(Math.toRadians(-angle));
	//
	// if (distanceMoved < moveDistance) {
	// int difference = moveDistance - distanceMoved;
	//
	// if (difference < speed) {
	// float nextX = difference * sin;
	// float nextY = difference * cos;
	// xPos -= nextX;
	// yPos -= nextY;
	// distanceMoved += difference;
	// } else {
	// float nextX = speed * sin;
	// float nextY = speed * cos;
	// xPos -= nextX;
	// yPos -= nextY;
	// distanceMoved += speed;
	// }
	// } else if (distanceMoved > moveDistance) {
	// int difference = distanceMoved - moveDistance;
	//
	// if (difference < speed) {
	// float nextX = difference * sin;
	// float nextY = difference * cos;
	// xPos += nextX;
	// yPos += nextY;
	// distanceMoved -= (distanceMoved - moveDistance);
	// } else {
	// float nextX = speed * sin;
	// float nextY = speed * cos;
	// xPos += nextX;
	// yPos += nextY;
	// distanceMoved -= speed;
	// }
	// }
	//
	// tx = (int) xPos;
	// ty = (int) yPos;
	//
	// if (penDown && !isTurning) {
	// currentLine = new Line(sx, sy, tx, ty, penSize, penColor);
	//
	// if (moveDistance == distanceMoved) {
	// lines.add(currentLine);
	// }
	// }
	//
	// if (angle < newAngle) {
	// rImage.rotate(-1);
	// angle++;
	// } else if (angle > newAngle) {
	// rImage.rotate(1);
	// angle--;
	// }
	//
	// rImage.x = (int) xPos;
	// rImage.y = (int) yPos;
	// }

	public void changeRobot(String fileName) {
		actionQueue.add(new Action("changeRobot", 0, fileName));
	}

	// public void changeRobot(String fileName) {
	// rImage.changeRobot(fileName);
	// }

	public void setPenColor(Color c) {
		setPenColor(c.getRed(), c.getGreen(), c.getBlue());
	}

	// public void setPenColor(Color c) {
	// penColor = c;
	// }

	public void setPenColor(int r, int g, int b) {

		int[] color = new int[3];

		color[0] = Util.clamp(r, 0, 255);
		color[1] = Util.clamp(r, 0, 255);
		color[2] = Util.clamp(b, 0, 255);

		actionQueue.add(new Action("setPenColor", 0, color));
	}

	// public void setPenColor(int r, int g, int b) {
	// r = Util.clamp(r, 0, 255);
	// g = Util.clamp(r, 0, 255);
	// b = Util.clamp(b, 0, 255);
	//
	// penColor = new Color(r, g, b);
	// }

	public static void setWindowColor(Color c) {
		if (RobotWindow.ROBOT_COUNT != 0) {
			window.setWinColor(c);
		}
	}

	public static void setWindowColor(int r, int g, int b) {
		if (RobotWindow.ROBOT_COUNT != 0) {
			r = Util.clamp(r, 0, 255);
			g = Util.clamp(r, 0, 255);
			b = Util.clamp(b, 0, 255);

			window.setWinColor(new Color(r, g, b));
		}
	}

	public void setPenWidth(int size) {
		size = Util.clamp(size, 1, 10);
		actionQueue.add(new Action("setPenWidth", 0, new int[] { size }));
	}

	// public void setPenWidth(int size) {
	// size = Util.clamp(size, 1, 10);
	// penSize = size;
	// }

	public void clear() {
		actionQueue.add(new Action("clear", 0));
	}

	// public void clear() {
	// lines.clear();
	// }

	public void miniturize() {
		actionQueue.add(new Action("miniturize", 1, false));
	}

	// public void miniturize() {
	// rImage.miniturize();
	// }

	public void expand() {
		actionQueue.add(new Action("expand", -1, false));
	}

	// public void expand() {
	// rImage.expand();
	// }

	public void sparkle() {
		actionQueue.add(new Action("sparkle", 2));
	}

	// public void sparkle() {
	// isSparkling = true;
	// }

	public void unSparkle() {
		actionQueue.add(new Action("unSparkle", -2));
	}

	// public void unSparkle() {
	// isSparkling = false;
	// }

	public void show() {
		actionQueue.add(new Action("show", 3));
	}

	// public void show() {
	// isVisible = true;
	// }

	public void hide() {
		actionQueue.add(new Action("hide", -3));
	}

	// public void hide() {
	// isVisible = false;
	// }

	public void move(int distance) {
		actionQueue.add(new Action("move", 0, new int[] { distance }));
	}

	// public void move(int distance) {
	// staticAnimationTimer.stop();
	//
	// distanceMoved = 0;
	// moveDistance = distance;
	//
	// sx = (int) xPos;
	// sy = (int) yPos;
	// tx = (int) xPos;
	// ty = (int) yPos;
	//
	// if (penDown) {
	// currentLine = new Line(sx, sy, tx, ty, penSize, penColor);
	// }
	//
	// startTime = System.currentTimeMillis();
	//
	// while (distanceMoved != moveDistance) {
	// if ((System.currentTimeMillis() - startTime) > (1000 / 60)) {
	// startTime = System.currentTimeMillis();
	// }
	// }
	//
	// staticAnimationTimer.start();
	// }

	public void moveTo(int x, int y) {
		actionQueue.add(new Action("moveTo", 0, new int[] { x, y }));
	}

	// public void moveTo(int x, int y) {
	// xPos = x;
	// yPos = y;
	// }

	public void setX(int newX) {
		actionQueue.add(new Action("setX", 0, new int[] { newX }));
	}

	// public void setX(int newX) {
	// boolean pen = penDown;
	// penUp();
	//
	// xPos = newX;
	//
	// if (pen) {
	// penDown();
	// }
	// }

	public void setY(int newY) {
		actionQueue.add(new Action("setY", 0, new int[] { newY }));
	}

	// public void setY(int newY) {
	// boolean pen = penDown;
	// penUp();
	//
	// yPos = newY;
	//
	// if (pen) {
	// penDown();
	// }
	// }

	public void turn(int degrees) {
		actionQueue.add(new Action("turn", 0, new int[] { degrees }));
	}

	// public void turn(int degrees) {
	// staticAnimationTimer.stop();
	//
	// newAngle = angle + degrees;
	//
	// startTime = System.currentTimeMillis();
	//
	// while (angle != newAngle) {
	// isTurning = true;
	//
	// if ((System.currentTimeMillis() - startTime) > (10 - speed)) {
	// startTime = System.currentTimeMillis();
	// }
	// }
	//
	// isTurning = false;
	// staticAnimationTimer.start();
	// }

	public void penUp() {
		actionQueue.add(new Action("penUp", 4));
	}

	// public void penUp() {
	// penDown = false;
	// }

	public void penDown() {
		actionQueue.add(new Action("penDown", -4));
	}

	// public void penDown() {
	// penDown = true;
	// }

	public void setSpeed(int s) {

		s = Util.clamp(s, 0, 10);

		actionQueue.add(new Action("setSpeed", 0, new int[] { s }));
	}

	// public void setSpeed(int s) {
	// s = Util.clamp(s, 0, 10);
	//
	// speed = s;
	// }

	public void setAngle(int a) {
		int turnAmt = angle - a;
		turn(turnAmt);
	}

	// public void setAngle(int a) {
	// int turnAmt = angle - a;
	// rImage.rotate(turnAmt);
	// angle = a;
	// }

	public void setRandomPenColor() {
		Random random = new Random();

		setPenColor(random.nextInt(255), random.nextInt(255),
				random.nextInt(255));
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

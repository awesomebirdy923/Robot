package org.jointheleague.graphical.robot;

import static org.junit.Assert.*;

public class RobotTests {

	public static void main(String[] args) {
		Robot bob = new Robot();

		bob.setSpeed(10);

		bob.penDown();

		for (int j = 0; j < 4; j++) {

			for (int i = 0; i < 8; i++) {
				bob.setRandomPenColor();
				bob.miniturize();
				bob.move(j * 15);
				bob.turn(45);
			}
			
			bob.clear();
		}
		
		bob.moveTo(100, 450);
		
		bob.sparkle();
		
		for (int j = 0; j < 5; j++) {
			
			bob.expand();

			for (int i = 0; i < 6; i++) {
				bob.setRandomPenColor();
				bob.move(j * 15);
				bob.turn(60);
				
				if (j == 3 && i == 4) {
					bob.unSparkle();
				}
				
			}
			
			bob.clear();
		}
		
		bob.setX(60);
		bob.setY(194);
		
	}
}

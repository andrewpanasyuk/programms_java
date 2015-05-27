/*
 * Copyright (c) 2013 midgardabc.com
 */
package cademika.com;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Random;

import javax.naming.directory.DirContext;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @version 3.0
 */
public class BattleFieldTemplate2 extends JPanel {

	final boolean COLORDED_MODE = true;

	final int BF_WIDTH = 576;
	final int BF_HEIGHT = 576;

	// 1 - top, 2 - bottom, 3 - left, 4 - right
	int tankDirection = 4;

	 int tankX = (gen(0, 512) / 64) * 64;
	 int tankY = (gen(0, 512) / 64) * 64;

	int bulletX = -100;
	int bulletY = -100;

	int speed = 3;
	int bulletSpeed = 1;

	String[][] battleField = bf();

	/**
	 * Write your code here.
	 */
	void runTheGame() throws Exception {
		// processInterception();
		// clean();
		// fire();
		// clean1();
		// move(3);
		//moveRandomWoll();
		//moveRandomWollAndFire();
		// moveRandom();
		 moveToQuadrant(1, 1);
		 Thread.sleep(200);
		 moveToQuadrant(1, 8);
		 Thread.sleep(200);
		 moveToQuadrant(8, 2);
		 Thread.sleep(200);
		 moveToQuadrant(3, 8);
	}

	static String[][] bf() {
		String[][] bf = new String[9][9];
		for (String[] a : bf) {
			for (int i = 0; i <= 8; i++) {
				int s = gen(0, 2);
				if (s == 0 || s == 1) {
					a[i] = " ";
				} else {
					a[i] = "B";
				}
			}
		}
		return bf;
	}

	public static int gen(int a, int b) {
		int s = a + (int) (Math.random() * ((b - a) + 1));
		return s;
	}

	void moveRandomWoll() throws Exception {
		while (true) {
			tankDirection = gen(1, 4);
			if (wollRadar() == false) {
				move(tankDirection);
			} 
		}

	}
	void moveRandomWollAndFire() throws Exception {
		while (true) {
			tankDirection = gen(1, 4);
			if (wollRadar() == false) {
				move(tankDirection);
			} else {
				fire();
				move(tankDirection);
			}
		}
		
	}
	void moveAndFire() throws Exception {
			turn(tankDirection);
			//tankDirection = gen(1, 4);
			if (wollRadar() == false) {
				move(tankDirection);
			} else {
				fire();
				move(tankDirection);
			}
		
	}

	void clean() throws Exception {
		int start = 1;
		while (start <= 9) {
			moveToQuadrant(start, 1);
			turn(4);
			for (int i = 1; i < 9; i++) {
				if (!battleField[start - 1][i].trim().isEmpty()) {
					fire();
				}
			}
			start++;
		}
		System.out.println("Ok!!!");
	}

	boolean processInterception() {
		String qad = getQadrant(bulletX, bulletY);
		int separator = qad.indexOf("_");
		int y = Integer.parseInt(qad.substring(0, separator));
		int x = Integer.parseInt(qad.substring(separator + 1));
		if (y >= 0 && y <= 8 && x >= 0 && x <= 8) {
			if (!battleField[y][x].trim().isEmpty()) {
				battleField[y][x] = " ";
				repaint();
				return true;
			}
		}
		return false;

	}

	static String getQadrant(int x, int y) {
		return y / 64 + "_" + x / 64;
	}

	void fire() throws Exception {
		bulletX = tankX + 25;
		bulletY = tankY + 25;
		while (bulletX >= -14 && bulletX <= 590 && bulletY >= -14
				&& bulletY <= 590) {
			String newPoint = newCoordinate(tankDirection, bulletX, bulletY);
			bulletX = Integer.parseInt(newPoint.substring(0,
					newPoint.indexOf("_")));
			bulletY = Integer
					.parseInt(newPoint.substring(newPoint.indexOf("_") + 1));
			Thread.sleep(speed);
			repaint();
			if (processInterception()) {
				bulletX = -100;
				bulletY = -100;
			}
		}
	}

	String getQuadrantXY(int v, int h) {
		return (v - 1) * 64 + "_" + (h - 1) * 64;
	}

	Boolean controlFild(int direction, int coordinateX, int coordinateY) {
		int start = 0;
		int finish = 512;
		int nextPosition = -100;
		if (direction == 1) {
			nextPosition = coordinateY - 1;
		} else if (direction == 2) {
			nextPosition = coordinateY + 1;
		} else if (direction == 3) {
			nextPosition = coordinateX - 1;
		} else {
			nextPosition = coordinateX + 1;
		}
		if (nextPosition > start && nextPosition < finish) {
			return true;
		} else {
			return false;
		}
	}

	Boolean wollRadar() {

		int y = tankY / 64;
		int x = tankX / 64;
		if (tankDirection == 1 && y != 0) {
			y = y - 1;
		} else if (tankDirection == 2 && y != 8) {
			y = y + 1;
		} else if (tankDirection == 3 && x != 0) {
			x = x - 1;
		} else if (tankDirection == 4 && x != 8) {
			x = x + 1;
		}
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		if (!battleField[y][x].trim().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	void move(int direction) throws Exception {
		int covered = 0;
		if (controlFild(direction, tankX, tankY) == true) {
			turn(direction);
			while (covered < 64) {
				String newPoint = newCoordinate(direction, tankX, tankY);
				tankX = Integer.parseInt(newPoint.substring(0,
						newPoint.indexOf("_")));
				tankY = Integer.parseInt(newPoint.substring(newPoint
						.indexOf("_") + 1));
				covered += 1;
				repaint();
				Thread.sleep(speed);
			}
		}
	}

	String newCoordinate(int direction, int x, int y) {
		int step = 1;
		if (direction == 1) {
			y -= step;
		} else if (direction == 2) {
			y += step;
		} else if (direction == 3) {
			x -= step;
		} else {
			x += step;
		}
		return Integer.toString(x) + "_" + Integer.toString(y);
	}

	// boolean woll(int c, int xx, int yy) {
	// String newPoint = newCoordinate(c, xx, yy);
	// xx = Integer.parseInt(newPoint.substring(0,
	// newPoint.indexOf("_")));
	// yy = Integer.parseInt(newPoint.substring(newPoint
	// .indexOf("_") + 1));
	// int y = yy / 64;
	// int x = xx / 64;
	//
	// if (!battleField[y][x].trim().isEmpty()) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	void turn(int direction) {
		if (tankDirection != direction) {
			tankDirection = direction;
			repaint();
		}
	}

	void moveRandom() throws Exception {
		Random r = new Random();
		int i;
		while (true) {
			i = r.nextInt(5);
			if (i > 0) {
				// if (processInterception()) {
				// fire();
				// }
				tankDirection = i;
				move(tankDirection);
			}
		}
	}

	void moveToQuadrant(int v, int h) throws Exception {
		String coordinates = getQuadrantXY(v, h);
		int separator = coordinates.indexOf("_");
		int y = Integer.parseInt(coordinates.substring(0, separator));
		int x = Integer.parseInt(coordinates.substring(separator + 1));
		if (tankX < x) {
			while (tankX < x) {
				tankDirection = 4;
				moveAndFire();
			}
		} else {
			while (tankX > x) {
				tankDirection = 3;
				moveAndFire();
			}
		}

		if (tankY < y) {
			while (tankY < y) {
				tankDirection = 2;
				moveAndFire();
			}
		} else {
			while (tankY > y) {
				tankDirection = 1;
				moveAndFire();
			}
		}
	}

	// Magic bellow. Do not worry about this now, you will understand everything
	// in this course.
	// Please concentrate on your tasks only.

	public static void main(String[] args) throws Exception {
		BattleFieldTemplate2 bf = new BattleFieldTemplate2();
		bf.runTheGame();
	}

	public BattleFieldTemplate2() throws Exception {
		JFrame frame = new JFrame("BATTLE FIELD, DAY 2");
		frame.setLocation(750, 100);
		frame.setMinimumSize(new Dimension(BF_WIDTH, BF_HEIGHT + 22));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int i = 0;
		Color cc;
		for (int v = 0; v < 9; v++) {
			for (int h = 0; h < 9; h++) {
				if (COLORDED_MODE) {
					if (i % 2 == 0) {
						cc = new Color(252, 241, 177);
					} else {
						cc = new Color(233, 243, 255);
					}
				} else {
					cc = new Color(180, 180, 180);
				}
				i++;
				g.setColor(cc);
				g.fillRect(h * 64, v * 64, 64, 64);
			}
		}

		for (int j = 0; j < battleField.length; j++) {
			for (int k = 0; k < battleField.length; k++) {
				if (battleField[j][k].equals("B")) {
					String coordinates = getQuadrantXY(j + 1, k + 1);
					int separator = coordinates.indexOf("_");
					int y = Integer.parseInt(coordinates
							.substring(0, separator));
					int x = Integer.parseInt(coordinates
							.substring(separator + 1));
					g.setColor(new Color(0, 0, 255));
					g.fillRect(x, y, 64, 64);
				}
			}
		}

		g.setColor(new Color(255, 0, 0));
		g.fillRect(tankX, tankY, 64, 64);

		g.setColor(new Color(0, 255, 0));
		if (tankDirection == 1) {
			g.fillRect(tankX + 20, tankY, 24, 34);
		} else if (tankDirection == 2) {
			g.fillRect(tankX + 20, tankY + 30, 24, 34);
		} else if (tankDirection == 3) {
			g.fillRect(tankX, tankY + 20, 34, 24);
		} else {
			g.fillRect(tankX + 30, tankY + 20, 34, 24);
		}

		g.setColor(new Color(255, 255, 0));
		g.fillRect(bulletX, bulletY, 14, 14);
	}

}

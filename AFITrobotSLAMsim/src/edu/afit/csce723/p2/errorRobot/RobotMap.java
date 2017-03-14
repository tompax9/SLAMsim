/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Jul 1, 2014
 */

package edu.afit.csce723.p2.errorRobot;

import java.awt.Color;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class RobotMap extends Observable {

	private static final int HEIGHT = 101;
	private static final int WIDTH = 101;
	
	private int[][] sensorMap = new int[WIDTH][HEIGHT];
	private float normal = 0;
	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);

	
	public RobotMap() {
		Random rand = new Random(System.currentTimeMillis());
		for (int y=0; y<HEIGHT; y++) {
			for (int x=0; x<WIDTH; x++) {
				sensorMap[x][y] = x + y; //rand.nextInt(256);
				image.setRGB(x, y, sensorMap[x][y]);
			}
		}
	}
	
	public void step() {
		// TODO Auto-generated method stub
	}

	public Image getCurretMap() {
				
//		for (int y=0; y<HEIGHT; y++) {
//			for (int x=0; x<WIDTH; x++) {
//				image.setRGB(x, y, sensorMap[x][y]);
//			}
//		}
		return image;
	}

	private void updateNormal() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				Math.min(sensorMap[x][y], min);
				Math.max(sensorMap[x][y], max);				
			}
		}
		normal = (float) (max - min)/256;
	}


}

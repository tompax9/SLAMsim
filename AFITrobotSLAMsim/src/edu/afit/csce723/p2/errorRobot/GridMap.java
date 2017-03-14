/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Jul 9, 2015
 */

package edu.afit.csce723.p2.errorRobot;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class GridMap {
	private int gridMapWidth = 101;
	private int gridMapHeight = 101;
	private int gridMapOriginX = 50;
	private int gridMapOriginY = 50;
	
	private int[][] theGridMap = new int[gridMapWidth][gridMapHeight];
	
	// What are the largest 2D coordinate values seen thus far 
	private int minWidth = 0;
	private int maxWidth = 0;
	private int minHeight = 0;
	private int maxHeight = 0;
	
	public void update(Robot aRobot) {
		// This will read the robot's current position
		
		// Compute world coords for where the robot and sensors hits
		int x=0, y = 0;  // This will be some sensor or robot coord

		// If new coord exceed the current Western grid map boundary, 
		// expand the size of the map Westward
		minWidth = Math.min(x, minWidth);
		if (x < gridMapOriginX - gridMapWidth) {
			expandGridMapWest();
		}
		
		// If new coord exceed the current Eastern grid map boundary, 
		// expand the size of the map Eastward
		maxWidth = Math.max(x, maxWidth);
		if (x > gridMapWidth - gridMapOriginX) {
			expandGridMapEast();
		}

		// If new coord exceed the current Southern grid map boundary, 
		// expand the size of the map South
		minHeight = Math.min(y, minHeight);
		if (y < gridMapOriginY - gridMapHeight) {
			expandGridMapSouth();
		}
		
		// If new coord exceed the current Northern grid map boundary, 
		// expand the size of the map Northward
		maxHeight = Math.max(y, maxHeight);
		if (y > gridMapHeight - gridMapOriginY) {
			expandGridMapNorth();
		}
	}
	
	private void expandGridMapNorth() {
		
	}
	
	private void expandGridMapSouth() {
		
	}
	
	private void expandGridMapEast() {
		int expandedGridMapWidth = 2 * gridMapWidth + 1;
		System.out.println("The grid map is expanding East to " + expandedGridMapWidth + "x" + gridMapHeight);
		int[][] expandedGridMap = new int[expandedGridMapWidth][gridMapHeight];
		for (int i=0; i < gridMapWidth; i++) {
			for (int j=0; j< gridMapHeight; j++) {
				expandedGridMap[i][j] = theGridMap[i][j];
			}
		}
		gridMapWidth = expandedGridMapWidth;	
		theGridMap = expandedGridMap;
	}
	
	private void expandGridMapWest() {

	}
	
	
	/**
	 * This provides a copy of the current map recorded thus far.  This does not necessarily provide a square map.
	 * @return A copy of the current map
	 */
	public int[][] getTheMap() {
		int currWidth = maxWidth - minWidth;
		int currHeight = maxHeight - minHeight;
		int[][] retVal = new int[currWidth][currHeight]; 
		
		for (int x=0; x<currWidth; x++) {
			for (int y=0; x<currHeight; y++) {
				retVal[x][y] = theGridMap[x][y];
			}
		}
		return retVal;
		
	}
}

/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Jan 17, 2016
 */

package edu.afit.csce723.p2.gridMap;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author bwoolley
 *
 */
public class SimpleMap {

	private Collection<Point2D> pointSet = new HashSet<Point2D>();

	public Collection<Point2D> getPoints() {
		return pointSet;
	}

	public void reinforce(Point2D pt) {
		pointSet.add(pt);
	}
	
	
}

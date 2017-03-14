/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class SensorArray {

	public void setPose(Position pose) {
		for (RangeFinder a : rangeFinders)
			a.setPose(pose);
	}

	public void addRangeFinder(double angle, double maxRange) {
		rangeFinders.add(new RangeFinder(angle, maxRange));
	}
	
	/**
	 * Returns a map of sensor angles paired with the associated range value 
	 * @return A key/value pair denoting the sensor angle and the range value 
	 */
	private Map<Double, Double> getNormalizedRangeReadings() {
		Map<Double, Double> ranges = new HashMap<Double, Double>();
		for(RangeFinder x : rangeFinders) {
			ranges.put(x.getAngle(), x.reading()/x.getMaxRange());
		}
		return ranges;		
	}

	/**
	 * Returns a map of sensor angles paired with the associated range value 
	 * @return A key/value pair denoting the sensor angle and the range value 
	 */
	public Map<Double, Double> getRangeReadings() {
		Map<Double, Double> ranges = new HashMap<Double, Double>();
		for(RangeFinder x : rangeFinders) {
			ranges.put(x.getAngle(), x.reading());
		}
		return ranges;
	}

	private List<Double> getSensorAngles() {
		List<Double> angles = new ArrayList<Double>();
		for(RangeFinder x : rangeFinders) {
			angles.add(x.getAngle());
		}
		return angles;		
	}

	public List<Line2D> getBeams() {
		List<Line2D> beams = new ArrayList<Line2D>();
		for(RangeFinder x : rangeFinders) {
			beams.add(x.getBeam());
		}
		return beams;
	}
	
	private final List<RangeFinder> rangeFinders = new ArrayList<RangeFinder>();
}

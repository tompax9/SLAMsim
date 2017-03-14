/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.behaviorFramework;

import java.util.List;
import java.util.Map;

import edu.afit.csce723.p2.errorRobot.Robot;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class State {

	public State(Robot robot) {
		assert(robot != null);
		myRobot = robot;
	}

	public double getVelocity() {
		return myRobot.getVelocity();
	}
	
	public double getTurnRate() {
		return myRobot.getTurnRate();		
	}
	
	public double getX() {
		return myRobot.getPosition().getX();
	}
	
	public double getY() {
		return myRobot.getPosition().getY();
	}
	
	public double getTheta() {
		return myRobot.getPosition().getTheta();
	}
	
	public Map<Double, Double> getRangeReadings() {
		return myRobot.getSensorArray().getRangeReadings();
	}
	
	private final Robot myRobot;
}

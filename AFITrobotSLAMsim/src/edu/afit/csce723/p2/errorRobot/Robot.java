/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;


/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Robot {

	protected Robot() {
		this(new Position(0, 0, 0));
	}
	
	public Robot(Position pose) {
		// velocity accel/decel is 0.5 units per tick
		acceleration = MAX_VELOCITY/6;	
		
		// angular accel/decel is 0.5 deg per tick
		angAccel = MAX_TURN_RATE/6;		
		
		setStoped();
		
		// Setup the rangeFinder array
		maxSensorRange = 10000;
		sensorArray = new SensorArray();
		for (int i=-180; i<180; i+=30) {
			sensorArray.addRangeFinder( i*degs, maxSensorRange);
		}
//		sensorArray.addRangeFinder( 60*degs, maxSensorRange);
//		sensorArray.addRangeFinder( 30*degs, maxSensorRange);
//		sensorArray.addRangeFinder(  0*degs, maxSensorRange);
//		sensorArray.addRangeFinder(-30*degs, maxSensorRange);
//		sensorArray.addRangeFinder(-60*degs, maxSensorRange);
//		sensorArray.addRangeFinder(-90*degs, maxSensorRange);
		
		setPosition(pose);
	}

	/**
	 * Advances the Robot one timestep.
	 */
	public void step() {
		double diff;

		diff = tgtVelocity - currVelocity;
		if (Math.abs(diff) > acceleration)
		{
			if (currVelocity > tgtVelocity)
				currVelocity -= acceleration;
			if (currVelocity < tgtVelocity)
				currVelocity += acceleration;
		}
		else currVelocity += diff;	
		
		diff = tgtTurnRate - currTurnRate;
		if (Math.abs(diff) > angAccel)
		{
			if (currTurnRate > tgtTurnRate)
				currTurnRate -= angAccel;
			if (currTurnRate < tgtTurnRate)
				currTurnRate += angAccel;
		}
		else currTurnRate += diff;
	}
	
	public double getVelocity() {
		return currVelocity;
	}
	
	public void setVelocity(double velocity) {
		tgtVelocity = velocity * MAX_VELOCITY;
		tgtVelocity = Math.min(Math.max(tgtVelocity, MIN_VELOCITY), MAX_VELOCITY);
	}
	
	protected void setVelocityDirectly(double velocity) {
		currVelocity = velocity;
	}
	
	public double getTurnRate() {
		return currTurnRate;
	}
	
	public void setTurnRate(double turnRate) {
		tgtTurnRate = turnRate * MAX_TURN_RATE;
		tgtTurnRate = Math.min(Math.max(tgtTurnRate, MIN_TURN_RATE), MAX_TURN_RATE);
	}
	
	protected void setTurnRateDirectly(double turnRate) {
		currTurnRate = turnRate;
	}
	
	protected double getHeading() {
		return myPose.getTheta();
	}
	
	protected void setStoped() {
		currVelocity = tgtVelocity = 0;
		currTurnRate = tgtTurnRate = 0;
	}
	
	public Position getPosition() {
		return myPose;
	}
	
	public void setPosition(Position pose) {
		assert(pose != null);
		myPose = pose;
		sensorArray.setPose(pose);
	}
	
	public void setPositionOffset(Position delta) {
		assert(delta != null);
		setPosition(myPose.add(delta));
	}

	public double getRadius() {
		return RADIUS;
	}

	public SensorArray getSensorArray() {
		return sensorArray;
	}
	
	private Position myPose;
	private double currVelocity, tgtVelocity, acceleration;
	private double currTurnRate, tgtTurnRate, angAccel;
	
	private final double maxSensorRange;
	private final SensorArray sensorArray;
	
	private static final double degs = Math.PI/180;
	
	public static final double RADIUS =  8.0;
	public static final double MAX_VELOCITY =  3.0;
	public static final double MIN_VELOCITY = -MAX_VELOCITY;
	public static final double MAX_TURN_RATE =  10.0*degs;
	public static final double MIN_TURN_RATE = -MAX_TURN_RATE;
}

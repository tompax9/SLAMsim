/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.afit.csce723.p2.behaviorFramework.Behavior;
import edu.afit.csce723.p2.behaviorFramework.State;
import edu.afit.csce723.p2.util.Util;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Environment extends Observable implements KeyListener {

	/**
	 * 
	 * @param robotBehavior
	 */
	protected Environment(Behavior robotBehavior) {
        myMap = Maze.getExplorerMap();
        trueRobot = new Robot(myMap.getStart());
        myRobotBehavior = robotBehavior;
        
        errorRobot = new Robot(trueRobot.getPosition());
        errorSensorArray = new HashMap<Double, Double>();
        
        path = new Path();
    }

	/**
	 * Advances the environment one timestep.
	 */
    synchronized protected void step() {
        myRobotBehavior.genAction(new State(trueRobot)).execute(trueRobot);
        trueRobot.step();
        updateRobotPose();
        updateRobotSensors();
        motionErrorModel();
		
		notifyObservers(this);
        path.add(trueRobot.getPosition());
    }

    /**
     * 
     * @param robot
     */
    private void updateRobotPose() {
        for (int i = 0; i < RESOLUTION; i++) {
        	Position offset = delta(trueRobot);
            trueRobot.setPositionOffset(offset);
        }
    }

	/**
     * Shorten each rangeFinder beam to end at the point of intersection with
     * the nearest wall
     */
    private void updateRobotSensors() {
    	// Limits the length of the sensor beam to intersection of the nearest wall
    	Util.trimRobotSensors(trueRobot, myMap);

    	Map<Double, Double> rangeSet = trueRobot.getSensorArray().getRangeReadings();
    	
    	errorSensorArray.clear();
        for (Double angle : rangeSet.keySet()) {
        	errorSensorArray.put(angle, rangeSensorErrorModel(rangeSet.get(angle)));
        }
    }

    /**
     * 
     * @param robot
     * @return
     */
    private double adjustVelocity(Robot robot) {
    	double dv = robot.getVelocity() / RESOLUTION;
    	double dTheta = robot.getTurnRate() / RESOLUTION;
    	double theta = robot.getPosition().getTheta();
    	
    	double dx = dv * Math.cos(theta);
    	double dy = dv * Math.sin(theta);
        Position next = robot.getPosition().add(dx, dy, dTheta);

    	while (!isValid(next)) {
    		dv -= dv/10;
	    	dx = dv * Math.cos(theta);
	    	dy = dv * Math.sin(theta);
        	next = robot.getPosition().add(dx, dy, dTheta);
    	}
    	return dv;
    }
    
    /**
     * 
     * @param robot
     * @return
     */
	private Position delta(Robot robot) {
    	double dv = adjustVelocity(robot);
    	double dTheta = robot.getTurnRate() / RESOLUTION;
    	double theta = robot.getPosition().getTheta();
    	
    	double dx = dv * Math.cos(theta);
    	double dy = dv * Math.sin(theta);
        return new Position(dx, dy, dTheta);
    }
    
	/**
	 * 
	 * @param pose
	 * @return
	 */
    private boolean isValid(Position pose) {
        for (Line2D wall : myMap.getWalls()) {
            if (wall.ptSegDist(pose) < trueRobot.getRadius()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param robot
     */
    private void motionErrorModel() {
    	double dv = adjustVelocity(trueRobot);
    	double dTheta = trueRobot.getTurnRate() / RESOLUTION;
    	if (dv != 0.0 || dTheta != 0.0) {
	    	// First Order Gaussian Markov Acceleration (FOGMA) to turnRate
	    	t[0] = t[1];
	    	t[1] = 0.75 * t[0] + (rand.nextGaussian());
	    	dTheta += t[1]/250;
	
	    	// First Order Gaussian Markov Acceleration (FOGMA) to velocity
	    	v[0] = v[1];
	    	v[1] = 0.75 * v[0] + (rand.nextGaussian()/10);
	    	dv += v[1];
    	}
    	
    	errorRobot.setVelocityDirectly(dv);//.setVelocity(dv);
    	errorRobot.setTurnRateDirectly(dTheta);//setTurnRate(dTheta);
    	
    	double theta = errorRobot.getPosition().getTheta();
    	double dx = dv * Math.cos(theta);
    	double dy = dv * Math.sin(theta);
    	
        errorRobot.setPositionOffset(new Position(dx, dy, dTheta));
    }
    
    /**
     * 
     * @param x
     * @return
     */
    private Double rangeSensorErrorModel(Double x) {
		return x + rand.nextGaussian();
	}
    
    /**
     * The robot's belief about its current position (errors included). 
     * @return The current robot Position (errors included).
     */
    synchronized public Position getRobotPose() {
    	return new Position(errorRobot.getPosition());
    }
    
    /**
     * 
     * @return
     */
    synchronized public double getRobotVelocity() {
    	return errorRobot.getVelocity();
    }
    
    /**
     * 
     * @return
     */
    synchronized public double getRobotTurnRate() {
    	return errorRobot.getTurnRate();
    }
    
    /**
     * 
     * @param newPose
     */
    synchronized public void adjustRobotPose(Position newPose) {
    	if (newPose != null) {
    		errorRobot.setPosition(newPose);
    	}
    }
    
    /**
     * Gets a set of sensor angles and current range sensor readings.
     * @return A set of key/value pairs indicating the angle and current range reading
     */
    synchronized public Map<Double, Double> getRobotSensorReadings() {
    	Map<Double, Double> retVal = new HashMap<Double, Double>();
    	for (Double x : errorSensorArray.keySet()) {
    		retVal.put(new Double(x), new Double(errorSensorArray.get(x)));
    	}
    	return retVal;
    }

    /**
     * 
     * @return
     */
    synchronized protected Maze getMap() {
        return myMap;
    }

    /**
     * 
     * @return
     */
    synchronized protected Robot getRobot() {
    	Robot currentRobot = new Robot(trueRobot.getPosition());
        Util.trimRobotSensors(currentRobot, myMap);
        return currentRobot;
    }

    /**
     * 
     * @return
     */
    synchronized protected Path getPath() {
        return new Path(path);
    }
    
    private final Maze myMap;
    private Robot trueRobot;
    private final Behavior myRobotBehavior;
    private final Robot errorRobot;

	private final Random rand = new Random();
    private double[] v = {rand.nextGaussian(), rand.nextGaussian()};
    private double[] t = {rand.nextGaussian(), rand.nextGaussian()};
    private final Map<Double, Double> errorSensorArray;
    private final Path path;
    private static final int RESOLUTION = 1;

	synchronized public void keyTyped(KeyEvent e) {}

	synchronized public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();

        if (c == 'k') {
        	setNewRandomLocation();
        }
	}

	private void setNewRandomLocation() {
		double x, y, theta;
		Position pose;
		do {
			x = myMap.getWidth() * rand.nextDouble() + myMap.getOffset().width;
			y = myMap.getHeight() * rand.nextDouble() + myMap.getOffset().height;
			theta = 2 * Math.PI * (rand.nextDouble()-0.5);
			pose = new Position(x, y, theta);
		} while (!isValid(pose));

		trueRobot.setPosition(pose);
	}

	public void keyReleased(KeyEvent e) {}
}

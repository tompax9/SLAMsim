/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Aug 8, 2014
 */

package edu.afit.csce723.p2.robotSLAM;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.afit.csce723.p2.errorRobot.Environment;
import edu.afit.csce723.p2.errorRobot.EstimationApproach;
import edu.afit.csce723.p2.errorRobot.Maze;
import edu.afit.csce723.p2.errorRobot.Position;
import edu.afit.csce723.p2.errorRobot.Robot;
import edu.afit.csce723.p2.errorRobot.RobotSLAM;
import edu.afit.csce723.p2.util.Util;

/**
 * @author Sutdent 1
 * @author Student 2
 *
 */
public class MonteCarloLocalization implements EstimationApproach {

	private Position robotPose;
	private double robotVelocity;
	private double robotTurnRate;
	private Map<Double, Double> robotSensorReadings;
	private List<Position> population = new ArrayList<Position>();
	private Random rand = new Random();	
	private Maze theMap;

	/**
     * @param args
     */
    public static void main(String[] args) {
    	new RobotSLAM(new MonteCarloLocalization(), Maze.getExplorerMap()).run();
    }
    
	public void setup(Environment environment, Maze map) {
		theMap = map;		
		environment.registerObserver(this);
		for (int i=0; i<POPULATION_SIZE; i++) {
			population.add(new Position(environment.getRobotPose()));
		}
		f_positionEstimatePanel.setPositions(population);
	}

	public void update(Environment subject) {	
		// This is where you will implement your MCL algorithm.
		
		// You can create any additional classes that you want within the
		// edu.afit.csce723.p2.robotSLAM package, this is your workspace.
		// Do not modify the contents of the other packages.
		
		// The goal is to monitor the environment values to localize the robot
		// within the map, despite motion errors.  This work will form the basis
		// for the fastSLAM particle-based algorithm implemented in Part III.

		robotPose = subject.getRobotPose();						// Where the robot believes that it is.
		robotVelocity = subject.getRobotVelocity();				// The velocity according to the robot.
		robotTurnRate = subject.getRobotTurnRate();				// The turn rate according to the robot.
		robotSensorReadings = subject.getRobotSensorReadings();	// The sensor range readings from the robot.

		// Step 1:  Update population positions based on robot motion
		population = updatePopulationPositionsFromRobotMotion();

		// Step 2:  Score hypothetical population sensor readings against actual robot sensors
		List<Double> scores = new ArrayList<Double>();
		for (Position pose : population) {
			Robot aRobot = new Robot(pose);
			Util.trimRobotSensors(aRobot, theMap);
			
			List<Double> actual = new ArrayList<Double>();
			List<Double> hypothesis = new ArrayList<Double>();
			for (Double key : robotSensorReadings.keySet()) {
				actual.add(robotSensorReadings.get(key));
				hypothesis.add(aRobot.getSensorArray().getRangeReadings().get(key));
			}
			scores.add(Util.euclidianDistance(actual, hypothesis));
		}
		scores = Util.normalizeMinusOne(scores);

		// Step 3: Apply MonteCarlo and SUS selection to form a new population of points
		// population = MonteCarlo(population, scores, POPULATION_SIZE);
		
		// TODO apply SUS to select a new population of points
		population = SUS(population, scores, POPULATION_SIZE);
		
		// TODO How does SUS differ from MonteCarlo selection?
				
		// Once you have a new position estimate, the robot pose can be adjusted
		// by passing a new absolute position
		subject.adjustRobotPose(getPopulationCentroid(population));
		
		// If done right, the path and sensor readings on the upper right will
		// aligned with the actual map---most of the time---GOOD LUCK!!
		
		// Paint the current position distribution on PanelA.
		f_positionEstimatePanel.setPositions(population);
		f_positionEstimatePanel.repaint();
	}

	private List<Position> updatePopulationPositionsFromRobotMotion() {
		// Add random Gaussian noise to the robot motion
		// TODO:  Experiment with sigma values of 0.2, 0.33, 0.66, 0.8, and 1.0
		double sigma = 1.0; 

		List<Position> newPopulation = new ArrayList<Position>();
		for (Position pose : population) {
			// TODO: Here you will perturb the position of the current population
        	newPopulation.add(robotPose);
		}
		return newPopulation;
	}

	private Position getPopulationCentroid(Collection<Position> aPopulation) {
		double x = 0;
		double y = 0;
		double theta = 0;
		for (Position pose : aPopulation) {
			x += pose.getX();
			y += pose.getY();
			theta += pose.getTheta();
		}
		x /= aPopulation.size();
		y /= aPopulation.size();
		theta /= aPopulation.size();
		return new Position(x, y, theta);
	}

	private List<Position> MonteCarlo(List<Position> population, List<Double> scores, int n) {
		if (population.size() != scores.size()) 
			throw new IllegalStateException(
					"Population List and Score List must be the same length!");

		List<Position> keep = new ArrayList<Position>();
		for (int i=0; i<population.size(); i++) {
			// TODO: Here you will need to add your MonteCarlo selection algorithm
			keep.add(new Position(population.get(i)));
		}

		if (keep.size() != n)
			throw new IllegalStateException("Resulting MonteCarlo population size should be " + n + " but is " + keep.size());
		return keep;
	}
	
	private List<Position> SUS(List<Position> population, List<Double> scores, int n) {
		if (population.size() != scores.size()) 
			throw new IllegalStateException(
					"Population List and Score List must be the same length!");

		List<Position> keep = new ArrayList<Position>();
		for (int i=0; i<population.size(); i++) {
			// TODO: Here you will need to add your Stochastic Universal Sampling algorithm
			keep.add(new Position(population.get(i)));
		}

		if (keep.size() != n)
			throw new IllegalStateException("Resulting SUS population size should be " + n + " but is " + keep.size());
		return keep;
	}

	public Component getPositionEstimatePanel() {
		// TODO This panel is registered to the main panel for your use.
		return f_positionEstimatePanel;
	}

	public Component getInternalMapPanel() {
		// TODO This panel is registered to the main panel for your use.
		return f_internalMapPanel;
	}

	// For rendering the population's distribution of positions
	private final PositionEstimatePanel f_positionEstimatePanel = new PositionEstimatePanel();
	// For rendering the systems internal map
	private final InternalMapPanel f_internalMapPanel = new InternalMapPanel();

	private static final int POPULATION_SIZE = 1000;
}

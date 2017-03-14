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
import java.util.List;
import java.util.Random;

import edu.afit.csce723.p2.errorRobot.Environment;
import edu.afit.csce723.p2.errorRobot.Maze;
import edu.afit.csce723.p2.errorRobot.Observer;
import edu.afit.csce723.p2.errorRobot.Position;
import edu.afit.csce723.p2.errorRobot.RobotSLAM;

/**
 * @author Sutdent 1
 * @author Student 2
 *
 */
public class KidnappedRobot implements Observer<Environment> {

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
	}

	public void update(Environment subject) {
		subject.getRobotPose();		 // Where the robot believes that it is. 
		subject.getRobotVelocity();  // The velocity according to the robot. 
		subject.getRobotTurnRate();  // The turn rate according to the robot. 
		subject.getRobotSensorReadings(); // The sensor range readings from the robot. 
		
		// TODO This is where you will implement your kidnaped robot algorithm.
		
		// You can create any additional classes that you want within the
		// edu.afit.csce723.p2.robotSLAM package, this is your workspace.
		// Do not modify the contents of the other packages.
		
		// The goal is to monitor the environment values  to simultaneously build a
		// map and localize your self using the fastSLAM particle-based algorithm.
				
		// Once you have a new position estimate, the robot pose can be adjusted
		// by passing a position offset--NOT a new absolute position--
		subject.adjustRobotPose(null);
		
		// If done right, the path and sensor readings on the upper right will
		// aligned with the actual map, even when the robot is kidnaped.  
		// ---GOOD LUCK!!--
		
		// There are two JPanels registered to the main window for you to paint on. 
		f_positionEstimatePanel.repaint();
		f_internalMapPanel.repaint();
	}

	public Component getPanelA() {
		// TODO This panel is registered to the main panel for your use.
		return f_positionEstimatePanel;
	}

	public Component getPanelB() {
		// TODO This panel is registered to the main panel for your use.
		return f_internalMapPanel;
	}

	private final PositionEstimatePanel f_positionEstimatePanel = new PositionEstimatePanel();  // For rendering the population's distribution of positions
	private final InternalMapPanel f_internalMapPanel = new InternalMapPanel();  // For rendering the systems internal map
}

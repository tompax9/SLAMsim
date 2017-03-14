/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Jul 15, 2015
 */

package edu.afit.csce723.p2.errorRobot;

import java.awt.Component;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public interface EstimationApproach extends Observer<Environment> {

	/**
	 * 
	 * @param environment
	 * @param map
	 */
	public void setup(Environment environment, Maze map);
	
	/**
	 * 
	 * @return
	 */
	public Component getPositionEstimatePanel();
	
	/**
	 * 
	 * @return
	 */
	public Component getInternalMapPanel();

}

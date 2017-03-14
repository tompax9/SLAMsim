/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Aug 19, 2014
 */

package edu.afit.csce723.p2.robotSLAM;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JPanel;

import edu.afit.csce723.p2.errorRobot.Maze;
import edu.afit.csce723.p2.errorRobot.MazeRenderingTool;
import edu.afit.csce723.p2.errorRobot.Position;
import edu.afit.csce723.p2.errorRobot.Robot;

/**
 * @author Sutdent 1
 * @author Student 2
 *
 */
public class PositionEstimatePanel extends JPanel {

	synchronized public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(-theMap.getOffset().width, -theMap.getOffset().height);
        renderer.drawTheMaze(theMap, g, getSize());
        if (positions != null) {
        	renderer.renderPositions(positions, 1, Color.RED, g, getSize());
        }
    }

	synchronized public void setPositions(Collection<Position> population) {
		positions.clear();
		for (Position pose : population) {
			positions.add(new Position(pose));		
		}
	}

	private Maze theMap = Maze.getExplorerMap();
    private Robot theRobot;
    private Collection<Position> positions = new HashSet<Position>();
    private MazeRenderingTool renderer = new MazeRenderingTool();
    /**
	 * 
	 */
	private static final long serialVersionUID = -3728034301044215454L;
}

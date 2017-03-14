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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import javax.swing.JPanel;

import edu.afit.csce723.p2.errorRobot.MazeRenderingTool;
import edu.afit.csce723.p2.errorRobot.Robot;
import edu.afit.csce723.p2.gridMap.SimpleMap;

/**
 * @author Sutdent 1
 * @author Student 2
 *
 */
public class InternalMapPanel extends JPanel {

	synchronized public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.setBackground(Color.BLACK, g, getSize());
        for (Point2D pt : gridMap.getPoints()) {
        	renderer.drawPoint(pt, 1, Color.WHITE, g, getSize());
        }
    }

	public void internalMapUpdate(Robot aRobot) {
		for (Line2D beam : aRobot.getSensorArray().getBeams()) {
			gridMap.reinforce(beam.getP2());
		}
	}
	
	public void internalMapUpdate(Collection<Point2D> points) {
		for (Point2D point : points) {
			gridMap.reinforce(point);
		}
	}

	private SimpleMap gridMap = new SimpleMap();
    private MazeRenderingTool renderer = new MazeRenderingTool();

    /**
	 * 
	 */
	private static final long serialVersionUID = -8615102274248226257L;
}

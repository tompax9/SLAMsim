/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class MazeRenderingTool {

    private Maze theMaze = Maze.getExplorerMap();
    private Robot theRobot;
    private Collection<Position> theArchive;
    private Dimension panelSize;
    private final int f_padding = 10;

    public void renderEnvironment(Maze maze, Position pose, Path path, Graphics g, Dimension size) {
    	assert (theMaze != null);
    	theMaze = maze;
    	theRobot = new Robot(pose);
    	
        setSize(size);

        drawTheMaze(theMaze, g, size);
//        drawTheBreadcrumbTrail(theMaze, g, size);

        drawRobotPath(path, g, size);

        drawRangefinderBeams(theRobot, g, size);
        drawTheRobot(theRobot, g, size);
        drawTheRobotHeading(theRobot, g, size);
    }

    public void renderEnvironment(Maze theMaze, Robot theRobot, Path path, Graphics g, Dimension size) {
        this.theMaze = theMaze;
        this.theRobot = theRobot;
        setSize(size);
        
        Dimension offset = theMaze.getOffset();
        g.translate(-offset.width, -offset.height);

        drawTheMaze(theMaze, g, size);

        if (showPath) {
        	drawRobotPath(path, g, size);
        }

        if (showRobot) {
        	drawRangefinderBeams(theRobot, g, size);
        	drawTheRobot(theRobot, g, size);
        	drawTheRobotHeading(theRobot, g, size);
        }
    }

    public void renderEnvironment(Maze theMaze, Robot theRobot, Graphics g, Dimension size) {
    	renderEnvironment(theMaze, theRobot, new Path(), g, size);
    }

    public void renderEnvironment(Maze theMaze, Path path, Graphics g, Dimension size) {
        this.theMaze = theMaze;

        drawTheMaze(theMaze, g, size);

        if (theArchive != null) {
            drawPositionPoints(theArchive, g, size);
        }

        if (path != null) {
            drawRobotPath(path, g, size);
        }
    }

	public void renderEnvironment(Maze theMaze, Path path, int currentTimeStep, Graphics g, Dimension size) {
		Robot robot = new Robot(path.get(currentTimeStep));
		renderEnvironment(theMaze, robot, path, g, size);
	}

	public void renderPositions(Collection<Position> points, int aSize, Color aColor, Graphics g, Dimension size) {
		for (Position p : points) {
			drawPoint(p.getPoint2D(), aSize, aColor, g, size, theMaze);
		}
	}
	
	public void renderPoints(Maze theMaze, Collection<Point2D> points, int aSize, Color aColor, Graphics g, Dimension size) {
        drawPoints(points, aSize, aColor, g, size);
    }

    private Line2D scale(Line2D line, double width, double height) {
        Point2D p1 = scale(line.getP1(), width, height),
                p2 = scale(line.getP2(), width, height);
        return new Line2D.Double(p1, p2);
    }

    private Point2D scale(Point2D pt, double width, double height) {
        int x = scale(pt.getX(), width, height),
                y = scale(pt.getY(), width, height);
        return new Point2D.Double(x, y);
    }

    private int scale(double a, double width, double height) {
        double frac = width / height;

        if (getWidth() / frac < getHeight()) {
            return (int) (a * (getWidth() - f_padding) / width); 
        } else {
            return (int) (a * (getHeight() - f_padding) / height);
        }
    }

    private int getWidth() {
        return panelSize.width;
    }

    private int getHeight() {
        return panelSize.height;
    }

    private void setSize(Dimension size) {
        panelSize = size;
    }

    public void drawTheMaze(Maze theMaze, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        // Draw the walls of the maze map
        g.setColor(Color.DARK_GRAY);
        for (Line2D wall : theMaze.getWalls()) {
            g2.draw(scale(wall, theMaze.getWidth(), theMaze.getHeight()));
        }
    }

    private void drawRangefinderBeams(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        g2.setColor(Color.RED);
        for (Line2D beam : theRobot.getSensorArray().getBeams()) {
            g2.draw(scale(beam, theMaze.getWidth(), theMaze.getHeight()));
        }
    }

    private void drawTheRobot(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        double width = theMaze.getWidth();
        double height = theMaze.getHeight();

        int x, y, dx, dy;
        x = scale(theRobot.getPosition().getX() - theRobot.getRadius(), width, height);
        y = scale(theRobot.getPosition().getY() - theRobot.getRadius(), width, height);
        dx = scale(2 * theRobot.getRadius(), width, height);
        dy = scale(2 * theRobot.getRadius(), width, height);
        g2.setColor(Color.BLUE);
        g2.fill(new Ellipse2D.Double(x, y, dx, dy));

    }

    private void drawTheRobotHeading(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

      double width = theMaze.getWidth();
      double height = theMaze.getHeight();

        int x, y, dx, dy;
        x = scale(theRobot.getPosition().getX(), width, height);
        y = scale(theRobot.getPosition().getY(), width, height);
        dx = x + scale(theRobot.getRadius() * Math.cos(theRobot.getPosition().getTheta()), width, height);
        dy = y + scale(theRobot.getRadius() * Math.sin(theRobot.getPosition().getTheta()), width, height);
        g2.setColor(Color.WHITE);
        g2.draw(new Line2D.Double(x, y, dx, dy));
    }

    private void drawPositionPoints(Collection<Position> positionPoints, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        double width = theMaze.getWidth();
        double height = theMaze.getHeight();

        int x, y, dx, dy;
        g2.setColor(Color.DARK_GRAY);
        for (Position pt : positionPoints) {
            x = scale(pt.getX() - 1, width, height);
            y = scale(pt.getY() - 1, width, height);
            dx = 2;
            dy = 2;
            g2.fill(new Ellipse2D.Double(x, y, dx, dy));
        }
    }

    private void drawRobotPath(Path thePath, Graphics g, Dimension size) {
        // Draw the robot path as positions that fade from dark gray (55, 55, 55) to white (255, 255, 255)
        double color = 200.0;
        double step = color / thePath.size();

        Color aColor = new Color((int) color, (int) color, (int) color);
        for (Position pt : thePath) {
        	aColor = new Color((int) color, (int) color, (int) color);
        	drawPoint(pt, 3, aColor, g, size, theMaze);
            color -= step;
            color = Math.max(Math.min(color, 255), 0);
        }
    }

	public void renderSensorEnvironment(Maze maze, Collection<Point2D> sensorPoints, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);
        
        drawPoints(sensorPoints, 1, Color.RED, g2, size);
	}

	public void setBackground(Color aColor, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);
        g2.setColor(aColor);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    }

    private void drawPoints(Collection<Point2D> points, int radius, Color aColor, Graphics g, Dimension size) {
        for (Point2D pt : points) {
        	drawPoint(pt, radius, aColor, g, size, theMaze);
        }
    }
    
    private void drawPoint(Point2D pt, int aRadius, Color aColor, Graphics g, Dimension size, Maze aMaze) {
        double width = aMaze.getWidth();
        double height = aMaze.getHeight();

        drawPoint(pt,  aRadius, aColor, g, size, width, height);
    }

    public void drawPoint(Point2D pt, int aRadius, Color aColor, Graphics g, Dimension size) {
    	drawPoint(pt, aRadius, aColor, g, size, size.getWidth(), size.getHeight());
    }

    private void drawPoint(Point2D pt, int aRadius, Color aColor, Graphics g, Dimension size, double width, double height) {
        // The minimum radius size is 1. 
    	if (aRadius < 1) 
    		return;
    	
    	Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        g2.setColor(aColor);

        x = scale(pt.getX() - aRadius, width, height);
        y = scale(pt.getY() - aRadius, width, height);
        dx = scale(2 * aRadius + 1, width, height);
        dy = scale(2 * aRadius + 1, width, height);
        g2.fill(new Ellipse2D.Double(x, y, dx, dy));
    }

    private boolean showRobot = true;
	public void setShowRobot(boolean selected) {
		showRobot = selected;
	}

	private boolean showPath = true;
	public void setShowPath(boolean selected) {
		showPath = selected;
	}
}

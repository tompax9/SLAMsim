/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.awt.geom.Point2D;

import edu.afit.csce723.p2.util.Util;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Position extends Point2D {

    public Position(double x, double y, double theta) {
      setLocation(x, y);
      this.theta = Util.normalize(theta);
    }

    public Position(Position aPosition) {
    	this(aPosition.x, aPosition.y, aPosition.theta); 
	}

	public boolean equals(Position pose) {
        boolean equal = true;
        if (x != pose.x) {
            equal = false;
        }
        if (y != pose.y) {
            equal = false;
        }
        if (theta != pose.theta) {
            equal = false;
        }
        return equal;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
     * Returns the Theta angle of this <code>Position</code> in <code>double</code> precision [-Pi .. Pi].
     * @return The Theta angle of this <code>Position</code>.
     */
    public double getTheta() {
        return theta;
    }

    /**
     * Adds the vector values of two Position objects 
     * @param other The other position to add to this one.
     * @return The new Position object
     */
    protected Position add(Position other) {
    	return add(other.x, other.y, other.theta);
    }
    
    public Position add(double dx, double dy, double dTheta) {
        double x = this.x + dx,
               y = this.y + dy,
               t = Util.normalize(theta + dTheta);
        return new Position(x, y, t);
    }
    
    private double x, y, theta;

	public Point2D getPoint2D() {
		return this;
//		return new Point2D.Double(x, y);
	}
}

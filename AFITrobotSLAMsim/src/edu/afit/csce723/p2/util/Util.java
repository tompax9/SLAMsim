/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.afit.csce723.p2.errorRobot.Maze;
import edu.afit.csce723.p2.errorRobot.Position;
import edu.afit.csce723.p2.errorRobot.Robot;


/**
 * 
 * @author Brian Woolley (brian.woolley@ieee.org)
 *
 */
public class Util {

    private Util() {
    }
    private static Util thisInstance = new Util();

    protected static Util getInstance() {
        return thisInstance;
    }

    /**
     * Calculates the radian angle from point a to point b .
     * @param a The reference point.
     * @param b Another point in the space.
     * @return The radian angle from point a to point b.
     */
    private static double angleOf(Point2D a, Point2D b) {
        // Calculate angle from a to b
        double x = b.getX() - a.getX();
        double y = b.getY() - a.getY();
        return Math.atan2(y, x);
    }

    /**
     * Adds two radian angles and returns a value between -Pi and Pi.
     * @param a The first radian angle.
     * @param b Another radian angle.
     * @return The sum of two radian angles, normalized to a value between -Pi and Pi.
     */
    private static double addAngles(double a, double b) {
        return Util.normalize(a + b);
    }

    /**
     * Adjusts radian angles to be values between -Pi and Pi.
     * @param raw The raw radian angle to be adjusted.
     * @return The adjusted radian angle (values are between -Pi and Pi).
     */
    public static double normalize(double raw) {
        while (raw < -Math.PI) {
            raw += 2 * Math.PI;
        }
        while (raw > Math.PI) {
            raw -= 2 * Math.PI;
        }
        return raw;
    }

    /**
     * Computes the intersection between two lines. The calculated point is approximate, 
     * since integers are used. If you need a more precise result, use doubles
     * everywhere. 
     * (c) 2007 Alexander Hristov. Use Freely (LGPL license). http://www.ahristov.com
     *
     * @param x1 Point 1 of Line 1
     * @param y1 Point 1 of Line 1
     * @param x2 Point 2 of Line 1
     * @param y2 Point 2 of Line 1
     * @param x3 Point 1 of Line 2
     * @param y3 Point 1 of Line 2
     * @param x4 Point 2 of Line 2
     * @param y4 Point 2 of Line 2
     * @return Point where the segments intersect, or null if they don't
     */
    public static Point2D intersection(Line2D a, Line2D b) {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(),
                x3 = b.getX1(), y3 = b.getY1(), x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }
    
    /**
     * 
     * @param aRobot
     * @param aMap
     */
	public static void trimRobotSensors(Robot aRobot, Maze aMap) {
    	// Limits the length of the sensor beam to intersection of the nearest wall
        for (Line2D beam : aRobot.getSensorArray().getBeams()) {
            for (Line2D wall : aMap.getWalls()) {
                if (beam.intersectsLine(wall)) {
                    Point2D p1 = beam.getP1();
                    Point2D p2 = Util.intersection(beam, wall);
                    if (p1 != null && p2 != null) {
                    	beam.setLine(p1, p2);
                    }
                }
            }
        }
	}

	/**
	 * 
	 * @param scores
	 * @return
	 */
	public static double sum(List<Double> scores) {
		double f = 0.0;
		for (Double score : scores) {
			f += score;
		}
		return f;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
    public static List<Double> normalizeMinusOne(List<Double> values) {
    	List<Double> retVal = new ArrayList<Double>();
    	for (Double x : normalize(values)) {
        	retVal.add(1.0 - x);
    	}
    	return retVal;    	
    }
    
	/**
	 * 
	 * @param values
	 * @return
	 */
    public static List<Double> normalize(List<Double> values) {
    	double min = Double.MAX_VALUE;
    	double max = Double.MIN_VALUE;
    	for (Double x : values) {
    		min = Math.min(x, min);
    		max = Math.max(x, max);
    	}

    	List<Double> retVal = new ArrayList<Double>();
    	for (Double x : values) {
        	if (min == max) {
        		retVal.add(Math.random());
        	} else {
        		retVal.add((x - min)/(max - min));
        	}
    	}
    	return retVal;
    }

	/**
	 * Computes the n-dimensional Euclidian distance between two sets.  The primary
	 * intent is to measure the distance between the actual robot sensor readings
	 * and the sensor readings from a hypothetical robot position. 
	 * @param alpha An ordered set of decimal values.
	 * @param beta An ordered set of decimal values.
	 * @return A decimal value denoting the distance between the two sets 
	 */
	public static double euclidianDistance(List<Double> alpha, List<Double> beta) {
		if (alpha.size() != beta.size())
			throw new IllegalStateException(
					"Size of the actual measurement list and the hypothesis measurment list must be equal.");
		double euclidianDist = 0.0;	
		for (int i=0; i<alpha.size(); i++) {
			euclidianDist += Math.pow(alpha.get(i) - beta.get(i), 2);
		}
		return Math.sqrt(euclidianDist);
	}

	// Cartesian 
	private static Point2D convertToCartesian(Position pose, Double angle, Double range) {
		Double x = pose.getX();
		Double y = pose.getY();
		
		Double theta = Util.normalize(pose.getTheta() + angle);
		Double dx = Math.cos(theta) * range;
		Double dy = Math.sin(theta) * range;
		
		return new Point2D.Double(x + dx, y+ dy);
	}

	public static Collection<Point2D> convertToCartesian(Position robotPose, Map<Double, Double> rangeReadings) {
		Collection<Point2D> retVal = new HashSet<Point2D>();
		for (Double angleKey : rangeReadings.keySet()) {
			retVal.add(convertToCartesian(robotPose, angleKey, rangeReadings.get(angleKey)));
		}
		return retVal;
	}

}

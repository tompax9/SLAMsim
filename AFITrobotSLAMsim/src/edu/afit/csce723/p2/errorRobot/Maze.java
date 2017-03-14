/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.*;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class Maze {

    protected static final int HARD_MAP = 0,
            MEDIUM_MAP = 1,
            EXPLORER_MAP = 3;

    private Maze(int mapType) {
        if (mapType == HARD_MAP) {
            setupHardMap();
        }
        if (mapType == MEDIUM_MAP) {
            setupMedMap();
        }
        if (mapType == EXPLORER_MAP) {
        	setupExplorerMap();
        }
        f_walls = Collections.unmodifiableCollection(f_walls);
        f_offset = computeOffset();
    }
    
    /**
     * 
     * @return 
     */
    public static Maze getMediumMap() {
        return instanceOfMediumMap;
    }
    
    /**
     * 
     * @return 
     */
    public static Maze getHardMap() {
        return instanceOfHardMap;
    }
    
    public static Maze getExplorerMap() {
    	return instanceOfExplorerMap;
    }
    
    private void calculateDimensions() {
        int minX = Integer.MAX_VALUE,
        	minY = Integer.MAX_VALUE,
        	maxX = Integer.MIN_VALUE,
        	maxY = Integer.MIN_VALUE;
        for (Line2D wall : f_walls) {
            minX = (int) Math.min(minX, Math.min(wall.getX1(), wall.getX2()));
            minY = (int) Math.min(minY, Math.min(wall.getY1(), wall.getY2()));
            
            maxX = (int) Math.max(maxX, Math.max(wall.getX1(), wall.getX2()));
            maxY = (int) Math.max(maxY, Math.max(wall.getY1(), wall.getY2()));
        }
        f_dimensions = new Dimension(maxX - minX, maxY - minY);
    }

    public Dimension getSize() {
        return f_dimensions;
    }

    public Dimension getOffset() {
    	return f_offset;
    }
    
    private Dimension computeOffset() {
    	int minX = Integer.MAX_VALUE,
    		minY = Integer.MAX_VALUE;
    	for (Line2D wall : f_walls) {
    		minX = (int) Math.min(minX, Math.min(wall.getX1(), wall.getX2()));
    		minY = (int) Math.min(minY, Math.min(wall.getY1(), wall.getY2()));
    	}
    	return new Dimension(minX, minY);
    }
    
    public int getWidth() {
        return getSize().width;
    }

    public int getHeight() {
        return getSize().height;
    }

    public Collection<Line2D> getWalls() {
        return f_walls;
    }

    public Position getStart() {
        return f_start;
    }

    private void setupExplorerMap() {
        f_start = new Position(36, 384, 0);

        f_walls.clear();
        // Outer Walls
        f_walls.add(new Line2D.Double(-40, -40, 440, -40)); // wall #TOP
        f_walls.add(new Line2D.Double(-40, 440, 440, 440)); // wall #BOTTOM
        f_walls.add(new Line2D.Double(-40, -40, -40, 440)); // wall #LEFT
        f_walls.add(new Line2D.Double(440, -40, 440, 440)); // wall #RIGHT

        // Upper Left Quadrant
        f_walls.add(new Line2D.Double(0, 0, 0, 200)); // wall #2
        f_walls.add(new Line2D.Double(0, 150, 57, 150)); // wall #3
        f_walls.add(new Line2D.Double(0, 0, 200, 0)); // wall #4
        f_walls.add(new Line2D.Double(57, 150, 57, 43)); // wall #6 
        f_walls.add(new Line2D.Double(57, 94, 158, 38)); // wall #7
        f_walls.add(new Line2D.Double(77, 0, 108, 36)); // wall #8
        f_walls.add(new Line2D.Double(0, 120, 33, 79)); // wall #9
        f_walls.add(new Line2D.Double(200, 54, 95, 109)); // wall #10
        f_walls.add(new Line2D.Double(57, 150, 135, 170)); // wall #11

        // Lower Left Quadrant
        f_walls.add(new Line2D.Double(0, 200, 0, 400)); // wall #2
        f_walls.add(new Line2D.Double(0, 250, 57, 250)); // wall #3
        f_walls.add(new Line2D.Double(77, 400, 200, 400)); // wall #4
        f_walls.add(new Line2D.Double(57, 250, 57, 357)); // wall #6
        f_walls.add(new Line2D.Double(57, 306, 158, 362)); // wall #7
        f_walls.add(new Line2D.Double(77, 400, 108, 364)); // wall #8
        f_walls.add(new Line2D.Double(0, 280, 33, 321)); // wall #9
        f_walls.add(new Line2D.Double(200, 346, 95, 291)); // wall #10
        f_walls.add(new Line2D.Double(57, 250, 135, 230)); // wall #11

        // Upper Right Quadrant
        f_walls.add(new Line2D.Double(400, 0, 400, 150)); // wall #5
        f_walls.add(new Line2D.Double(400, 150, 343, 150)); // wall #3
        f_walls.add(new Line2D.Double(400, 0, 200, 0)); // wall #4
        f_walls.add(new Line2D.Double(343, 150, 343, 43)); // wall #6
        f_walls.add(new Line2D.Double(343, 94, 242, 38)); // wall #7
        f_walls.add(new Line2D.Double(323, 0, 292, 36)); // wall #8
        f_walls.add(new Line2D.Double(400, 120, 367, 79)); // wall #9
        f_walls.add(new Line2D.Double(200, 54, 305, 109)); // wall #10
        f_walls.add(new Line2D.Double(343, 150, 265, 170)); // wall #11

        // Lower Right Quadrant
        f_walls.add(new Line2D.Double(400, 250, 400, 400)); // wall #5
        f_walls.add(new Line2D.Double(400, 250, 343, 250)); // wall #3
        f_walls.add(new Line2D.Double(400, 400, 200, 400)); // wall #4
        f_walls.add(new Line2D.Double(343, 250, 343, 357)); // wall #6
        f_walls.add(new Line2D.Double(343, 306, 242, 362)); // wall #7
        f_walls.add(new Line2D.Double(323, 400, 292, 364)); // wall #8
        f_walls.add(new Line2D.Double(400, 280, 367, 321)); // wall #9
        f_walls.add(new Line2D.Double(200, 346, 305, 291)); // wall #10
        f_walls.add(new Line2D.Double(343, 250, 265, 230)); // wall #11

        // Asymmetric Walls
        f_walls.add(new Line2D.Double(95, 291, 305, 291)); // wall #x
        f_walls.add(new Line2D.Double(200, 140, 95, 109)); // wall #y
        f_walls.add(new Line2D.Double(200, 140, 305, 109)); // wall #z

        calculateDimensions();
    }

    private void setupHardMap() {
        f_start = new Position(36, 184, 0);

        f_walls.clear();
        f_walls.add(new Line2D.Double(200, 0, 0, 0));
        f_walls.add(new Line2D.Double(0, 0, 0, 200));
        f_walls.add(new Line2D.Double(0, 50, 57, 50));
        f_walls.add(new Line2D.Double(0, 200, 200, 200));
        f_walls.add(new Line2D.Double(200, 200, 200, 0));
        f_walls.add(new Line2D.Double(57, 50, 57, 157));
        f_walls.add(new Line2D.Double(57, 106, 158, 162));
        f_walls.add(new Line2D.Double(77, 200, 108, 164));
        f_walls.add(new Line2D.Double(0, 80, 33, 121));
        f_walls.add(new Line2D.Double(200, 146, 95, 91));
        f_walls.add(new Line2D.Double(57, 50, 135, 30));
        calculateDimensions();
    }

    private void setupMedMap() {
        f_start = new Position(30, 22, 0);

        f_walls.clear();
        f_walls.add(new Line2D.Double(300, 0, 300, 135));
        f_walls.add(new Line2D.Double(300, 135, 0, 135));
        f_walls.add(new Line2D.Double(0, 135, 0, 0));
        f_walls.add(new Line2D.Double(0, 0, 300, 0));
        f_walls.add(new Line2D.Double(241, 135, 58, 65));
        f_walls.add(new Line2D.Double(114, 0, 73, 42));
        f_walls.add(new Line2D.Double(134, 94, 104, 49)); //
        f_walls.add(new Line2D.Double(196, 0, 139, 51));
        f_walls.add(new Line2D.Double(217, 126, 180, 67)); //
        f_walls.add(new Line2D.Double(267, 0, 214, 63));
        f_walls.add(new Line2D.Double(271, 135, 237, 88));
        calculateDimensions();
    }

    private static Maze instanceOfMediumMap = new Maze(MEDIUM_MAP);    
    private static Maze instanceOfHardMap = new Maze(HARD_MAP);
    private static Maze instanceOfExplorerMap = new Maze(EXPLORER_MAP);
    
    private Collection<Line2D> f_walls = new HashSet<Line2D>();
    private Dimension f_dimensions, f_offset;
    private Position f_start;
}

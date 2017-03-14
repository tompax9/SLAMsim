/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.util.ArrayList;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class Path extends ArrayList<Position> {

	public Path() {}
    
    Path(Path path) {
        addAll(path);
    }

    @Override
    public Position get(int index) {
    	if (index >= size()) 
    		return getLast();
    	else 
    		return super.get(index);
    }
    
    public Position getLast() {
        return get(size()-1);
    }

    /** Auto-generated Serial-ID */
	private static final long serialVersionUID = 6967085243092065997L;
}

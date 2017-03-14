/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class Observable {
    
	public void notifyObservers(Environment env) {
		for(Observer o : observers) {
			o.update(env);
		}
	}
	
	public void registerObserver(Observer o) {
		observers.add(o);
	}
	
	public boolean removeObserver(Observer o) {
		return observers.remove(o);
	}
	
	private Set<Observer> observers = new HashSet<Observer>();
}

/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.behaviorFramework;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class KeyboardCtlr implements Behavior, KeyListener {

    private Action currentAction = new Action();

    synchronized public Action genAction(State currentState) {
        return currentAction;
    }

    synchronized public void keyPressed(KeyEvent e) {
        // Actively move in this direction
        char c = e.getKeyChar();

        if (c == 'd') {
            currentAction.setTurnRate(1.0);
        } else if (c == 'a') {
            currentAction.setTurnRate(-1.0);
        } else if (c == 'w') {
            currentAction.setVelocity(1.0);
        } else if (c == 's') {
            currentAction.setVelocity(-1.0);
        }
    }

    synchronized public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();

        if (c == 'd') {
            currentAction.setTurnRate(Math.min(currentAction.turnRate, 0.0));
        } else if (c == 'a') {
            currentAction.setTurnRate(Math.max(currentAction.turnRate, 0.0));
        } else if (c == 'w') {
            currentAction.setVelocity(Math.min(currentAction.velocity, 0.0));
        } else if (c == 's') {
            currentAction.setVelocity(Math.max(currentAction.velocity, 0.0));
        }
    }

    synchronized public void keyTyped(KeyEvent arg0) {
        // nothing to do here		
    }
}

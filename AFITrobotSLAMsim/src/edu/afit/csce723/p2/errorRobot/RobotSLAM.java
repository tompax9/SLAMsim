/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.afit.csce723.p2.errorRobot;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.afit.csce723.p2.behaviorFramework.KeyboardCtlr;
import edu.afit.csce723.p2.robotSLAM.MonteCarloLocalization;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class RobotSLAM extends JFrame {

	public RobotSLAM(EstimationApproach slamAlgorithm, Maze theMap) {
        super("Robot SLAM Simulation Viewer");

        if (slamAlgorithm == null)
        	throw new IllegalStateException("The EstimationApproach was not specified.");
        
        if (theMap == null)
        	theMap = Maze.getExplorerMap();
        
        KeyboardCtlr f_keyboardCtlr = new KeyboardCtlr();
        environment = new Environment(f_keyboardCtlr);

        slamAlgorithm.setup(environment, Maze.getExplorerMap());

        JTextField f_input = new JTextField();
        f_input.addKeyListener(f_keyboardCtlr);
        f_input.addKeyListener(environment);

        JPanel mainPanel = new JPanel(new GridLayout(2,2));
        mainPanel.add(new EnvPanel(environment));
        mainPanel.add(new MapPanel(environment));
        mainPanel.add(slamAlgorithm.getPositionEstimatePanel());
        mainPanel.add(slamAlgorithm.getInternalMapPanel());
        
        getContentPane().add(f_input, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setSize(840, 840);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void run() {
        long hack, delay;
        while(true) {
            try {
                hack = System.currentTimeMillis();
                environment.step();

                delay = Math.min(System.currentTimeMillis() - hack, 100);
                Thread.sleep(100 - delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(RobotSLAM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private final Environment environment;

    /**
     * *
     */
    private static final long serialVersionUID = 6102710692892382207L;
}

/*
 * $Id: ClockTick.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

/**
 * This class mimics the ticking of a clock.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
abstract class ClockTick extends AbstractAction {
    private static final String CLASSNAME = ClockTick.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(ClockTick.class.getPackage().getName());

    /** The value of one second, expressed in milliseconds. */
    public static final long SECOND = 1000;
    
    /** The value of one minute, expressed in milliseconds. */
    public static final long MINUTE = SECOND * 60;
    
    /** The value of one hour, expressed in milliseconds. */
    public static final long HOUR = MINUTE * 60;
    
    private long startMillis;
    
    private long elapsedMillis;
    
    ClockTick() {
        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName);

        startMillis = System.currentTimeMillis();

        LOGGER.exiting(CLASSNAME, methodName);
    }

    /**
     * Updates a digital clock representation in the user interface.
     *
     * @param clockFace the UI clock's display string
     */
    public abstract void updateClock(String clockFace);

    /**
     * @{inheritDoc}
     *
     * @param e the event that triggered this event listener
     */
    public void actionPerformed(ActionEvent e) {
        // no logging here to minimize execution time
        elapsedMillis = System.currentTimeMillis() - startMillis;

        long hours = elapsedMillis / HOUR;
        long mins = (elapsedMillis % HOUR) / MINUTE;
        long secs = (elapsedMillis % MINUTE) / SECOND;
        
        String clockFace = new StringBuffer()
        .append(hours < 10 ? "0" + hours : hours).append(":")
        .append(mins < 10 ? "0" + mins : mins).append(":")
        .append(secs < 10 ? "0" + secs : secs)
        .toString();
        
        updateClock(clockFace);
    }
}

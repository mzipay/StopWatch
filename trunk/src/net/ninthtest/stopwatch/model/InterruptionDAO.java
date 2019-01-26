/*
 * $Id: InterruptionDAO.java 80 2008-04-05 05:07:24Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.util.List;

/**
 * This interface declares the contract for access to interruption data.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 80 $
 */
public interface InterruptionDAO {
    /**
     * Returns all known interruptions.
     *
     * @return a list of interruptions
     * @throws DataAccessException if access to the data store fails
     */
    public List findAllInterruptions() throws DataAccessException;

    /**
     * Returns all known interruptions for a specified task.
     *
     * @param task the task being queried
     * @return a list of interruptions for the specified task
     * @throws DataAccessException if access to the data store fails
     */
    public List findInterruptionsForTask(Task task) throws DataAccessException;
    
    /**
     * Stores the data describing an interruption.
     *
     * @param intr the interruption to be stored
     * @throws DataAccessException if access to the data store fails
     */
    public void saveInterruption(Interruption intr) throws DataAccessException;
}

/*
 * $Id: TaskDAO.java 80 2008-04-05 05:07:24Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.util.Date;
import java.util.List;

/**
 * This interface declares the contract for access to task data.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 80 $
 */
public interface TaskDAO {
    /**
     * Returns all known tasks.
     *
     * @return a list of tasks
     * @throws DataAccessException if access to the data store fails
     */
    public List findAllTasks() throws DataAccessException;
    
    /**
     * Returns all known tasks in a specified date range.
     *
     * @param startDate the start date in the date range (inclusive)
     * @param endDate the end date in the date range (exclusive)
     * @return a list of tasks in the specified date range
     * @throws DataAccessException if access to the data store fails
     */
    public List findAllTasksInDateRange(Date startDate, Date endDate) throws DataAccessException;
    
    /**
     * Stores the data describing a task.
     *
     * @param task the task to be stored
     * @throws DataAccessException if access to the data store fails
     */
    public void saveTask(Task task) throws DataAccessException;
}

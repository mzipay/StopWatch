/*
 * $Id: JDBCTaskDAO.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides JDBC access to task data.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class JDBCTaskDAO implements TaskDAO {
    private static final String CLASSNAME = JDBCTaskDAO.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(JDBCTaskDAO.class.getPackage().getName());
    
    private static final String FIND_ALL_TASKS_SQL;
    
    private static final String FIND_ALL_TASKS_IN_DATE_RANGE_SQL;
    
    private static final String INSERT_TASK_SQL;
    
    private static final String UPDATE_TASK_SQL;
    
    static {
        final String methodName = "<clinit>";
        LOGGER.entering(CLASSNAME, methodName);
        
        Properties sqlProps = new Properties();
        try {
            sqlProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("task-sql.properties"));
        } catch (IOException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error loading JDBCTaskDAO SQL properties", ex);
        }
        
        FIND_ALL_TASKS_SQL = sqlProps.getProperty("findAllTasks");
        FIND_ALL_TASKS_IN_DATE_RANGE_SQL = sqlProps.getProperty("findAllTasksInDateRange");
        INSERT_TASK_SQL = sqlProps.getProperty("insertTask");
        UPDATE_TASK_SQL = sqlProps.getProperty("updateTask");
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    /**
     * {@inheritDoc}
     *
     * @return a list of tasks
     * @throws DataAccessException if access to the data store fails
     */
    public List findAllTasks() throws DataAccessException {
        final String methodName = "findAllTasks";
        LOGGER.entering(CLASSNAME, methodName);
        
        if ((FIND_ALL_TASKS_SQL == null) || (FIND_ALL_TASKS_SQL.equals(""))) {
            throw new DataAccessException("undefined or empty SQL template");
        }
        
        List allTasks = new ArrayList();
        
        Connection conx = null;
        try {
            conx = JDBCAccess.getConnection();
        } catch (SQLException ex) {
            String msg = "failed to open database connection";
            LOGGER.logp(Level.WARNING, CLASSNAME, methodName, msg, ex);
            throw new DataAccessException(msg, ex);
        }
        
        try {
            Statement select = conx.createStatement();
            
            if (!select.execute(FIND_ALL_TASKS_SQL)) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "task query execution returned false");
                throw new DataAccessException("task query failed");
            }
            
            ResultSet taskResults = select.getResultSet();
            while (taskResults.next()) {
                Long id = Long.valueOf(taskResults.getLong(1));
                String desc = taskResults.getString(2);
                
                // use Timestamp to preserve H:M:S
                Timestamp startTS = taskResults.getTimestamp(3);
                java.util.Date start = (startTS != null) ? new java.util.Date(startTS.getTime()) : null;
                
                // use Timestamp to preserve H:M:S
                Timestamp endTS = taskResults.getTimestamp(4);
                java.util.Date end = (endTS != null) ? new java.util.Date(endTS.getTime()) : null;
                
                allTasks.add(new Task(id, desc, start, end));
            }
            
            try {
                taskResults.close();
                select.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close select result set or statement", ex);
            }
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "task select failed", ex);
            throw new DataAccessException(ex);
        }
        
        LOGGER.exiting(CLASSNAME, methodName, allTasks);
        return allTasks;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param startDate the start date in the date range (inclusive)
     * @param endDate the end date in the date range (exclusive)
     * @return a list of tasks in the specified date range
     * @throws DataAccessException if access to the data store fails
     */
    public List findAllTasksInDateRange(java.util.Date startDate, java.util.Date endDate) throws DataAccessException {
        final String methodName = "findAllTasksInDateRange";
        LOGGER.entering(CLASSNAME, methodName, new Object[] {startDate, endDate});
        
        if ((FIND_ALL_TASKS_IN_DATE_RANGE_SQL == null) || (FIND_ALL_TASKS_IN_DATE_RANGE_SQL.equals(""))) {
            throw new DataAccessException("undefined or empty SQL template");
        }
        
        List allTasksInDateRange = new ArrayList();
        
        Connection conx = null;
        try {
            conx = JDBCAccess.getConnection();
        } catch (SQLException ex) {
            String msg = "failed to open database connection";
            LOGGER.logp(Level.WARNING, CLASSNAME, methodName, msg, ex);
            throw new DataAccessException(msg, ex);
        }
        
        try {
            PreparedStatement select = conx.prepareStatement(FIND_ALL_TASKS_IN_DATE_RANGE_SQL);
            select.setDate(1, new java.sql.Date(startDate.getTime()));
            select.setDate(2, new java.sql.Date(endDate.getTime()));
            
            if (!select.execute()) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "task query execution returned false");
                throw new DataAccessException("task query failed");
            }
            
            ResultSet taskResults = select.getResultSet();
            while (taskResults.next()) {
                Long id = Long.valueOf(taskResults.getLong(1));
                String desc = taskResults.getString(2);
                
                // use Timestamp to preserve H:M:S
                Timestamp startTS = taskResults.getTimestamp(3);
                java.util.Date start = (startTS != null) ? new java.util.Date(startTS.getTime()) : null;
                
                // use Timestamp to preserve H:M:S
                Timestamp endTS = taskResults.getTimestamp(4);
                java.util.Date end = (endTS != null) ? new java.util.Date(endTS.getTime()) : null;
                
                allTasksInDateRange.add(new Task(id, desc, start, end));
            }
            
            try {
                taskResults.close();
                select.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close select result set or statement", ex);
            }
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "task select failed", ex);
            throw new DataAccessException(ex);
        }
        
        LOGGER.exiting(CLASSNAME, methodName, allTasksInDateRange);
        return allTasksInDateRange;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param task the task to be stored
     * @throws DataAccessException if access to the data store fails
     */
    public void saveTask(Task task) throws DataAccessException {
        final String methodName = "saveTask";
        LOGGER.entering(CLASSNAME, methodName, task);
        
        if (task.getId() == null) {
            insertTask(task);
        } else {
            updateTask(task);
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private void insertTask(Task task) throws DataAccessException {
        final String methodName = "insertTask";
        LOGGER.entering(CLASSNAME, methodName, task);
        
        if ((INSERT_TASK_SQL == null) || (INSERT_TASK_SQL.equals(""))) {
            String msg = "undefined or empty SQL template";
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, msg);
            throw new DataAccessException(msg);
        }
        
        Connection conx = null;
        try {
            conx = JDBCAccess.getConnection();
        } catch (SQLException ex) {
            String msg = "failed to open database connection";
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, msg, ex);
            throw new DataAccessException(msg, ex);
        }
        
        Long id = null;
        
        try {
            PreparedStatement insert = conx.prepareStatement(INSERT_TASK_SQL);
            insert.setString(1, task.getDescription());
            
            int rowCount = insert.executeUpdate();
            LOGGER.logp(Level.INFO, CLASSNAME, methodName, "task insert rowCount = {0}", Integer.valueOf(rowCount));
            if (rowCount == 0) {
                throw new DataAccessException("task was not inserted");
            }
            
            try {
                insert.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close insert statement", ex);
            }
            
            CallableStatement identity = conx.prepareCall("{CALL IDENTITY()}");
            if (!identity.execute()) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "identity call failed");
                conx.rollback();
                throw new DataAccessException("unable to obtain inserted task ID");
            }
            
            ResultSet idResult = identity.getResultSet();
            if (!idResult.next()) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to retrieve task ID from result set");
                conx.rollback();
                throw new DataAccessException("unable to obtain inserted task ID");
            }
            
            id = Long.valueOf(idResult.getLong(1));
            
            try {
                idResult.close();
                identity.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close identity result set or statement", ex);
            }
            
            conx.commit();
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "task insert failed", ex);
            throw new DataAccessException(ex);
        }
        
        task.setId(id);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private void updateTask(Task task) throws DataAccessException {
        final String methodName = "updateTask";
        LOGGER.entering(CLASSNAME, methodName, task);
        
        if ((UPDATE_TASK_SQL == null) || (UPDATE_TASK_SQL.equals(""))) {
            throw new DataAccessException("undefined or empty SQL template");
        }
        
        Connection conx = null;
        try {
            conx = JDBCAccess.getConnection();
        } catch (SQLException ex) {
            String msg = "failed to open database connection";
            LOGGER.logp(Level.WARNING, CLASSNAME, methodName, msg, ex);
            throw new DataAccessException(msg, ex);
        }
        
        try {
            PreparedStatement update = conx.prepareStatement(UPDATE_TASK_SQL);
            update.setString(1, task.getDescription());
            // use Timestamp to preserve H:M:S
            update.setTimestamp(2, new java.sql.Timestamp(task.getStartTime().getTime()));
            update.setTimestamp(3, new java.sql.Timestamp(task.getEndTime().getTime()));
            update.setLong(4, task.getId());
            
            int rowCount = update.executeUpdate();
            LOGGER.logp(Level.INFO, CLASSNAME, methodName, "task update rowCount = {0}", Integer.valueOf(rowCount));
            if (rowCount == 0) {
                throw new DataAccessException("task was not updated");
            }
            
            try {
                update.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close update statement", ex);
            }
            
            conx.commit();
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "task update failed", ex);
            throw new DataAccessException(ex);
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
}

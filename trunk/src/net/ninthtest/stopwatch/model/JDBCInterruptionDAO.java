/*
 * $Id: JDBCInterruptionDAO.java 84 2008-04-07 17:29:50Z matt.zipay $
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
 * This class provides JDBC access to interruption data.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class JDBCInterruptionDAO implements InterruptionDAO {
    private static final String CLASSNAME = JDBCInterruptionDAO.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(JDBCInterruptionDAO.class.getPackage().getName());
    
    private static final String FIND_ALL_INTERRUPTIONS_SQL;
    
    private static final String FIND_INTERRUPTIONS_FOR_TASK_SQL;
    
    private static final String INSERT_INTERRUPTION_SQL;
    
    private static final String UPDATE_INTERRUPTION_SQL;
    
    static {
        final String methodName = "<clinit>";
        LOGGER.entering(CLASSNAME, methodName);
        
        Properties sqlProps = new Properties();
        try {
            sqlProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("interruption-sql.properties"));
        } catch (IOException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error loading JDBCInterruptionDAO SQL properties", ex);
        }
        
        FIND_ALL_INTERRUPTIONS_SQL = sqlProps.getProperty("findAllInterruptions");
        FIND_INTERRUPTIONS_FOR_TASK_SQL = sqlProps.getProperty("findInterruptionsForTask");
        INSERT_INTERRUPTION_SQL = sqlProps.getProperty("insertInterruption");
        UPDATE_INTERRUPTION_SQL = sqlProps.getProperty("updateInterruption");
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    /**
     * {@inheritDoc}
     *
     * @return a list of interruptions
     * @throws DataAccessException if access to the data store fails
     */
    public List findAllInterruptions() throws DataAccessException {
        final String methodName = "findAllInterruptions";
        LOGGER.entering(CLASSNAME, methodName);
        
        if ((FIND_ALL_INTERRUPTIONS_SQL == null) || (FIND_ALL_INTERRUPTIONS_SQL.equals(""))) {
            throw new DataAccessException("undefined or empty SQL template");
        }
        
        List allInterruptions = new ArrayList();
        
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
            
            if (!select.execute(FIND_ALL_INTERRUPTIONS_SQL)) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "interruption query execution returned false");
                throw new DataAccessException("interruption query failed");
            }
            
            ResultSet intrResults = select.getResultSet();
            while (intrResults.next()) {
                Long id = Long.valueOf(intrResults.getLong(1));
                Long taskId = Long.valueOf(intrResults.getLong(2));
                String desc = intrResults.getString(3);
                
                // use Timestamp to preserve H:M:S
                Timestamp startTS = intrResults.getTimestamp(4);
                java.util.Date start = (startTS != null) ? new java.util.Date(startTS.getTime()) : null;
                
                // use Timestamp to preserve H:M:S
                Timestamp endTS = intrResults.getTimestamp(5);
                java.util.Date end = (endTS != null) ? new java.util.Date(endTS.getTime()) : null;
                
                allInterruptions.add(new Interruption(id, taskId, desc, start, end));
            }
            
            try {
                intrResults.close();
                select.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close select result set or statement", ex);
            }
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "interruption select failed", ex);
            throw new DataAccessException(ex);
        }
        
        LOGGER.exiting(CLASSNAME, methodName, allInterruptions);
        return allInterruptions;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param task the task being queried
     * @return a list of interruptions for the specified task
     * @throws DataAccessException if access to the data store fails
     */
    public List findInterruptionsForTask(Task task) throws DataAccessException {
        final String methodName = "findInterruptionsForTask";
        LOGGER.entering(CLASSNAME, methodName, task);
        
        if ((FIND_INTERRUPTIONS_FOR_TASK_SQL == null) || (FIND_INTERRUPTIONS_FOR_TASK_SQL.equals(""))) {
            throw new DataAccessException("undefined or empty SQL template");
        }
        
        List intrsForTask = new ArrayList();
        
        Connection conx = null;
        try {
            conx = JDBCAccess.getConnection();
        } catch (SQLException ex) {
            String msg = "failed to open database connection";
            LOGGER.logp(Level.WARNING, CLASSNAME, methodName, msg, ex);
            throw new DataAccessException(msg, ex);
        }
        
        try {
            PreparedStatement select = conx.prepareStatement(FIND_INTERRUPTIONS_FOR_TASK_SQL);
            select.setLong(1, task.getId());
            
            if (!select.execute()) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "interruption query execution returned false");
                throw new DataAccessException("interruption query failed");
            }
            
            ResultSet intrResults = select.getResultSet();
            while (intrResults.next()) {
                Long id = Long.valueOf(intrResults.getLong(1));
                Long taskId = Long.valueOf(intrResults.getLong(2));
                String desc = intrResults.getString(3);
                
                // use Timestamp to preserve H:M:S
                Timestamp startTS = intrResults.getTimestamp(4);
                java.util.Date start = (startTS != null) ? new java.util.Date(startTS.getTime()) : null;
                
                // use Timestamp to preserve H:M:S
                Timestamp endTS = intrResults.getTimestamp(5);
                java.util.Date end = (endTS != null) ? new java.util.Date(endTS.getTime()) : null;
                
                intrsForTask.add(new Interruption(id, taskId, desc, start, end));
            }
            
            try {
                intrResults.close();
                select.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close select result set or statement", ex);
            }
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "interruption select failed", ex);
            throw new DataAccessException(ex);
        }
        
        LOGGER.exiting(CLASSNAME, methodName, intrsForTask);
        return intrsForTask;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param intr the interruption to be stored
     * @throws DataAccessException if access to the data store fails
     */
    public void saveInterruption(Interruption intr) throws DataAccessException {
        final String methodName = "saveInterruption";
        LOGGER.entering(CLASSNAME, methodName, intr);
        
        if (intr.getId() == null) {
            insertInterruption(intr);
        } else {
            updateInterruption(intr);
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private void insertInterruption(Interruption intr) throws DataAccessException {
        final String methodName = "insertInterruption";
        LOGGER.entering(CLASSNAME, methodName, intr);
        
        if ((INSERT_INTERRUPTION_SQL == null) || (INSERT_INTERRUPTION_SQL.equals(""))) {
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
        
        Long id = null;
        
        try {
            PreparedStatement insert = conx.prepareStatement(INSERT_INTERRUPTION_SQL);
            insert.setLong(1, intr.getRelatedTaskId());
            insert.setString(2, intr.getDescription());
            
            int rowCount = insert.executeUpdate();
            LOGGER.logp(Level.INFO, CLASSNAME, methodName, "interruption insert rowCount = {0}", Integer.valueOf(rowCount));
            if (rowCount == 0) {
                throw new DataAccessException("interruption was not inserted");
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
                throw new DataAccessException("unable to obtain inserted interruption ID");
            }
            
            ResultSet idResult = identity.getResultSet();
            if (!idResult.next()) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to retrieve interruption ID from result set");
                conx.rollback();
                throw new DataAccessException("unable to obtain inserted interruption ID");
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
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "interruption insert failed", ex);
            throw new DataAccessException(ex);
        }
        
        intr.setId(id);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private void updateInterruption(Interruption intr) throws DataAccessException {
        final String methodName = "updateInterruption";
        LOGGER.entering(CLASSNAME, methodName, intr);
        
        if ((UPDATE_INTERRUPTION_SQL == null) || (UPDATE_INTERRUPTION_SQL.equals(""))) {
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
            PreparedStatement update = conx.prepareStatement(UPDATE_INTERRUPTION_SQL);
            update.setString(1, intr.getDescription());
            // use Timestamp to preserve H:M:S
            update.setTimestamp(2, new java.sql.Timestamp(intr.getStartTime().getTime()));
            update.setTimestamp(3, new java.sql.Timestamp(intr.getEndTime().getTime()));
            update.setLong(4, intr.getId());
            update.setLong(5, intr.getRelatedTaskId());
            
            int rowCount = update.executeUpdate();
            LOGGER.logp(Level.INFO, CLASSNAME, methodName, "interruption update rowCount = {0}", Integer.valueOf(rowCount));
            if (rowCount == 0) {
                throw new DataAccessException("interruption was not updated");
            }
            
            try {
                update.close();
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to close update statement", ex);
            }
            
            conx.commit();
        } catch (SQLException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "interruption update failed", ex);
            throw new DataAccessException(ex);
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
}

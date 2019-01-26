/*
 * $Id: DAOFactory.java 80 2008-04-05 05:07:24Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides methods for obtaining DAO implementations at runtime.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 80 $
 */
public class DAOFactory {
    private static final String CLASSNAME = DAOFactory.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(DAOFactory.class.getPackage().getName());
    
    private static final Properties IMPLEMENTATIONS = new Properties();

    /**
     * Prepares any resource the factory will require to service requests.
     *
     * @throws DataAccessException if resource initialization fails
     */
    public static void initializeResources() throws DataAccessException {
        final String methodName = "initializeResources";
        LOGGER.entering(CLASSNAME, methodName);
        
        try {
            IMPLEMENTATIONS.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("dao-impl.properties"));
        } catch (IOException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error loading DAO implementations map", ex);
            throw new DataAccessException(ex);
        }
        
        if ("JDBC".equalsIgnoreCase(IMPLEMENTATIONS.getProperty("implementation"))) {
            try {
                // cache the database connection
                JDBCAccess.getConnection();
            } catch (SQLException ex) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to establish database connection", ex);
                throw new DataAccessException(ex);
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    /**
     * Returns the currently configured implementation for a DAO class.
     *
     * @param klass the specified DAO interface class
     * @return an instance of the appropriate DAO implementation class
     * @throws DataAccessException if resource initialization fails
     */
    public static Object getImplementation(Class klass) throws DataAccessException {
        final String methodName = "getImplementation";
        LOGGER.entering(CLASSNAME, methodName, klass);
        
        Object dao = null;
        
        String implClassName = IMPLEMENTATIONS.getProperty(klass.getName());
        if (implClassName != null) {
            Class implClass = null;
            
            try {
                implClass = Class.forName(implClassName);
            } catch (ClassNotFoundException ex) {
                String msg = "failed to load DAO implementation class";
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, msg, ex);
                throw new DataAccessException(msg, ex);
            }
            
            try {
                dao = implClass.newInstance();
            } catch (Exception ex) {
                String msg = "failed to instantiate DAO implementation";
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, msg, ex);
                throw new DataAccessException(msg, ex);
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName, dao);
        return dao;
    }
    
    /**
     * Destroys any resource the factory used to service requests.
     *
     * @throws DataAccessException if resources could not be cleaned up
     */
    public static void releaseResources() throws DataAccessException {
        final String methodName = "releaseResources";
        LOGGER.entering(CLASSNAME, methodName);
        
        if ("JDBC".equalsIgnoreCase(IMPLEMENTATIONS.getProperty("implementation"))) {
            try {
                // destroy the database connection
                JDBCAccess.releaseConnection();
                LOGGER.logp(Level.INFO, CLASSNAME, methodName, "database connection released successfully");
            } catch (SQLException ex) {
                LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to release database connection", ex);
                throw new DataAccessException(ex);
            }
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private DAOFactory() {
        // never instantiated
    }
}

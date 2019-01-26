/*
 * $Id: JDBCAccess.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides access to a JDBC data source.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public final class JDBCAccess {
    private static final String CLASSNAME = JDBCAccess.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(JDBCAccess.class.getPackage().getName());
    
    private static final String URL;
    
    private static final String USERNAME;
    
    private static final String PASSWORD;
    
    private static Connection conx;
    
    static {
        final String methodName = "<clinit>";
        LOGGER.entering(CLASSNAME, methodName);
        
        Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (Exception ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to load JDBC properties", ex);
        }
        
        try {
            Class.forName(props.getProperty("driver"));
        } catch (Exception ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to load JDBC driver", ex);
        }
        
        URL = props.getProperty("url");
        USERNAME = props.getProperty("username");
        PASSWORD = props.getProperty("password");
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    /**
     * Returns a connection to the JDBC data source. If a cached connection
     * exists, it is returned. If a connection is not cached, it is created and
     * cached for future use.
     *
     * @return a connection to the JDBC data source
     * @throws SQLException if the connection cannot be estalbished
     */
    public synchronized static Connection getConnection() throws SQLException {
        final String methodName = "getConnection";
        LOGGER.entering(CLASSNAME, methodName);
        
        if (conx == null) {
            conx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conx.setAutoCommit(false);
        }
        
        LOGGER.exiting(CLASSNAME, methodName, conx);
        return conx;
    }
    
    /**
     * Closes the connection to the JDBC data source.
     *
     * @throws SQLException if the connection is not closed successfully
     */
    public synchronized static void releaseConnection() throws SQLException {
        final String methodName = "releaseConnection";
        LOGGER.entering(CLASSNAME, methodName);
        
        if (conx != null) {
            try {
                conx.close();
            } finally {
                conx = null;
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private JDBCAccess() {
        // never instantiated
    }
}

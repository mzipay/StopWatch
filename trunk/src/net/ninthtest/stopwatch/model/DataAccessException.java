/*
 * $Id: DataAccessException.java 80 2008-04-05 05:07:24Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

/**
 * This class defines the exception type thrown when data access fails.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 80 $
 */
public class DataAccessException extends Exception {
    /**
     * Creates an empty data access exception.
     */
    public DataAccessException() {
    }
    
    /**
     * Creates a data access exception with an explanation message.
     *
     * @param message the explanation for this exception
     */
    public DataAccessException(String message) {
        super(message);
    }
    
    /**
     * Creates a data access exception that wraps another exception and
     * provides an explanatory message.
     *
     * @param message the explanation for this exception
     * @param cause the wrapped exception
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a data access exception that wraps another exception.
     *
     * @param cause the wrapped exception
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}

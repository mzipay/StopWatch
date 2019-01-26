/*
 * $Id: Interruption.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.util.Date;
import java.util.logging.Logger;

/**
 * This class describes an interruption task. An interruption task encapsulates
 * the relationship between itself and the task it interrupted.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class Interruption extends Task {
    private static final String CLASSNAME = Interruption.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(Interruption.class.getPackage().getName());

    private Long relatedTaskId;

    /**
     * Creates a new interruption associated with the task it interrupted.
     *
     * @param relatedTask the task that was interrupted
     */
    public Interruption(Task relatedTask) {
        super(null);

        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName, relatedTask);

        relatedTaskId = relatedTask.getId();
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    Interruption(Long id, Long relatedTaskId, String description, Date startTime, Date endTime) {
        super(id, description, startTime, endTime);
        this.relatedTaskId = relatedTaskId;
    }

    /**
     * Returns the interrupted task's ID.
     *
     * @return the ID of the task that was interrupted
     */
    public Long getRelatedTaskId() {
        return relatedTaskId;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param obj the other object
     * @return
     *      <CODE>true</CODE> if this interruption has the same ID and related
     *      task as the <CODE>obj</CODE> argument; <CODE>false</CODE> otherwise
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object obj) {
        Long thisId = getId();
        return ((obj instanceof Interruption) && (thisId != null) && (thisId.equals(((Interruption) obj).getId()))
        && (relatedTaskId != null) && relatedTaskId.equals(((Interruption) obj).getRelatedTaskId()));
    }
    
    /**
     * {@inheritDoc}
     *
     * @return
     *      the hash code of this interruption's ID if the ID is not
     *      <CODE>null</CODE>; <CODE>-1</CODE> otherwise
     * @see java.lang.Object#hashCode
     */
    public int hashCode() {
        Long thisId = getId();
        return (thisId != null) ? thisId.hashCode() : -1;
    }

    /**
     * {@inheritDoc}
     *
     * @return a string describing the properties of this interruption
     * @see java.lang.Object#toString
     */
    public String toString() {
        return new StringBuffer(CLASSNAME)
        .append("[id=").append(getId())
        .append(";related_task_id=").append(relatedTaskId)
        .append(";description=\"").append(getDescription())
        .append("\";startTime=").append(getStartTime())
        .append(";endTime=").append(getEndTime())
        .append("]")
        .toString();
    }
}

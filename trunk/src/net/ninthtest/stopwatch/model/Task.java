/*
 * $Id: Task.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class describes a common work task.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class Task {
    private static final String CLASSNAME = Task.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(Task.class.getPackage().getName());
    
    private Long id;
    
    private String description;
    
    private Date startTime;
    
    private Date endTime;
    
    /**
     * Creates a new task with a description.
     *
     * @param description a short description of the task
     */
    public Task(String description) {
        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName, description);

        this.description = description;

        LOGGER.exiting(CLASSNAME, methodName);
    }

    Task(Long id, String description, Date startTime, Date endTime) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Marks the start time for this task.
     */
    public void begin() {
        startTime = new Date();
    }
    
    /**
     * Marks the stop time for this task.
     */
    public void end() {
        endTime = new Date();
    }
    
    /**
     * Returns the task ID.
     *
     * @return the unique ID of this task
     */
    public Long getId() {
        return id;
    }
    
    void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Returns the task description.
     *
     * @return a short phrase describing this task
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Assigns a description to this task.
     *
     * @param description a short description for this task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the task start time.
     *
     * @return the start time of this task
     */
    public Date getStartTime() {
        return startTime;
    }
    
    /**
     * Returns the task stop time.
     *
     * @return the stop time of this task
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param obj the reference object with which to compare
     * @return
     *      <CODE>true</CODE> if <CODE>obj</CODE> is a task and
     *      <CODE>obj</CODE> has the same ID as this task; <CODE>false</CODE>
     *      otherwise
     */
    public boolean equals(Object obj) {
        return ((obj instanceof Task) && (id != null) && (id.equals(((Task) obj).id)));
    }
    
    /**
     * {@inheritDoc}
     *
     * @return
     *      the hash code of this task's ID if the ID is not <CODE>null</CODE>;
     *      <CODE>-1</CODE> otherwise
     */
    public int hashCode() {
        return (id != null) ? id.hashCode() : -1;
    }
    
    /**
     * {@inheritDoc}
     *
     * @return a string describing the properties of this task
     * @see java.lang.Object#toString
     */
    public String toString() {
        return new StringBuffer(CLASSNAME)
        .append("[id=").append(id)
        .append(";description=\"").append(description)
        .append("\";startTime=").append(startTime)
        .append(";endTime=").append(endTime)
        .append("]")
        .toString();
    }
}
